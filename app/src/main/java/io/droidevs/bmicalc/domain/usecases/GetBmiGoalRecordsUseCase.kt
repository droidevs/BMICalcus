package io.droidevs.bmicalc.domain.usecases

import io.droidevs.bmicalc.domain.GoalFilter
import io.droidevs.bmicalc.domain.model.BmiGoal
import io.droidevs.bmicalc.domain.repository.BmiGoalRecordRepository
import io.droidevs.bmicalc.domain.result.errors.DatabaseError
import io.droidevs.wallpaper.domain.result.Result
import kotlinx.coroutines.flow.Flow

class GetBmiGoalRecordsUseCase(
    private val repository : BmiGoalRecordRepository
) {

    operator fun invoke(
        filter: GoalFilter?,
        page: Int,
        pageSize: Int
    ): Flow<Result<List<BmiGoal>, DatabaseError>> {
        val safeFilter = filter ?: GoalFilter(query = null, flag = null)
        return repository.searchGoals(
            goalFilter = safeFilter,
            page = page,
            pageSize = pageSize
        )
    }
}
