package io.droidevs.bmicalc.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.droidevs.bmicalc.data.pagging.BmiRecordPaginator
import io.droidevs.bmicalc.domain.model.BmiFilter
import io.droidevs.bmicalc.domain.model.BmiRecord
import io.droidevs.bmicalc.domain.result.onFailureSuspend
import io.droidevs.bmicalc.domain.result.onSuccessSuspend
import io.droidevs.bmicalc.domain.usecases.BmiUseCases
import io.droidevs.bmicalc.domain.usecases.SelectBmiRecordUseCase
import io.droidevs.bmicalc.ui.helper.ActionHandler
import io.droidevs.bmicalc.ui.helper.actions.HistoryScreenAction
import io.droidevs.bmicalc.ui.helper.event.BmiRecordHistoryScreenEvent
import io.droidevs.bmicalc.ui.helper.states.HistoryState
import io.droidevs.bmicalc.ui.model.BmiRecordUi
import io.droidevs.bmicalc.ui.model.LoadingMode
import io.droidevs.bmicalc.ui.model.toUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BmiRecordHistoryViewModel(
    private val useCases: BmiUseCases,
    private val selectBmiRecordUseCase : SelectBmiRecordUseCase,
    private val applicationCoroutine : CoroutineScope
) : ViewModel() , ActionHandler<HistoryScreenAction> {

    private var isRefreshing = false
    private var isLoadingMore = false

    private var _state = MutableStateFlow(HistoryState())
    var state = _state.asStateFlow()
        .onStart { loadState() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HistoryState()
        )

    private var _event = MutableSharedFlow<BmiRecordHistoryScreenEvent>()
    val event = _event.asSharedFlow()


    private fun loadState(){
        loadNextItems()
    }

    val paginator = BmiRecordPaginator(
        initialKey = _state.value.page,
        onLoadUpdated = {
            if(it){
                if(isRefreshing)
                    _state.value = state.value.copy(mode = LoadingMode.Refresh)
                if(isLoadingMore)
                    _state.value = state.value.copy(mode = LoadingMode.Append)
            }
            else {
                isLoadingMore = false
                isRefreshing = false
                _state.value = state.value.copy(mode = LoadingMode.Ide)
            }
        },
        onRequest = { nextPage ->
            useCases.get(filter = _state.value.bmiFilter, page = nextPage , size = 15).first()
        },
        getNextKey = {
            state.value.page + 1
        },
        onError = {
            _state.value = state.value.copy(error = it)
        },
        onSuccess = { items , newKey ->
            _state.value = _state.value.copy(
                records =
                (state.value.records
                    + items.map {
                        it.toUiModel()
                    }
                ),
                endReached = items.isEmpty(),
                page = newKey
            )
        }
    )


    fun changeFilter(filter: BmiFilter){
        _state.value = state.value.copy(bmiFilter = filter)
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
        _state.value = state.value.copy(records = emptyList())
        paginator.reset()
    }


    private fun select(record: BmiRecord){
        select(record.id)
    }

    private fun select(ids : List<Long>) {
        for(recordId in ids) select(recordId)
    }

    private fun select(recordId : Long){
        selectBmiRecordUseCase(state.value.records,recordId)
    }

    private fun deselect(wallpaper: BmiRecordUi){
        deselect(wallpaper.id)
    }

    private fun deselect(recordId: Long){
        val records = state.value.records
        val record = records.find {
            it.id == recordId
        }
        record?.isSelected = false
    }


    private fun deselectAll(){
        val records = state.value.records
        for(record in records) {
            record.isSelected = false
        }
    }


    override fun onAction(action: HistoryScreenAction) {
        when(action){
            is HistoryScreenAction.Refresh -> refresh()
            is HistoryScreenAction.LoadMore -> loadMore()
            is HistoryScreenAction.Select -> select(action.id)
            is HistoryScreenAction.Deselect -> deselect(action.id)
            is HistoryScreenAction.DeselectAll -> deselectAll()
            is HistoryScreenAction.ChangeFilter -> {
                changeFilter(action.bmiFilter)
            }
            is HistoryScreenAction.Delete -> {
                applicationCoroutine.launch {
                    useCases.delete(action.record.id).onSuccessSuspend {
                        _event.emit(BmiRecordHistoryScreenEvent.RecordDeletedSuccessfully)
                        _event.emit(BmiRecordHistoryScreenEvent.NavigateBack)
                    }.onFailureSuspend {
                        _event.emit(BmiRecordHistoryScreenEvent.RecordDeleteFailed)
                    }
                }
            }
            else -> throw IllegalStateException("Unknown action")
        }
    }
}