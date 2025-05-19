package io.droidevs.bmicalc.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.droidevs.bmicalc.domain.result.onFailure
import io.droidevs.bmicalc.domain.result.onSuccess
import io.droidevs.bmicalc.domain.usecases.bmi.GetBmiRecordByIdUseCase
import io.droidevs.bmicalc.domain.usecases.bmi.UpdateBmiRecordUseCase
import io.droidevs.bmicalc.domain.usecases.bmi.ValidateBmiInputUseCase
import io.droidevs.bmicalc.domain.usecases.unitsystem.GetUnitSystemUseCase
import io.droidevs.bmicalc.ui.helper.actions.BmiRecordEditAction
import io.droidevs.bmicalc.ui.helper.event.BmiRecordDetailsEvent
import io.droidevs.bmicalc.ui.helper.event.BmiRecordEditScreenEvent
import io.droidevs.bmicalc.ui.helper.states.BmiRecordEditState
import io.droidevs.bmicalc.ui.model.toDomainModel
import io.droidevs.bmicalc.ui.model.toUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BmiRecordEditViewModel(
    val getBmiRecordUseCase: GetBmiRecordByIdUseCase,
    val updateBmiRecordUseCase: UpdateBmiRecordUseCase,
    val getUnitSystem : GetUnitSystemUseCase,
    val applicationScope : CoroutineScope,
    val validationUseCase : ValidateBmiInputUseCase
) : ViewModel() {

    var recordId: Long = -1

    var _state = MutableStateFlow(BmiRecordEditState())
    val state = _state.asStateFlow()
        .onStart {
            loadState()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = BmiRecordEditState()
        )

    private val _eventSharedFlow = MutableSharedFlow<BmiRecordEditScreenEvent>()
    val eventSharedFlow = _eventSharedFlow.asSharedFlow()

    suspend fun loadState() {
        getUnitSystem().collect{ result ->
            result.onSuccess {
                _state.value = _state.value.copy(unitSystem = it)
            }.onFailure {
                _state.value = _state.value.copy(error = it)
            }
        }
        getBmiRecordUseCase(recordId).collect { result ->
            result.onSuccess {
                _state.value = _state.value.copy(original = it.toUiModel())
            }.onFailure {
                _state.value = _state.value.copy(error = it)
            }
        }
    }


    fun onAction(action: BmiRecordEditAction){
        applicationScope.launch {
            when(action){
                is BmiRecordEditAction.ReloadData -> {
                    loadState()
                }
                is BmiRecordEditAction.SaveRecord -> {
                    updateBmiRecordUseCase(state.value.edited.toDomainModel()).onSuccess {
                        _eventSharedFlow.tryEmit(BmiRecordEditScreenEvent.SavedSuccessfully)
                    }.onFailure {
                        _eventSharedFlow.tryEmit(BmiRecordEditScreenEvent.SaveFailed)
                    }
                }
                is BmiRecordEditAction.ChangeRecordData -> {
                    val record = action.record
                    _state.value = _state.value.copy(edited = record)
                    _state.value = _state.value.copy(validationResult = validationUseCase(unitSystem = _state.value.unitSystem, height = _state.value.edited.height, weight = _state.value.edited.weight))
                }
                is BmiRecordEditAction.ChangeRecordWeight -> {
                    _state.value = _state.value.copy(edited = _state.value.edited.copy(weight = action.weight))
                    _state.value = _state.value.copy(validationResult = validationUseCase(unitSystem = _state.value.unitSystem, height = _state.value.edited.height, weight = _state.value.edited.weight))
                }
                is BmiRecordEditAction.ChangeRecordHeight -> {
                    _state.value = _state.value.copy(edited = _state.value.edited.copy(height = action.height))
                    _state.value = _state.value.copy(validationResult = validationUseCase(unitSystem = _state.value.unitSystem, height = _state.value.edited.height, weight = _state.value.edited.weight))
                }
                is BmiRecordEditAction.RestoreData -> {
                    _state.value = _state.value.copy(edited = _state.value.original)
                    _eventSharedFlow.tryEmit(BmiRecordEditScreenEvent.DataRestoredSuccessfully)
                }
                is BmiRecordEditAction.CancelEdit -> {
                    _eventSharedFlow.tryEmit(BmiRecordEditScreenEvent.NavigateBack)
                }
            }
        }
    }
}