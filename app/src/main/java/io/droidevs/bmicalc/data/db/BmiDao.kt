package io.droidevs.bmicalc.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

// dao/BmiDao.kt
@Dao
interface BmiDao {

    @Query("SELECT * FROM bmi_records WHERE id = :id")
    suspend fun get(id: Long): Flow<BmiRecordEntity>
    @Insert
    suspend fun insert(record: BmiRecordEntity)

    @Query("DELETE FROM bmi_records WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("DELETE FROM bmi_records WHERE id in (:ids)")
    suspend fun delete(ids: List<Long>)
    @Query("SELECT * FROM bmi_records ORDER BY date DESC")
    fun getAllRecords(): Flow<List<BmiRecordEntity>>

    @Query("SELECT AVG(bmi) FROM bmi_records")
    fun getAverageBmi(): Flow<Float?>

    @Query("SELECT * FROM bmi_records ORDER BY date DESC LIMIT :limit OFFSET :offset")
    fun getRecordsPage(offset:Int, limit:Int) : Flow<List<BmiRecordEntity>>

    @Query("SELECT * FROM bmi_records WHERE date BETWEEN :start AND :end")
    fun getRecordsBetweenDates(start: Long, end: Long): Flow<List<BmiRecordEntity>>

    @Query("SELECT * FROM bmi_records WHERE " +
            "(:startTime IS NULL OR " +
            "date >= :startTime )" +
            "AND (:endTime IS NULL OR date < :endTime)" +
            "AND (:minBmi IS NULL OR bmi >= :minBmi) " +
            "AND (:maxBmi IS NULL OR bmi <= :maxBmi) " +
            "AND (:minWeight IS NULL OR weight >= :minWeight) " +
            "AND (:maxWeight IS NULL OR weight <= :maxWeight) " +
            "AND (:minHeight IS NULL OR height >= :minHeight) " +
            "AND (:maxHeight IS NULL OR height <= :maxHeight) " +
            "ORDER BY date DESC LIMIT :limit OFFSET :offset")
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
    ): Flow<List<BmiRecordEntity>>

}