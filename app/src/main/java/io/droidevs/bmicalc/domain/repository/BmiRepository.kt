package io.droidevs.bmicalc.domain.repository

import io.droidevs.bmicalc.domain.model.BmiFilter
import io.droidevs.bmicalc.domain.model.BmiRecord
import io.droidevs.wallpaper.domain.result.Result
import io.droidevs.wallpaper.domain.result.errors.DatabaseError
import kotlinx.coroutines.flow.Flow


interface BmiRepository {

    suspend fun getRecord(id: Long): Flow<Result<BmiRecord, DatabaseError>>


    suspend fun getRecordPage(page: Int, pageSize: Int): Flow<Result<List<BmiRecord>, DatabaseError>>

    fun getRecords(filter: BmiFilter, page: Int, pageSize: Int): Flow<Result<List<BmiRecord>, DatabaseError>>

    fun allRecords() : Flow<Result<List<BmiRecord>, DatabaseError>>

    fun averageBmi() : Flow<Result<Float, DatabaseError>>

    suspend fun addRecord(record: BmiRecord) : Result<Long, DatabaseError>

    suspend fun updateRecord(record: BmiRecord) : Result<Int, DatabaseError>

    suspend fun deleteRecord(id: Long) : Result<Int, DatabaseError>

    suspend fun deleteRecords(ids: List<Long>) : Result<Int, DatabaseError>

    fun getRecordsBetween(start: Long, end: Long) : Flow<Result<List<BmiRecord>, DatabaseError>>
}