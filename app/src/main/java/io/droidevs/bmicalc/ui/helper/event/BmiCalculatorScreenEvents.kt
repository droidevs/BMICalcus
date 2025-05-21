package io.droidevs.bmicalc.ui.helper.event

sealed interface BmiCalculatorScreenEvent {

    data object NavigateToGoalSetUpScreen : BmiCalculatorScreenEvent
}