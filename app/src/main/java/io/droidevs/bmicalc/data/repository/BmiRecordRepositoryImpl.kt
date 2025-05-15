package io.droidevs.bmicalc.data.repository

import io.droidevs.bmicalc.data.db.dao.BmiDao
import io.droidevs.bmicalc.data.db.exceptions.flowRunCatchingDatabase
import io.droidevs.bmicalc.data.db.exceptions.runCatchingDatabaseResult
import io.droidevs.bmicalc.data.mapers.toDomain
import io.droidevs.bmicalc.data.mapers.toEntity
import io.droidevs.bmicalc.domain.model.BmiFilter
import io.droidevs.bmicalc.domain.model.BmiRecord
import io.droidevs.bmicalc.domain.model.TimeRange
import io.droidevs.bmicalc.domain.repository.BmiRepository
import io.droidevs.bmicalc.domain.result.mapResult
import io.droidevs.wallpaper.domain.result.Result
import io.droidevs.wallpaper.domain.result.errors.DatabaseError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus

class BmiRecordRepositoryImpl(
    private val bmiDao: BmiDao
) : BmiRepository {




    override fun getRecords(filter: BmiFilter, page: Int, pageSize: Int): Flow<Result<List<BmiRecord>, DatabaseError>> {
        // Calculate time range
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
            TimeRange.All -> Pair<Instant?,Instant?>(null,null)
            else -> Pair<Instant?,Instant?>(null,null)
        }
        return flowRunCatchingDatabase {
            bmiDao.getFilteredRecords(
                startTime = startTime?.toEpochMilliseconds(),
                endTime = endTime?.toEpochMilliseconds(),
                minBmi = filter.bmi?.start,
                maxBmi = filter.bmi?.end,
                minWeight = filter.weight?.start,
                maxWeight = filter.weight?.end,
                minHeight = filter.height?.start,
                maxHeight = filter.height?.end,
                offset = pageSize * (page - 1),
                limit = pageSize
            )
        }.mapResult { records ->
            records.map {
                it.toDomain()
            }
        }
    }

    override suspend fun getRecord(id: Long): Flow<Result<BmiRecord, DatabaseError>> {
        return flowRunCatchingDatabase {
            bmiDao.get(id)
        }.mapResult { records ->
            records.map {
                it.toDomain()
            }
        }
    }

    override suspend fun getRecordPage(page: Int, pageSize: Int): Flow<Result<List<BmiRecord>, DatabaseError>> {
        return flowRunCatchingDatabase {
            bmiDao.getRecordsPage(offset = pageSize*page, limit = pageSize)
        }.mapResult { records ->
            records.map { it.toDomain() }
        }
    }

    override fun allRecords(): Flow<Result<List<BmiRecord>, DatabaseError>> {
        return flowRunCatchingDatabase {
            bmiDao.getAllRecords()
        }.mapResult { records ->
            records.map { it.toDomain() }
        }
    }

    override fun averageBmi(): Flow<Result<Float, DatabaseError>> {
        return flowRunCatchingDatabase {
            bmiDao.getAverageBmi()
        }
    }

    override suspend fun addRecord(record: BmiRecord) : Result<Long, DatabaseError> =
        withContext(Dispatchers.IO){
            runCatchingDatabaseResult {
                bmiDao.insert(record.toEntity())
            }
        }

    override suspend fun updateRecord(record: BmiRecord) =
        withContext(Dispatchers.IO){
            runCatchingDatabaseResult {
                bmiDao.update(record.toEntity())
            }
        }

    override suspend fun deleteRecord(id: Long): Result<Int, DatabaseError> {
        return withContext(Dispatchers.IO){
            runCatchingDatabaseResult {
                bmiDao.delete(id)
            }
        }
    }

    override suspend fun deleteRecords(ids: List<Long>): Result<Int, DatabaseError> {
        return withContext(Dispatchers.IO){
            runCatchingDatabaseResult {
                bmiDao.delete(ids)
            }
        }
    }

    override fun getRecordsBetween(start: Long, end: Long): Flow<Result<List<BmiRecord>, DatabaseError>> {
        return flowRunCatchingDatabase {
            bmiDao.getRecordsBetweenDates(start = start,end = end)
        }.mapResult { records ->
            records.map { it.toDomain() }
        }
    }
}