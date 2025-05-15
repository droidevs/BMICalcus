package io.droidevs.bmicalc.domain.usecases.goal

import io.droidevs.bmicalc.data.model.ActiveBmiGoal
import io.droidevs.bmicalc.data.repository.BmiGoalRecordRepository
import io.droidevs.wallpaper.domain.result.Result
import io.droidevs.wallpaper.domain.result.errors.DatabaseError

class CompleteGoalUseCase(
    private val repository: BmiGoalRecordRepository
) {

    suspend operator fun invoke(goal : ActiveBmiGoal) : Result<Long, DatabaseError>{
        return repository.completeGoal(goal)
    }
}