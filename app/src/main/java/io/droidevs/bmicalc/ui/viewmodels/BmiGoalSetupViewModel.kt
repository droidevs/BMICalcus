package io.droidevs.bmicalc.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.droidevs.bmicalc.domain.GoalStatus
import io.droidevs.bmicalc.domain.result.onSuccess
import io.droidevs.bmicalc.domain.usecases.goal.AbandonGoalUseCase
import io.droidevs.bmicalc.domain.usecases.goal.CompleteGoalUseCase
import io.droidevs.bmicalc.domain.usecases.EvaluateBmiGoalProgressUseCase
import io.droidevs.bmicalc.domain.usecases.goal.GetActiveBmiGoalUseCase
import io.droidevs.bmicalc.domain.usecases.GetBmiScoreUseCase
import io.droidevs.bmicalc.domain.usecases.goal.SetActiveBmiGoalUseCase
import io.droidevs.bmicalc.ui.helper.ActionHandler
import io.droidevs.bmicalc.ui.helper.actions.BmiGoalAction
import io.droidevs.bmicalc.ui.helper.states.BmiGoalSetUpScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BmiGoalSetupViewModel(
    val getActiveGoal: GetActiveBmiGoalUseCase,
    val setActiveGoal: SetActiveBmiGoalUseCase,
    val getScore: GetBmiScoreUseCase,
    val evaluateBmiGoal: EvaluateBmiGoalProgressUseCase,
    val complete: CompleteGoalUseCase,
    val abandon : AbandonGoalUseCase
) : ViewModel() , ActionHandler<BmiGoalAction> {

    var _state  = MutableStateFlow(BmiGoalSetUpScreenState())

    val state =  _state
        .onStart {
            initialize()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = BmiGoalSetUpScreenState()
        )


    suspend fun initialize(){
        getScore().collect { result ->
            result.onSuccess { bmiScore ->
                _state.value = _state.value.copy(
                    bmiScore = bmiScore,
                    goalStatus = this._state.value.activeBmiGoal?.let { evaluateBmiGoal(goal = it, currentBmi = bmiScore.value) }?: GoalStatus.NOT_SET
                )
            }
        }
        getActiveGoal().collect { result ->
            result.onSuccess { goal ->
                _state.value = _state.value.copy(
                    activeBmiGoal = goal,
                    goalStatus = this._state.value.bmiScore?.let { evaluateBmiGoal(goal = goal, currentBmi = it.value) }?: GoalStatus.NOT_SET
                )
            }
        }
    }

    override fun onAction(action: BmiGoalAction){
        when(action){
            BmiGoalAction.CompleteGoal -> {
                _state.value.activeBmiGoal?.let {
                    viewModelScope.launch {
                        complete(it) // todo: handle failure result
                    }
                }
            }

            BmiGoalAction.AbandonGoal -> {
                _state.value.activeBmiGoal?.let {
                    viewModelScope.launch {
                        abandon(it) // todo: handle failure result
                    }
                }
            }
            BmiGoalAction.SaveGoal -> TODO()
            is BmiGoalAction.SetMotivation -> {
                _state.value = _state.value.copy(
                    goalInput = _state.value.goalInput.copy(
                        motivation = action.text,
                    )
                )
            }
            is BmiGoalAction.SetTargetBmi -> {
                _state.value = _state.value.copy(
                    goalInput = _state.value.goalInput.copy(
                        targetBmi = action.value,
                    )
                )
            }
            is BmiGoalAction.SetTargetDate -> {
                _state.value = _state.value.copy(
                    goalInput = _state.value.goalInput.copy(
                        targetDate = action.date,
                    )
                )
            }
        }
    }
}