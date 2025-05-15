package io.droidevs.bmicalc.domain.usecases.goal

import io.droidevs.bmicalc.data.repository.BmiGoalRecordRepository
import io.droidevs.bmicalc.domain.BmiGoal
import io.droidevs.bmicalc.domain.model.toEntity

class DeleteGoalUseCase(
    private val repository: BmiGoalRecordRepository
) {

    suspend fun invoke(goalId : Long) : Result<Unit> {
        return repository.deleteGoal(goalId)
    }

    suspend fun invoke(goal: BmiGoal) : Result<Unit> {
        return repository.deleteGoal(goal = goal.toEntity())
    }
}