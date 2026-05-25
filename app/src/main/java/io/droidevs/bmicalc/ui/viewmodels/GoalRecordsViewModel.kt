package io.droidevs.bmicalc.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.droidevs.bmicalc.data.pagging.GoalRecordPaginator
import io.droidevs.bmicalc.domain.GoalFilter
import io.droidevs.bmicalc.domain.usecases.goal.BmiGoalUseCases
import io.droidevs.bmicalc.domain.usecases.goal.SelectBmiGoalRecordUseCase
import io.droidevs.bmicalc.ui.helper.ActionHandler
import io.droidevs.bmicalc.ui.helper.actions.BmiGoalRecordAction
import io.droidevs.bmicalc.ui.helper.states.BmiGoalListState
import io.droidevs.bmicalc.ui.model.LoadingMode
import io.droidevs.bmicalc.ui.model.GoalRecordUi
import io.droidevs.bmicalc.ui.model.toUiModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class GoalRecordsViewModel(
    private val useCases : BmiGoalUseCases,
    private val selectGoalUseCase : SelectBmiGoalRecordUseCase
) : ViewModel() , ActionHandler<BmiGoalRecordAction>{

    private var isRefreshing = false
    private var isLoadingMore = false

    var state by mutableStateOf(BmiGoalListState())

    var goalFilter by mutableStateOf<GoalFilter?>(null)

    val paginator = GoalRecordPaginator(
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
            var records  = useCases.get(filter = goalFilter, page = nextPage , pageSize = 15).first()
            records
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


    fun changeFilter(filter: GoalFilter?){
        this.goalFilter = filter
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


    private fun select(record: GoalRecordUi){
        select(record.id)
    }

    private fun selectRecords(records : List<GoalRecordUi>){
        var updated = state.records
        for (record in records) {
            updated = selectGoalUseCase(updated, record.id)
        }
        state = state.copy(records = updated)
    }

    private fun selectIds(ids : List<Long>) {
        var updated = state.records
        for (recordId in ids) {
            updated = selectGoalUseCase(updated, recordId)
        }
        state = state.copy(records = updated)
    }

    private fun select(recordId : Long){
        state = state.copy(records = selectGoalUseCase(state.records, recordId))
    }

    private fun deselect(record: GoalRecordUi){
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


    override fun onAction(action: BmiGoalRecordAction) {
        when(action){
            is BmiGoalRecordAction.Refresh -> refresh()
            is BmiGoalRecordAction.LoadMore -> loadMore()
            is BmiGoalRecordAction.Select -> select(action.id)
            is BmiGoalRecordAction.Deselect -> deselect(action.id)
            is BmiGoalRecordAction.DeselectAll -> deselectAll()
            is BmiGoalRecordAction.Delete -> deselectAll()
            is BmiGoalRecordAction.ChangeFilter -> changeFilter(action.goalFilter)
            else -> throw IllegalStateException("Unknown action")
        }
    }
}
