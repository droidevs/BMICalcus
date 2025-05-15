package io.droidevs.bmicalc.ui.helper.event

import io.droidevs.bmicalc.ui.helper.UiEvent


sealed class BmiGoalEvent : UiEvent {
    object GoalSaved : BmiGoalEvent()

    object GoalCompleted : BmiGoalEvent()
    object GoalAbandoned : BmiGoalEvent()
    object ProgressRecorded : BmiGoalEvent()
    data class GoalError(val message: String) : BmiGoalEvent()
}