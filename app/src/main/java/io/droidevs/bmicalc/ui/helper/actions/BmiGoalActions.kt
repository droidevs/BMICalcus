package io.droidevs.bmicalc.ui.helper.actions

import io.droidevs.bmicalc.ui.helper.UiAction

sealed class BmiGoalAction : UiAction {

    data class SetTargetBmi(val value: String) : BmiGoalAction()
    data class SetTargetDate(val date: Long?) : BmiGoalAction()
    data class SetMotivation(val text: String) : BmiGoalAction()
    object SaveGoal : BmiGoalAction()

    object CompleteGoal : BmiGoalAction()
    object AbandonGoal : BmiGoalAction()

}