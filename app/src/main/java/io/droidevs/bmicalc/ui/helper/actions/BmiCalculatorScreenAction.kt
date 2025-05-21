package io.droidevs.bmicalc.ui.helper.actions

sealed class BmiCalculatorScreenAction {


    data object ReloadData : BmiCalculatorScreenAction()
    data object ToggleUnitSystem : BmiCalculatorScreenAction()

    data class ChangeHeight(val height : Float?) : BmiCalculatorScreenAction()

    data class ChangeWeight(val weight: Float?) : BmiCalculatorScreenAction()

    data object CalculateBmi : BmiCalculatorScreenAction()

    data object SetUpOrManageGoal : BmiCalculatorScreenAction()

}