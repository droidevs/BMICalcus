package io.droidevs.bmicalc.domain.repository

import io.droidevs.bmicalc.data.db.entities.FavoriteBmiRecordEntity
import io.droidevs.bmicalc.domain.FavoredFilter
import io.droidevs.bmicalc.domain.model.BmiRecord
import io.droidevs.bmicalc.domain.model.FavoredBmiRecord
import io.droidevs.bmicalc.domain.result.errors.DatabaseError
import kotlinx.coroutines.flow.Flow
import io.droidevs.wallpaper.domain.result.Result
interface FavoriteBmiRepository {
    suspend fun addToFavorites(bmiRecordId: Long, note: String = ""): Result<Long, DatabaseError>
    suspend fun removeFromFavorites(id: Long): Result<Int, DatabaseError>
    suspend fun removeFromFavoritesByBmiRecordId(bmiRecordId: Long): Result<Int, DatabaseError>
    suspend fun updateCustomNote(favoredRecordId: Long, note: String): Result<Int, DatabaseError>

    suspend fun getFavoriteById(id: Long): Flow<Result<BmiRecord, DatabaseError>>
    suspend fun getFavoriteByBmiRecordId(bmiRecordId: Long): Flow<FavoriteBmiRecordEntity?>
    fun getAllFavorites(): Flow<List<FavoriteBmiRecordEntity>>
    suspend fun isFavorite(bmiRecordId: Long): Boolean
    suspend fun getFavoriteCount(): Int


    fun getFavorites(page: Int, pageSize: Int): Flow<Result<List<FavoredBmiRecord>, DatabaseError>>
    fun searchFavoritesByNote(query: String, page: Int, pageSize: Int) : Flow<Result<List<FavoredBmiRecord>, DatabaseError>>

    fun getFavoredRecords(
        filter: FavoredFilter,
        page: Int,
        pageSize: Int
    ): Flow<Result<List<FavoredBmiRecord>, DatabaseError>>

}