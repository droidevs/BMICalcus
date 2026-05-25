package io.droidevs.bmicalc.data.db.dao

import androidx.room.*
import io.droidevs.bmicalc.data.db.entities.FavoriteBmiRecordEntity
import io.droidevs.bmicalc.data.db.relations.FavoriteWithBmiData
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteBmiRecordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorite: FavoriteBmiRecordEntity): Long

    @Update
    suspend fun update(favorite: FavoriteBmiRecordEntity)

    @Query("UPDATE favorite_bmi_records SET customNote = :customNote WHERE id = :id")
    suspend fun updateCustomNote(id: Long, customNote: String) : Int

    @Query("DELETE FROM favorite_bmi_records WHERE id = :id")
    suspend fun deleteById(id: Long) : Int

    @Query("DELETE FROM favorite_bmi_records WHERE bmiRecordId = :bmiRecordId")
    suspend fun deleteByBmiRecordId(bmiRecordId: Long) : Int

    @Query("SELECT * FROM favorite_bmi_records WHERE id = :id")
    fun getById(id: Long): Flow<FavoriteBmiRecordEntity?>

    @Query("SELECT * FROM favorite_bmi_records WHERE bmiRecordId = :bmiRecordId")
    fun getByBmiRecordId(bmiRecordId: Long): Flow<FavoriteBmiRecordEntity?>

    @Transaction
    @Query("""
        SELECT f.*,
               b.id AS b_id,
               b.bmi AS b_bmi,
               b.height AS b_height,
               b.weight AS b_weight,
               b.date AS b_date,
               b.is_favored AS b_is_favored
        FROM favorite_bmi_records f
        JOIN bmi_records b ON f.bmiRecordId = b.id
        WHERE f.id = :id
    """)
    fun getFavoriteWithBmiById(id: Long): Flow<FavoriteWithBmiData?>

    @Query("SELECT * FROM favorite_bmi_records ORDER BY "+
            "dateFavored DESC")
    fun getAllFavorites(): Flow<List<FavoriteBmiRecordEntity>>

    @Query("SELECT COUNT(*) FROM favorite_bmi_records")
    suspend fun getFavoriteCount(): Int

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_bmi_records WHERE bmiRecordId = :bmiRecordId LIMIT 1)")
    suspend fun isFavorite(bmiRecordId: Long): Boolean

    @Transaction
    @Query("""
        SELECT f.*,
               b.id AS b_id,
               b.bmi AS b_bmi,
               b.height AS b_height,
               b.weight AS b_weight,
               b.date AS b_date,
               b.is_favored AS b_is_favored
        FROM favorite_bmi_records f
        JOIN bmi_records b ON f.bmiRecordId = b.id
        WHERE f.customNote LIKE '%' || :query || '%'
        ORDER BY dateFavored DESC
        LIMIT :limit OFFSET :offset
    """)
    fun searchFavoritesByNote(query: String,offset: Int, limit: Int): Flow<List<FavoriteWithBmiData>>


    @Query("""
        SELECT f.*,
               b.id AS b_id,
               b.bmi AS b_bmi,
               b.height AS b_height,
               b.weight AS b_weight,
               b.date AS b_date,
               b.is_favored AS b_is_favored
        FROM favorite_bmi_records f
        JOIN bmi_records b ON f.bmiRecordId = b.id
        LIMIT :limit OFFSET :offset
    """)
    fun getFavorites(offset: Int, limit: Int): Flow<List<FavoriteWithBmiData>>

    @Transaction
    @Query("""
    SELECT f.*,
           b.id AS b_id,
           b.bmi AS b_bmi,
           b.height AS b_height,
           b.weight AS b_weight,
           b.date AS b_date,
           b.is_favored AS b_is_favored
    FROM favorite_bmi_records f
    JOIN bmi_records b ON f.bmiRecordId = b.id
    WHERE (:query IS NULL OR f.customNote LIKE '%' || :query || '%')
      AND (:bmiMin IS NULL OR b.bmi >= :bmiMin)
      AND (:bmiMax IS NULL OR b.bmi <= :bmiMax)
      AND (:weightMin IS NULL OR b.weight >= :weightMin)
      AND (:weightMax IS NULL OR b.weight <= :weightMax)
      AND (:heightMin IS NULL OR b.height >= :heightMin)
      AND (:heightMax IS NULL OR b.height <= :heightMax)
      AND (:dateMin IS NULL OR b.date >= :dateMin)
      AND (:dateMax IS NULL OR b.date <= :dateMax)
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
    fun searchFavoritesWithFilters(
        query: String?,
        bmiMin: Float?,
        bmiMax: Float?,
        weightMin: Float?,
        weightMax: Float?,
        heightMin: Float?,
        heightMax: Float?,
        dateMin: Long?,
        dateMax: Long?,
        sortField: String,
        sortOrder: String,
        limit: Int,
        offset: Int
    ): Flow<List<FavoriteWithBmiData>>

}
