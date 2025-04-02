package io.droidevs.bmicalc.data.db

import io.droidevs.bmicalc.domain.BmiFilter
import kotlinx.coroutines.flow.Flow

interface IFavoriteBmiRepository {
    suspend fun addToFavorites(bmiRecordId: Long, note: String?, priority: Int): Flow<Long>
    suspend fun removeFromFavorites(id: Long): Result<Unit>
    suspend fun removeFromFavoritesByBmiRecordId(bmiRecordId: Long): Result<Unit>
    suspend fun updateFavorite(favorite: FavoriteBmiRecordEntity): Result<Unit>
    suspend fun getFavoriteById(id: Long): Flow<FavoriteBmiRecordEntity?>
    suspend fun getFavoriteByBmiRecordId(bmiRecordId: Long): Flow<FavoriteBmiRecordEntity?>
    fun getAllFavorites(): Flow<List<FavoriteBmiRecordEntity>>
    suspend fun isFavorite(bmiRecordId: Long): Boolean
    suspend fun getFavoriteCount(): Int


    fun getFavorites(page: Int, pageSize: Int): Flow<List<FavoriteWithBmiData>>
    fun searchFavoritesByNote(query: String, page: Int, pageSize: Int) : Flow<List<FavoriteWithBmiData>>

    fun searchFavoritesWithFilters(
        query: String?,
        filter: BmiFilter,
        page: Int,
        pageSize: Int
    ): Flow<List<FavoriteWithBmiData>>
}