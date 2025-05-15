package io.droidevs.bmicalc.data.repository

import io.droidevs.bmicalc.data.db.dao.BmiGoalDao
import io.droidevs.bmicalc.data.db.entities.BmiGoalEntity
import io.droidevs.bmicalc.data.db.exceptions.flowRunCatchingDatabase
import io.droidevs.bmicalc.data.db.exceptions.runCatchingDatabaseResult
import io.droidevs.bmicalc.data.mapers.toDomain
import io.droidevs.bmicalc.data.mapers.toEntity
import io.droidevs.bmicalc.data.model.ActiveBmiGoal
import io.droidevs.bmicalc.dispatchers.CoroutineDispatcherProvider
import io.droidevs.bmicalc.domain.model.BmiGoal
import io.droidevs.bmicalc.domain.GoalFilter
import io.droidevs.bmicalc.domain.GoalFlag
import io.droidevs.bmicalc.domain.repository.BmiGoalRecordRepository
import io.droidevs.bmicalc.domain.result.mapResult
import io.droidevs.wallpaper.domain.result.errors.DatabaseError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import io.droidevs.wallpaper.domain.result.Result
class BmiGoalRecordRepositoryImpl(
    private val dao: BmiGoalDao,
    private val dispatcher: CoroutineDispatcherProvider
) : BmiGoalRecordRepository {

    override suspend fun completeGoal(goal: ActiveBmiGoal): Result<Long, DatabaseError> {
        return withContext(dispatcher.io) {
                runCatchingDatabaseResult {
                    dao.insertGoal(
                        BmiGoalEntity(
                            targetBmi = goal.targetBmi,
                            targetDate = goal.targetDate,
                            initialDate = goal.initialDate,
                            motivation = goal.motivation,
                            flag = GoalFlag.COMPLETED
                        )
                    )
                }
            }
    }

    override suspend fun abandonGoal(goal: ActiveBmiGoal): Result<Long, DatabaseError> =
        withContext(dispatcher.io) {
            runCatchingDatabaseResult {
                dao.insertGoal(
                    BmiGoalEntity(
                        targetBmi = goal.targetBmi,
                        targetDate = goal.targetDate,
                        initialDate = goal.initialDate,
                        motivation = goal.motivation,
                        flag = GoalFlag.ABANDONED
                    )
                )
            }
        }

    override fun searchGoals(
        filter: GoalFilter,
        page: Int,
        pageSize: Int
    ): Flow<Result<List<BmiGoal>, DatabaseError>> {
        return flowRunCatchingDatabase {
            dao.getGoals(
                status = filter.flag?.name,
                motivationQuery = filter.query,
                offset = page * pageSize,
                limit = pageSize
            )
        }.mapResult { goals ->
            goals.map {
                it.toDomain()
            }
        }
    }

    override fun getGoalHistory(): Flow<Result<List<BmiGoal>, DatabaseError>> {
        return flowRunCatchingDatabase {
            dao.getAllGoals()
        }.mapResult { goals ->
            goals.map {
                it.toDomain()
            }
        }
    }

    override suspend fun deleteGoal(goal: BmiGoal): Result<Int, DatabaseError> =
        withContext(dispatcher.io){
            runCatchingDatabaseResult {
                dao.deleteGoal(goal.toEntity())
            }
        }

    override suspend fun deleteGoal(goalId: Long): Result<Int, DatabaseError> =
        withContext(dispatcher.io){
            runCatchingDatabaseResult {
                dao.deleteGoalById(goalId)
            }
        }

    override suspend fun insertGoal(goal: BmiGoal): Result<Long, DatabaseError> =
        withContext(dispatcher.io){
            runCatchingDatabaseResult {
                dao.insertGoal(goal.toEntity())
            }
        }

}