package io.droidevs.bmicalc.data.repository

import io.droidevs.bmicalc.data.db.dao.BmiDao
import io.droidevs.bmicalc.data.db.entities.BmiRecordEntity
import io.droidevs.bmicalc.data.db.relations.BmiRecordWithFavorite
import io.droidevs.bmicalc.domain.BmiFilter
import io.droidevs.bmicalc.model.TimeRange
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus

class BmiRepository(
    val bmiDao: BmiDao
) : IBmiRepository {




    override fun getFilteredRecords(filter: BmiFilter, page: Int, pageSize: Int): Flow<List<BmiRecordEntity>> {
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
        return bmiDao.getFilteredRecords(
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
    }

    override fun getRecordsWithFavorite(
        filter: BmiFilter,
        page: Int,
        pageSize: Int
    ): Flow<List<BmiRecordWithFavorite>> {
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
        return bmiDao.getFilteredBmiRecordsPaged(
            timeMin = startTime?.toEpochMilliseconds(),
            timeMax = endTime?.toEpochMilliseconds(),
            bmiMin = filter.bmi?.start,
            bmiMax = filter.bmi?.end,
            weightMin = filter.weight?.start,
            weightMax = filter.weight?.end,
            heightMin = filter.height?.start,
            heightMax = filter.height?.end,
            sortField = filter.order?.orderBy?.text,
            sortOrder = filter.order?.orderType?.text,
            offset = pageSize * (page - 1),
            limit = pageSize
        )
    }

    override suspend fun getRecord(id: Long): Flow<BmiRecordEntity> {
        return bmiDao.get(id).flowOn(Dispatchers.IO)
    }

    override suspend fun getRecordPage(page: Int, pageSize: Int): Flow<List<BmiRecordEntity>> {
        return bmiDao.getRecordsPage(offset = pageSize*page, limit = pageSize)
    }

    override fun allRecords(): Flow<List<BmiRecordEntity>> {
        return bmiDao.getAllRecords().flowOn(Dispatchers.IO)
    }

    override fun averageBmi(): Flow<Float?> {
        return bmiDao.getAverageBmi().flowOn(Dispatchers.IO)
    }

    override suspend fun addRecord(record: BmiRecordEntity) {
        withContext(Dispatchers.IO){
            bmiDao.insert(record)
        }
    }

    override suspend fun updateRecord(record: BmiRecordEntity) {
        bmiDao.update(record)
    }

    override suspend fun deleteRecord(id: Long) {
        withContext(Dispatchers.IO){
            bmiDao.delete(id)
        }
    }

    override suspend fun deleteRecords(ids: List<Long>) {
        withContext(Dispatchers.IO){
            bmiDao.delete(ids)
        }
    }

    override fun getRecordsBetween(start: Long, end: Long): Flow<List<BmiRecordEntity>> {
        return bmiDao.getRecordsBetweenDates(start = start,end = end).flowOn(Dispatchers.IO)
    }
}