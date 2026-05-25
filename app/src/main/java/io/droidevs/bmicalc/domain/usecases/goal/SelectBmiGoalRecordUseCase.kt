package io.droidevs.bmicalc.domain.usecases.goal

import io.droidevs.bmicalc.ui.model.GoalRecordUi

class SelectBmiGoalRecordUseCase {

    operator fun invoke(goals: List<GoalRecordUi>, goalId: Long): List<GoalRecordUi> {
        return goals.map { record ->
            if (record.id == goalId) record.copy(isSelected = true) else record
        }
    }
}
