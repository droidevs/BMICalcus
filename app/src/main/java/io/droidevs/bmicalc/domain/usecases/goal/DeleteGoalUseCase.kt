package io.droidevs.bmicalc.domain.usecases.goal

import io.droidevs.bmicalc.domain.repository.BmiGoalRecordRepository
import io.droidevs.bmicalc.domain.BmiGoal
import io.droidevs.wallpaper.domain.result.errors.DatabaseError
import io.droidevs.wallpaper.domain.result.Result
class DeleteGoalUseCase(
    private val repository: BmiGoalRecordRepository
) {

    suspend fun invoke(goalId : Long) : Result<Int,DatabaseError> {
        return repository.deleteGoal(goalId)
    }

    suspend fun invoke(goal: BmiGoal) : Result<Int,DatabaseError> {
        return repository.deleteGoal(goal = goal)
    }
}