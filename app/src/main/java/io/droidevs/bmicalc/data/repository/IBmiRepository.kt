package io.droidevs.bmicalc.data.repository

import io.droidevs.bmicalc.data.db.entities.BmiRecordEntity
import io.droidevs.bmicalc.data.db.relations.BmiRecordWithFavorite
import io.droidevs.bmicalc.domain.BmiFilter
import kotlinx.coroutines.flow.Flow


interface IBmiRepository {

    suspend fun getRecord(id: Long): Flow<BmiRecordEntity>


    suspend fun getRecordPage(page: Int, pageSize: Int): Flow<List<BmiRecordEntity>>

    fun getFilteredRecords(filter: BmiFilter, page: Int, pageSize: Int): Flow<List<BmiRecordEntity>>

    fun getRecordsWithFavorite(filter: BmiFilter, page: Int, pageSize: Int) : Flow<List<BmiRecordWithFavorite>>

    fun allRecords() : Flow<List<BmiRecordEntity>>

    fun averageBmi() : Flow<Float?>

    suspend fun addRecord(record: BmiRecordEntity)

    suspend fun updateRecord(record: BmiRecordEntity)

    suspend fun deleteRecord(id: Long)

    suspend fun deleteRecords(ids: List<Long>)

    fun getRecordsBetween(start: Long, end: Long) : Flow<List<BmiRecordEntity>>
}