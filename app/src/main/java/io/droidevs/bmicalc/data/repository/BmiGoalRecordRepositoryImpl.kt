package io.droidevs.bmicalc.data.repository

import io.droidevs.bmicalc.data.db.InfrastructureException
import io.droidevs.bmicalc.data.db.dao.BmiGoalDao
import io.droidevs.bmicalc.data.db.entities.BmiGoalEntity
import io.droidevs.bmicalc.data.db.exceptions.flowRunCatchingDatabase
import io.droidevs.bmicalc.data.db.exceptions.runCatchingDatabaseResult
import io.droidevs.bmicalc.data.model.ActiveBmiGoal
import io.droidevs.bmicalc.dispatchers.CoroutineDispatcherProvider
import io.droidevs.bmicalc.domain.GoalFlag
import io.droidevs.wallpaper.domain.result.errors.DatabaseError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
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
        flag: GoalFlag?,
        query: String?,
        page: Int,
        pageSize: Int
    ): Flow<Result<List<BmiGoalEntity>, DatabaseError>> {
        return flowRunCatchingDatabase {
            dao.getGoals(
                status = flag?.name,
                motivationQuery = query,
                offset = page * pageSize,
                limit = pageSize
            )
        }.flowOn(dispatcher.io)
    }

    override fun getGoalHistory(): Flow<Result<List<BmiGoalEntity>, DatabaseError>> {
        return flowRunCatchingDatabase {
            dao.getAllGoals()
        }.flowOn(dispatcher.io)
    }

    override suspend fun deleteGoal(goal: BmiGoalEntity): Result<Int, DatabaseError> =
        withContext(dispatcher.io){
            runCatchingDatabaseResult {
                dao.deleteGoal(goal)
            }
        }

    override suspend fun deleteGoal(goalId: Long): Result<Int, DatabaseError> =
        withContext(dispatcher.io){
            runCatchingDatabaseResult {
                dao.deleteGoalById(goalId)
            }
        }

    override suspend fun insertGoal(goal: BmiGoalEntity): Result<Long, DatabaseError> =
        withContext(dispatcher.io){
            runCatchingDatabaseResult {
                dao.insertGoal(goal)
            }
        }

}