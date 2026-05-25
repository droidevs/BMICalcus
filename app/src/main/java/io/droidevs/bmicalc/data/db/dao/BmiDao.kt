package io.droidevs.bmicalc.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import io.droidevs.bmicalc.data.db.entities.BmiRecordEntity
import io.droidevs.bmicalc.data.db.relations.BmiRecordWithFavorite

import kotlinx.coroutines.flow.Flow

// dao/BmiDao.kt
@Dao
interface BmiDao {

    @Transaction
    @Query("""
        SELECT b.*,
               CASE WHEN f.id IS NULL THEN 0 ELSE 1 END as isFavorite
        FROM bmi_records b
        LEFT JOIN favorite_bmi_records f ON b.id = f.bmiRecordId
        WHERE b.id = :id
    """)
    fun get(id: Long): Flow<BmiRecordWithFavorite>
    @Insert
    suspend fun insert(record: BmiRecordEntity) : Long

    @Update
    suspend fun update(record: BmiRecordEntity) : Int

    @Query("DELETE FROM bmi_records WHERE id = :id")
    suspend fun delete(id: Long) : Int

    @Query("DELETE FROM bmi_records WHERE id in (:ids)")
    suspend fun delete(ids: List<Long>) : Int
    @Query("SELECT * FROM bmi_records ORDER BY date DESC")
    fun getAllRecords(): Flow<List<BmiRecordEntity>>

    @Query("SELECT AVG(bmi) FROM bmi_records")
    fun getAverageBmi(): Flow<Float>

    @Query("SELECT * FROM bmi_records ORDER BY date DESC LIMIT :limit OFFSET :offset")
    fun getRecordsPage(offset:Int, limit:Int) : Flow<List<BmiRecordEntity>>

    @Transaction
    @Query("""
        SELECT b.*,
               CASE WHEN f.id IS NULL THEN 0 ELSE 1 END as isFavorite
        FROM bmi_records b
        LEFT JOIN favorite_bmi_records f ON b.id = f.bmiRecordId
        WHERE b.date BETWEEN :start AND :end
        ORDER BY b.date DESC
    """)
    fun getRecordsBetweenDates(start: Long, end: Long): Flow<List<BmiRecordWithFavorite>>

    @Transaction
    @Query(
        "SELECT b.*, " +
            "CASE WHEN f.id IS NULL THEN 0 ELSE 1 END as isFavorite " +
            "FROM bmi_records b " +
            "LEFT JOIN favorite_bmi_records f ON b.id = f.bmiRecordId " +
            "WHERE (:startTime IS NULL OR b.date >= :startTime) " +
            "AND (:endTime IS NULL OR b.date < :endTime) " +
            "AND (:minBmi IS NULL OR b.bmi >= :minBmi) " +
            "AND (:maxBmi IS NULL OR b.bmi <= :maxBmi) " +
            "AND (:minWeight IS NULL OR b.weight >= :minWeight) " +
            "AND (:maxWeight IS NULL OR b.weight <= :maxWeight) " +
            "AND (:minHeight IS NULL OR b.height >= :minHeight) " +
            "AND (:maxHeight IS NULL OR b.height <= :maxHeight) " +
            "ORDER BY b.date DESC LIMIT :limit OFFSET :offset"
    )
    fun getFilteredRecords(
        startTime: Long? = null,
        endTime: Long? = null,
        minBmi: Float? = null,
        maxBmi: Float? = null,
        minWeight: Float? = null,
        maxWeight: Float? = null,
        minHeight: Float? = null,
        maxHeight: Float? = null,
        offset: Int,
        limit: Int,
    ): Flow<List<BmiRecordWithFavorite>>

    @Transaction
    @Query("""
    SELECT b.*,
           CASE WHEN f.id IS NULL THEN 0 ELSE 1 END as isFavorite
    FROM bmi_records b
    LEFT JOIN favorite_bmi_records f ON b.id = f.bmiRecordId
    WHERE b.id = :id
""")
    suspend fun getBmiRecordWithFavoriteById(id: Long): BmiRecordWithFavorite?


}
