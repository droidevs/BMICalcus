package io.droidevs.bmicalc.ui.helper.states

import androidx.compose.runtime.Stable
import io.droidevs.bmicalc.data.model.ActiveBmiGoal
import io.droidevs.bmicalc.data.model.BmiScore
import io.droidevs.bmicalc.domain.GoalStatus
import io.droidevs.bmicalc.ui.helper.UiState
import io.droidevs.bmicalc.ui.model.GoalInput


@Stable
data class BmiGoalSetUpScreenState(
    val goalInput: GoalInput = GoalInput(),
    val bmiScore: BmiScore?= null,
    val activeBmiGoal : ActiveBmiGoal? = null,
    val goalStatus: GoalStatus = GoalStatus.NOT_SET,
) : UiState