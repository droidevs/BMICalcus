package io.droidevs.bmicalc.domain.usecases

import io.droidevs.bmicalc.domain.model.BmiGoal
import io.droidevs.bmicalc.domain.repository.BmiGoalRecordRepository
import io.droidevs.bmicalc.domain.result.errors.DatabaseError
import io.droidevs.wallpaper.domain.result.Result


class AddGoalRecordUseCase(
    private val repository : BmiGoalRecordRepository
) {

    suspend fun invoke(goal: BmiGoal): Result<Long, DatabaseError> = repository.insertGoal(goal)
}
