package io.droidevs.bmicalc.data.repository

import io.droidevs.bmicalc.data.db.dao.FavoriteBmiRecordDao
import io.droidevs.bmicalc.data.db.entities.FavoriteBmiRecordEntity
import io.droidevs.bmicalc.data.db.exceptions.flowRunCatchingDatabase
import io.droidevs.bmicalc.data.db.exceptions.runCatchingDatabaseResult
import io.droidevs.bmicalc.data.db.relations.FavoriteWithBmiData
import io.droidevs.bmicalc.data.mapers.toDomain
import io.droidevs.bmicalc.domain.FavoredFilter
import io.droidevs.bmicalc.domain.model.BmiRecord
import io.droidevs.bmicalc.domain.model.FavoredBmiRecord
import io.droidevs.bmicalc.domain.model.TimeRange
import io.droidevs.bmicalc.domain.repository.FavoriteBmiRepository
import io.droidevs.bmicalc.domain.result.mapResult
import io.droidevs.wallpaper.domain.result.errors.DatabaseError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import io.droidevs.wallpaper.domain.result.Result
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class FavoriteBmiRepositoryImpl(
    private val favoriteDao: FavoriteBmiRecordDao,
) : FavoriteBmiRepository {

    override suspend fun addToFavorites(bmiRecordId: Long, note: String): Result<Long, DatabaseError> {
        val favorite = FavoriteBmiRecordEntity(
            bmiRecordId = bmiRecordId,
            customNote = note,
            addedAt = Clock.System.now().toEpochMilliseconds(),
        )
        return runCatchingDatabaseResult {
            favoriteDao.insert(favorite)
        }
    }

    override suspend fun removeFromFavorites(id: Long): Result<Int, DatabaseError> {
        return withContext(Dispatchers.IO){
            runCatchingDatabaseResult {
                favoriteDao.deleteById(id)
            }
        }
    }

    override suspend fun removeFromFavoritesByBmiRecordId(bmiRecordId: Long): Result<Int, DatabaseError> {
        return withContext( Dispatchers.IO){
            runCatchingDatabaseResult {
                favoriteDao.deleteByBmiRecordId(bmiRecordId)
            }
        }
    }

    override suspend fun updateCustomNote(
        favoredRecordId: Long,
        note: String
    ): Result<Int, DatabaseError> {
        return withContext(Dispatchers.IO){
            runCatchingDatabaseResult {
                favoriteDao.updateCustomNote(favoredRecordId, note)
            }
        }
    }

    override suspend fun getFavoriteById(id: Long): Flow<Result<BmiRecord, DatabaseError>> {
        return TODO()
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

    override fun getFavorites(page: Int, pageSize: Int): Flow<Result<List<FavoredBmiRecord>, DatabaseError>> {
        return flowRunCatchingDatabase {
            favoriteDao.getFavorites(offset = pageSize * (page - 1), limit = pageSize)
                .map { records ->
                    records.map {
                        it.toDomain()
                    }
                }
                .flowOn(Dispatchers.IO)
        }
    }

    override fun searchFavoritesByNote(
        query: String,
        page: Int,
        pageSize: Int
    ): Flow<Result<List<FavoredBmiRecord>, DatabaseError>> {
        return flowRunCatchingDatabase {
            favoriteDao.searchFavoritesByNote(query = query, offset = pageSize * (page - 1), limit = pageSize)
                .map { records ->
                    records.map {
                        it.toDomain()
                    }
                }
                .flowOn(Dispatchers.IO)
        }
    }

    override fun getFavoredRecords(
        filter: FavoredFilter,
        page: Int,
        pageSize: Int
    ): Flow<Result<List<FavoredBmiRecord>, DatabaseError>> {
        val timezone = TimeZone.currentSystemDefault()
        val timeRange = filter.time
        val (startTime, endTime) = when (timeRange) {
            TimeRange.Week -> {
                val now = Clock.System.now()
                Pair(now.minus(7, DateTimeUnit.DAY, timezone), now)
            }

            TimeRange.Month -> {
                val now = Clock.System.now()
                Pair(now.minus(30, DateTimeUnit.DAY, timezone), now)
            }

            TimeRange.Year -> {
                val now = Clock.System.now()
                Pair(now.minus(365, DateTimeUnit.DAY, timezone), now)
            }

            TimeRange.Custom() -> {
                // Assuming you have custom start/end in your BmiFilter
                val custom = timeRange as TimeRange.Custom
                Pair(custom.start, custom.end)
            }

            TimeRange.All -> Pair<Instant?, Instant?>(null, null)
            else -> Pair<Instant?, Instant?>(null, null)
        }
        return flowRunCatchingDatabase {
            favoriteDao.searchFavoritesWithFilters(
                query = filter.query,
                dateMin = startTime?.toEpochMilliseconds(),
                dateMax = endTime?.toEpochMilliseconds(),
                bmiMin = filter.bmi?.start,
                bmiMax = filter.bmi?.end,
                weightMin = filter.weight?.start,
                weightMax = filter.weight?.end,
                heightMin = filter.height?.start,
                heightMax = filter.height?.end,
                sortField = filter.order?.orderBy?.text?: "DATE",
                sortOrder = filter.order?.orderType?.text?: "ASC",
                offset = pageSize * (page - 1),
                limit = pageSize
            )
        }.mapResult { records ->
            records.map {
                it.toDomain()
            }
        }
    }
}