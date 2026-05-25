package io.droidevs.bmicalc.domain.usecases.bmi

import io.droidevs.bmicalc.domain.repository.BmiRepository
import io.droidevs.bmicalc.domain.model.BmiRecord
import io.droidevs.wallpaper.domain.result.Result
import io.droidevs.bmicalc.domain.result.errors.DatabaseError
import kotlin.jvm.JvmName

class DeleteBmiRecordUseCase(
    private val bmiRepository: BmiRepository,
) {

    suspend operator fun invoke(record: BmiRecord) : Result<Int, DatabaseError>{
        return bmiRepository.deleteRecord(record.id)
    }

    suspend operator fun invoke(recordId: Long) : Result<Int, DatabaseError>{
        return bmiRepository.deleteRecord(recordId)
    }

    suspend operator fun invoke(records: List<BmiRecord>) : Result<Int, DatabaseError>{
        return records.map {
            it.id
        }.let {
            bmiRepository.deleteRecords(it)
        }
    }

    @JvmName("invokeByIds")
    suspend operator fun invoke(recordsId: List<Long>) : Result<Int, DatabaseError>{
        return bmiRepository.deleteRecords(recordsId)
    }
}
