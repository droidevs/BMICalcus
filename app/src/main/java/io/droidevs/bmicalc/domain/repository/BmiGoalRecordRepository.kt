package io.droidevs.bmicalc.domain.repository

import io.droidevs.bmicalc.data.model.ActiveBmiGoal
import io.droidevs.bmicalc.domain.model.BmiGoal
import io.droidevs.bmicalc.domain.GoalFilter
import kotlinx.coroutines.flow.Flow
import io.droidevs.wallpaper.domain.result.Result
import io.droidevs.bmicalc.domain.result.errors.DatabaseError

interface BmiGoalRecordRepository {

    /**
     * Marks a goal as completed and archives it
     * @return [Result.Success] if operation succeeded, [Result.Failure] with [GoalRepositoryException] otherwise
     */
    suspend fun completeGoal(goal: ActiveBmiGoal): Result<Long, DatabaseError>

    /**
     * Marks a goal as abandoned and archives it
     * @return [Result.Success] if operation succeeded, [Result.Failure] with [GoalRepositoryException] otherwise
     */
    suspend fun abandonGoal(goal: ActiveBmiGoal): Result<Long, DatabaseError>

    /**
     * Searches goals with pagination and optional filters
     * @param status Optional status filter
     * @param query Optional text search in motivation field
     * @param page Page number (0-indexed)
     * @param pageSize Number of items per page
     * @return Flow emitting [Result.Success] with list of goals or [Result.Failure] on error
     */
    fun searchGoals(
        goalFilter: GoalFilter,
        page: Int = 0,
        pageSize: Int = 20
    ): Flow<Result<List<BmiGoal>, DatabaseError>>

    /**
     * Retrieves full goal history
     * @return Flow emitting [Result.Success] with list of all goals or [Result.Failure] on error
     */
    fun getGoalHistory(): Flow<Result<List<BmiGoal>, DatabaseError>>


    suspend fun deleteGoal(goal: BmiGoal) : Result<Int, DatabaseError>

    suspend fun deleteGoal(goalId: Long) : Result<Int, DatabaseError>

    suspend fun insertGoal(goal : BmiGoal) : Result<Long, DatabaseError>

}