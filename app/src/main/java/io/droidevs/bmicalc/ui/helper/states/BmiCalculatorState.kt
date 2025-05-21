package io.droidevs.bmicalc.ui.helper.states

import androidx.compose.runtime.Stable
import io.droidevs.bmicalc.data.model.ActiveBmiGoal
import io.droidevs.bmicalc.data.model.BmiScore
import io.droidevs.bmicalc.data.model.UnitSystem
import io.droidevs.bmicalc.domain.GoalStatus
import io.droidevs.bmicalc.domain.model.BmiInputValidationResult
import io.droidevs.bmicalc.domain.model.BmiResult
import io.droidevs.bmicalc.ui.helper.UiState
import io.droidevs.bmicalc.domain.result.errors.Error


@Stable
data class BmiCalculatorState(
    val height : Float? = null,
    val weight: Float? = null,
    val bmiResult: BmiResult? = null,
    val validation: BmiInputValidationResult = BmiInputValidationResult(),
    val unitSystem : UnitSystem = UnitSystem.METRIC,
    val bmiScore: BmiScore?= null,
    val activeBmiGoal : ActiveBmiGoal? = null,
    val goalStatus: GoalStatus = GoalStatus.NOT_SET,
    val error: Error? = null,
) : UiState