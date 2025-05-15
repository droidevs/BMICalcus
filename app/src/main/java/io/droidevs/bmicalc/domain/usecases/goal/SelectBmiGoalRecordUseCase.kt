package io.droidevs.bmicalc.domain.usecases.goal

import io.droidevs.bmicalc.ui.model.GoalRecordUi

class SelectBmiGoalRecordUseCase {


    operator fun  invoke(goals: List<GoalRecordUi>, goalId: Long){
        val record = goals.find {
            it.id == goalId
        }
        record?.isSelected = true
    }
}