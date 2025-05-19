package io.droidevs.bmicalc.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.droidevs.bmicalc.domain.result.onFailure
import io.droidevs.bmicalc.domain.result.onSuccess
import io.droidevs.bmicalc.domain.result.onSuccessWithResult
import io.droidevs.bmicalc.domain.usecases.bmi.DeleteBmiRecordUseCase
import io.droidevs.bmicalc.domain.usecases.bmi.GetBmiRecordByIdUseCase
import io.droidevs.bmicalc.domain.usecases.bmi.favorite.AddToFavoritesUseCase
import io.droidevs.bmicalc.domain.usecases.bmi.favorite.DeleteFavoredBmiRecordUseCase
import io.droidevs.bmicalc.domain.usecases.unitsystem.GetUnitSystemUseCase
import io.droidevs.bmicalc.ui.helper.ActionHandler
import io.droidevs.bmicalc.ui.helper.actions.BmiRecordDetailsAction
import io.droidevs.bmicalc.ui.helper.event.BmiRecordDetailsEvent
import io.droidevs.bmicalc.ui.helper.states.BmiRecordDetailScreenState
import io.droidevs.bmicalc.ui.model.toUiModel
import io.droidevs.bmicalc.ui.screens.BmiRecordDetailsScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BmiRecordDetailsViewModel(
    val getUnitSystem: GetUnitSystemUseCase,
    val getBmiRecord: GetBmiRecordByIdUseCase,
    val deleteBmiRecord: DeleteBmiRecordUseCase,
    val addToFavorites: AddToFavoritesUseCase,
    val deleteFromFavorite: DeleteFavoredBmiRecordUseCase
) : ViewModel(), ActionHandler<BmiRecordDetailsAction> {

    var recordId : Long = -1

    private val _state = MutableStateFlow(BmiRecordDetailScreenState())
    val state = _state
        .onStart {
            loadState()
        }
        .flowOn(Dispatchers.IO)
        .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = BmiRecordDetailScreenState()
    )

    private val _eventSharedFlow = MutableSharedFlow<BmiRecordDetailsEvent>()
    val eventSharedFlow = _eventSharedFlow.asSharedFlow()

    suspend fun loadState(){
        getUnitSystem().collect { result ->
            result.onSuccess { unitSystem ->
                _state.value = _state.value.copy(unitSystem = unitSystem)
            }.onFailure { error->
                _state.value = _state.value.copy(error = error)
            }
        }
        getBmiRecord(recordId).collect { result ->
            result.onSuccess { record->
                _state.value = _state.value.copy(record = record.toUiModel())
            }.onFailure {
                _state.value = _state.value.copy(error = it)
            }
        }

    }
    override fun onAction(action : BmiRecordDetailsAction){
        viewModelScope.launch {
            when(action){
                is BmiRecordDetailsAction.RefreshRecord -> {
                    _state.value = _state.value.copy(error = null)
                    loadState()
                }
                is BmiRecordDetailsAction.EditAction -> {
                    _eventSharedFlow.tryEmit(BmiRecordDetailsEvent.NavigateToEditPage(recordId))
                }
                is BmiRecordDetailsAction.DeleteAction -> {
                    deleteBmiRecord(recordId).onSuccess {
                        _eventSharedFlow.tryEmit(BmiRecordDetailsEvent.DeletedSuccessfully)
                    }.onFailure {
                        _eventSharedFlow.tryEmit(BmiRecordDetailsEvent.FailedToDelete)
                    }
                }
                is BmiRecordDetailsAction.FavoriteAction -> {
                    addToFavorites(recordId, action.note).onSuccess {
                        _eventSharedFlow.tryEmit(BmiRecordDetailsEvent.FavoredSuccessfully)
                    }.onFailure {
                        _eventSharedFlow.tryEmit(BmiRecordDetailsEvent.FailedToFavorite)
                    }
                }
                BmiRecordDetailsAction.UnfavoriteAction -> {
                    deleteFromFavorite(recordId).onSuccess {
                        _eventSharedFlow.tryEmit(BmiRecordDetailsEvent.UnfavoredSuccessfully)
                    }.onFailure {
                        _eventSharedFlow.tryEmit(BmiRecordDetailsEvent.FailedToUnfavored)
                    }
                }
            }
        }
    }

}