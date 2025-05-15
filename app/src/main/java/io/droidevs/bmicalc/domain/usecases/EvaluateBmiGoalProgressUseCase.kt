package io.droidevs.bmicalc.domain.usecases

import io.droidevs.bmicalc.data.model.ActiveBmiGoal
import io.droidevs.bmicalc.domain.GoalStatus
import kotlinx.datetime.Clock
import kotlin.math.abs
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class EvaluateBmiGoalProgressUseCase {

    operator fun invoke(
        goal: ActiveBmiGoal,
        currentBmi: Float,
        currentDate: Long = Clock.System.now().toEpochMilliseconds()
    ): GoalStatus {
        // Handle cases where goal isn't properly set
        if (goal.targetBmi == 0f || goal.initialBmi == 0f) {
            return GoalStatus.NOT_SET
        }

        // Check if goal is already achieved
        val isGoalAchieved = when {
            goal.targetBmi > goal.initialBmi -> currentBmi >= goal.targetBmi  // Weight gain goal
            else -> currentBmi <= goal.targetBmi                              // Weight loss goal
        }

        if (isGoalAchieved) {
            return GoalStatus.ACHIEVED
        }

        // If no target date is set, just return ON_TRACK
        if (goal.targetDate == null) {
            return GoalStatus.ON_TRACK
        }

        // Calculate progress percentages
        val totalBmiChange = abs(goal.targetBmi - goal.initialBmi)
        val achievedBmiChange = abs(currentBmi - goal.initialBmi)
        val progressPercentage = (achievedBmiChange / totalBmiChange).coerceIn(0f, 1f)

        // Calculate time percentages
        val totalDuration = (goal.targetDate - goal.initialDate).toDuration(DurationUnit.MILLISECONDS)
        val elapsedDuration = (currentDate - goal.initialDate).toDuration(DurationUnit.MILLISECONDS)
        val timePercentage = (elapsedDuration / totalDuration).coerceIn(0.0, 1.0)

        // Determine status based on comparison
        return when {
            progressPercentage >= timePercentage.toFloat() + 0.1f -> GoalStatus.AHEAD_OF_SCHEDULE
            progressPercentage <= timePercentage.toFloat() - 0.1f -> GoalStatus.BEHIND_SCHEDULE
            else -> GoalStatus.ON_TRACK
        }
    }
}