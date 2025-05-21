package io.droidevs.bmicalc.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.droidevs.bmicalc.domain.model.BMICategory
import io.droidevs.bmicalc.data.model.UnitSystem
import io.droidevs.bmicalc.domain.model.BmiResult
import io.droidevs.bmicalc.domain.result.onFailure
import io.droidevs.bmicalc.domain.result.onSuccess
import io.droidevs.bmicalc.domain.usecases.validators.ValidateBmiInputUseCase
import io.droidevs.bmicalc.domain.usecases.unitsystem.UnitSystemUseCases
import io.droidevs.bmicalc.ui.helper.actions.BmiCalculatorScreenAction
import io.droidevs.bmicalc.ui.helper.event.BmiCalculatorScreenEvent
import io.droidevs.bmicalc.ui.helper.states.BmiCalculatorState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// BMICalculatorViewModel.kt
class BMICalculatorViewModel(
    val unitUseCases : UnitSystemUseCases,
    val validate : ValidateBmiInputUseCase
) : ViewModel() {


    private var _state = MutableStateFlow(BmiCalculatorState())

    val state = _state.asStateFlow()
        .onStart { loadState() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = BmiCalculatorState()
        )

    var _event = MutableSharedFlow<BmiCalculatorScreenEvent?>(
        replay = 3,
        extraBufferCapacity = 35
    )
    val event = _event.asSharedFlow()


    private suspend fun loadState(){
        unitUseCases.get().collect { result ->
            result.onSuccess { unitSystem ->
                _state.value = _state.value.copy(unitSystem = unitSystem)
            }.onFailure { error ->
                _state.value = _state.value.copy(error = error)
            }
        }
    }
    fun onAction(action: BmiCalculatorScreenAction) {
        viewModelScope.launch {
            when(action){
                is BmiCalculatorScreenAction.ChangeWeight -> setWeight(action.weight)
                is BmiCalculatorScreenAction.ChangeHeight -> setHeight(action.height)
                is BmiCalculatorScreenAction.ToggleUnitSystem -> toggleUnitSystem()
                BmiCalculatorScreenAction.SetUpOrManageGoal -> {
                    _event.emit(BmiCalculatorScreenEvent.NavigateToGoalSetUpScreen)
                }
                is BmiCalculatorScreenAction.CalculateBmi -> calculateBMI()
                BmiCalculatorScreenAction.ReloadData -> loadState()
            }
        }
    }


    private fun toggleUnitSystem(){
        _state.value = _state.value.copy(unitSystem = if(_state.value.unitSystem == UnitSystem.METRIC) UnitSystem.IMPERIAL else UnitSystem.METRIC)
        //todo : convert all inputs
    }

    private suspend fun setHeight(value: Float?) {
        _state.value = _state.value.copy(height = value)
        val validation = validate(_state.value.unitSystem,_state.value.height, _state.value.weight)
        _state.value = _state.value.copy(validation = validation)
    }

    suspend fun setWeight(value: Float?) {
        _state.value = _state.value.copy(weight = value)
        val validation = validate(_state.value.unitSystem,_state.value.height, _state.value.weight)
        _state.value = _state.value.copy(validation = validation)
    }

    private fun calculateBMI() {
        val heightValue = _state.value.height?: return
        val weightValue = _state.value.weight?: return

        val bmiResult = when (_state.value.unitSystem) {
            UnitSystem.METRIC -> calculateMetricBMI(heightValue, weightValue)
            UnitSystem.IMPERIAL -> calculateImperialBMI(heightValue, weightValue)
        }
        _state.value = _state.value.copy(bmiResult = bmiResult)
    }

    private fun calculateMetricBMI(heightCm: Float, weightKg: Float): BmiResult {
        val heightM = heightCm / 100
        val bmi = weightKg / (heightM * heightM)
        return BmiResult(bmi, BMICategory.getCategory(bmi))
    }

    private fun calculateImperialBMI(heightIn: Float, weightLb: Float): BmiResult {
        val bmi = (weightLb / (heightIn * heightIn)) * 703
        return BmiResult(bmi, BMICategory.getCategory(bmi))
    }

}

