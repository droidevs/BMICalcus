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

    @Query("SELECT * FROM bmi_records WHERE id = :id")
    suspend fun get(id: Long): Flow<BmiRecordEntity>
    @Insert
    suspend fun insert(record: BmiRecordEntity)

    @Update
    suspend fun update(record: BmiRecordEntity)

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

    @Transaction
    @Query("""
    SELECT b.*,
           CASE WHEN f.id IS NULL THEN 0 ELSE 1 END as isFavorite
    FROM bmi_records b
    LEFT JOIN favorite_bmi_records f ON b.id = f.bmiRecordId
    WHERE b.id = :id
""")
    suspend fun getBmiRecordWithFavoriteById(id: Long): BmiRecordWithFavorite?

    @Transaction
    @Query("""
    SELECT b.*, 

           CASE WHEN f.id IS NULL THEN 0 ELSE 1 END as isFavorite
    FROM bmi_records b
    LEFT JOIN favorite_bmi_records f ON b.id = f.bmiRecordId
    ORDER BY b.date DESC
""")
    fun getAllBmiRecordsWithFavoriteStatus(): Flow<List<BmiRecordWithFavorite>>

    @Transaction
    @Query("""
        SELECT b.*,
               CASE WHEN f.id IS NULL THEN 0 ELSE 1 END as isFavorite
        FROM bmi_records b
        LEFT JOIN favorite_bmi_records f ON b.id = f.bmiRecordId
        WHERE (:bmiMin IS NULL OR b.bmi >= :bmiMin)
          AND (:bmiMax IS NULL OR b.bmi <= :bmiMax)
          AND (:weightMin IS NULL OR b.weight >= :weightMin)
          AND (:weightMax IS NULL OR b.weight <= :weightMax)
          AND (:heightMin IS NULL OR b.height >= :heightMin)
          AND (:heightMax IS NULL OR b.height <= :heightMax)
          AND (:timeMin IS NULL OR b.date >= :timeMin)
          AND (:timeMax IS NULL OR b.date <= :timeMax)
        ORDER BY 
        CASE WHEN :sortField = 'BMI' AND :sortOrder = 'ASC' THEN b.bmi END ASC,
        CASE WHEN :sortField = 'BMI' AND :sortOrder = 'DESC' THEN b.bmi END DESC,
        CASE WHEN :sortField = 'WEIGHT' AND :sortOrder = 'ASC' THEN b.weight END ASC,
        CASE WHEN :sortField = 'WEIGHT' AND :sortOrder = 'DESC' THEN b.weight END DESC,
        CASE WHEN :sortField = 'HEIGHT' AND :sortOrder = 'ASC' THEN b.height END ASC,
        CASE WHEN :sortField = 'HEIGHT' AND :sortOrder = 'DESC' THEN b.height END DESC,
        CASE WHEN :sortField = 'DATE' AND :sortOrder = 'ASC' THEN b.date END ASC,
        CASE WHEN :sortField = 'DATE' AND :sortOrder = 'DESC' THEN b.date END DESC
        LIMIT :limit OFFSET :offset
    """)
    fun getFilteredBmiRecordsPaged(
        bmiMin: Float?,
        bmiMax: Float?,
        weightMin: Float?,
        weightMax: Float?,
        heightMin: Float?,
        heightMax: Float?,
        timeMin: Long?,
        timeMax: Long?,
        sortField: String?,
        sortOrder: String?,
        offset: Int,
        limit: Int,
    ): Flow<List<BmiRecordWithFavorite>>

}