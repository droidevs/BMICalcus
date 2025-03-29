package io.droidevs.bmicalc.data.db

import io.droidevs.bmicalc.domain.BmiFilter
import io.droidevs.bmicalc.domain.BmiRecord
import io.droidevs.bmicalc.domain.toDomain
import io.droidevs.bmicalc.model.TimeRange
import io.droidevs.bmicalc.ui.components.TimeRangeChip
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toJavaInstant
import java.util.Date

class BmiRepository(
    val bmiDao: BmiDao
) : IBmiRepository {

    override fun getFilteredRecords(filter: BmiFilter, page: Int, pageSize: Int): Flow<List<BmiRecordEntity>> {
        // Calculate time range
        val timezone = TimeZone.currentSystemDefault()
        val timeRange = filter.timeRange
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
            minBmi = filter.bmiRange?.start,
            maxBmi = filter.bmiRange?.end,
            minWeight = filter.weightRange?.start,
            maxWeight = filter.weightRange?.end,
            minHeight = filter.heightRange?.start,
            maxHeight = filter.heightRange?.end,
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