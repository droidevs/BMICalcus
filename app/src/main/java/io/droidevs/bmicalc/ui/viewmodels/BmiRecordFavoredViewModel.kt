package io.droidevs.bmicalc.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.droidevs.bmicalc.data.pagging.FavoredRecordPaginator
import io.droidevs.bmicalc.domain.FavoredFilter
import io.droidevs.bmicalc.domain.usecases.bmi.favorite.FavoredBmiUseCases
import io.droidevs.bmicalc.domain.usecases.SelectFavoredBmiUseCase
import io.droidevs.bmicalc.ui.helper.ActionHandler
import io.droidevs.bmicalc.ui.helper.actions.FavoredBmiRecordListActions
import io.droidevs.bmicalc.ui.helper.states.FavoredBmiListState
import io.droidevs.bmicalc.ui.model.LoadingMode
import io.droidevs.bmicalc.ui.model.FavoredBmiUi
import io.droidevs.bmicalc.ui.model.toUiModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class BmiRecordFavoredViewModel(
    private val useCases : FavoredBmiUseCases,
    private val selectFavoredUseCase : SelectFavoredBmiUseCase
) : ViewModel() , ActionHandler<FavoredBmiRecordListActions> {


    private var isRefreshing = false
    private var isLoadingMore = false

    var state by mutableStateOf(FavoredBmiListState())

    var filter by mutableStateOf<FavoredFilter?>(null)

    val paginator = FavoredRecordPaginator(
        initialKey = state.page,
        onLoadUpdated = {
            if(it){
                if(isRefreshing)
                    state = state.copy(mode = LoadingMode.Refresh)
                if(isLoadingMore)
                    state = state.copy(mode = LoadingMode.Append)
            }
            else {
                isLoadingMore = false
                isRefreshing = false
                state = state.copy(mode = LoadingMode.Ide)
            }
        },
        onRequest = { nextPage ->
            useCases.get(filter = filter, page = nextPage , pageSize = 15).first()
        },
        getNextKey = {
            state.page + 1
        },
        onError = {
            state = state.copy(error = it)
        },
        onSuccess = { items , newKey ->
            state = state.copy(
                records =
                (state.records
                        + items.map {
                    it.toUiModel()
                }
                        ),
                endReached = items.isEmpty(),
                page = newKey
            )
        }
    )


    fun changeFilter(filter: FavoredFilter?){
        this.filter = filter
        refresh()
    }


    private fun refresh(){
        if (isRefreshing or isLoadingMore)
            return
        isRefreshing = true
        reset()
        loadNextItems()
    }

    private fun loadMore(){
        if (isLoadingMore or isRefreshing)
            return
        isLoadingMore = true
        loadNextItems()
    }

    fun loadNextItems(){
        viewModelScope.launch {
            paginator.loadNextPage()
        }
    }

    private fun reset(){
        state = state.copy(
            records = emptyList(),
            page = 0,
            endReached = false
        )
        paginator.reset()
    }


    private fun select(record: FavoredBmiUi){
        select(record.id)
    }

    private fun select(ids : List<Long>) {
        var updated = state.records
        for (recordId in ids) {
            updated = selectFavoredUseCase(updated, recordId)
        }
        state = state.copy(records = updated)
    }

    private fun select(recordId : Long){
        state = state.copy(records = selectFavoredUseCase(state.records, recordId))
    }

    private fun deselect(record: FavoredBmiUi){
        deselect(record.id)
    }

    private fun deselect(recordId: Long){
        state = state.copy(
            records = state.records.map { record ->
                if (record.id == recordId) record.copy(isSelected = false) else record
            }
        )
    }


    private fun deselectAll(){
        state = state.copy(
            records = state.records.map { record -> record.copy(isSelected = false) }
        )
    }

    private fun delete(favoredId : Long){
        viewModelScope.launch {
            useCases.delete(favoredId)
        }
    }


    override fun onAction(action: FavoredBmiRecordListActions) {
        when(action){
            is FavoredBmiRecordListActions.Refresh -> refresh()
            is FavoredBmiRecordListActions.LoadMore -> loadMore()
            is FavoredBmiRecordListActions.Select -> select(action.id)
            is FavoredBmiRecordListActions.Deselect -> deselect(action.id)
            is FavoredBmiRecordListActions.DeselectAll -> deselectAll()
            is FavoredBmiRecordListActions.Delete -> delete(action.record.id)
            else -> throw IllegalStateException("Unknown action")
        }
    }

}
