package io.droidevs.bmicalc.data.db

import io.droidevs.bmicalc.domain.BmiFilter
import io.droidevs.bmicalc.model.TimeRange
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus

class FavoriteBmiRepository(
    private val favoriteDao: FavoriteBmiRecordDao,
) : IFavoriteBmiRepository {

    override suspend fun addToFavorites(bmiRecordId: Long, note: String?, priority: Int): Flow<Long> {
        val favorite = FavoriteBmiRecordEntity(
            bmiRecordId = bmiRecordId,
            customNote = note,
            addedAt = Clock.System.now().toEpochMilliseconds(),
        )
        return favoriteDao.insert(favorite)
    }

    override suspend fun removeFromFavorites(id: Long): Result<Unit> {
        return try {
            favoriteDao.deleteById(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun removeFromFavoritesByBmiRecordId(bmiRecordId: Long): Result<Unit> {
        return try {
            favoriteDao.deleteByBmiRecordId(bmiRecordId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateFavorite(favorite: FavoriteBmiRecordEntity): Result<Unit> {
        return try {
            favoriteDao.update(favorite)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getFavoriteById(id: Long): Flow<FavoriteBmiRecordEntity?> {
        return favoriteDao.getById(id)
    }

    override suspend fun getFavoriteByBmiRecordId(bmiRecordId: Long): Flow<FavoriteBmiRecordEntity?> {
        return favoriteDao.getByBmiRecordId(bmiRecordId)
    }

    override fun getAllFavorites(): Flow<List<FavoriteBmiRecordEntity>> {
        return favoriteDao.getAllFavorites()
    }

    override suspend fun isFavorite(bmiRecordId: Long): Boolean {
        return favoriteDao.isFavorite(bmiRecordId)
    }

    override suspend fun getFavoriteCount(): Int {
        return favoriteDao.getFavoriteCount()
    }

    override fun getFavorites(page: Int, pageSize: Int): Flow<List<FavoriteWithBmiData>> {
        return favoriteDao.getFavorites(offset = pageSize * (page - 1), limit = pageSize)
            .flowOn(Dispatchers.IO)
    }

    override fun searchFavoritesByNote(
        query: String,
        page: Int,
        pageSize: Int
    ): Flow<List<FavoriteWithBmiData>> {
        return favoriteDao.searchFavoritesByNote(query = query, offset = pageSize * (page - 1), limit = pageSize)
            .flowOn(Dispatchers.IO)
    }

    override fun searchFavoritesWithFilters(
        query: String?,
        filter: BmiFilter,
        page: Int,
        pageSize: Int
    ): Flow<List<FavoriteWithBmiData>> {
        val timezone = TimeZone.currentSystemDefault()
        val timeRange = filter.time
        val (startTime, endTime) = when (timeRange) {
            TimeRange.Week -> {
                val now = Clock.System.now()
                Pair(now.minus(7, DateTimeUnit.DAY, timezone), now)
            }
            TimeRange.Month -> {
                val now = Clock.System.now()
                Pair(now.minus(30, DateTimeUnit.DAY,timezone), now)
            }
            TimeRange.Year -> {
                val now = Clock.System.now()
                Pair(now.minus(365, DateTimeUnit.DAY,timezone), now)
            }
            TimeRange.Custom() -> {
                // Assuming you have custom start/end in your BmiFilter
                val custom = timeRange as TimeRange.Custom
                Pair(custom.start, custom.end)
            }
            TimeRange.All -> Pair<Instant?, Instant?>(null,null)
            else -> Pair<Instant?, Instant?>(null,null)
        }
        return favoriteDao.searchFavoritesWithFilters(
            query = query,
            bmiMin = filter.bmi?.start,
            bmiMax = filter.bmi?.end,
            weightMin = filter.weight?.start,
            weightMax = filter.weight?.end,
            heightMin = filter.height?.start,
            heightMax = filter.height?.end,
            dateMin = startTime?.toEpochMilliseconds(),
            dateMax = endTime?.toEpochMilliseconds(),
            sortField = filter.order?.orderBy?.text?: "",
            sortOrder = filter.order?.orderType?.text?: "",
            limit = pageSize,
            offset = page * pageSize
        )
    }
}