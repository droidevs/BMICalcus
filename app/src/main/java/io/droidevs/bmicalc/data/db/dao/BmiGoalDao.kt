package io.droidevs.bmicalc.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import io.droidevs.bmicalc.data.db.entities.BmiGoalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BmiGoalDao {
    @Insert
    suspend fun insertGoal(goal: BmiGoalEntity) : Long

    @Delete
    suspend fun deleteGoal(goal: BmiGoalEntity) : Int

    @Query("DELETE FROM bmi_goals WHERE id = :goalId")
    suspend fun deleteGoalById(goalId: Long) : Int

    @Query("SELECT * FROM bmi_goals ORDER BY target_date DESC")
    fun getAllGoals(): Flow<List<BmiGoalEntity>>

    @Query("""
        SELECT * FROM bmi_goals 
        WHERE flag = 'COMPLETED' 
        ORDER BY target_date DESC
    """)
    fun getCompletedGoals(): Flow<List<BmiGoalEntity>>

    @Query("""
        SELECT * FROM bmi_goals 
        WHERE (:status IS NULL OR flag = :status)
        AND (:motivationQuery IS NULL OR motivation LIKE '%' || :motivationQuery || '%')
        ORDER BY target_date DESC
        LIMIT :limit OFFSET :offset
    """)
    fun getGoals(
        status: String? = null,
        motivationQuery: String? = null,
        offset: Int,
        limit: Int
    ): Flow<List<BmiGoalEntity>>

    @Update
    suspend fun updateGoal(goal: BmiGoalEntity)

    // Optional: Count query for pagination metadata
    @Query("""
        SELECT COUNT(*) FROM bmi_goals 
        WHERE (:status IS NULL OR flag = :status)
        AND (:motivationQuery IS NULL OR motivation LIKE '%' || :motivationQuery || '%')
    """)
    suspend fun getGoalsCount(
        status: String? = null,
        motivationQuery: String? = null
    ): Int
}