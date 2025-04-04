package io.droidevs.bmicalc.domain.usecases

import io.droidevs.bmicalc.data.repository.IBmiRepository
import io.droidevs.bmicalc.domain.BmiRecord

class DeleteBmiRecordUseCase(
    private val bmiRepository: IBmiRepository,
) {

    suspend operator fun invoke(record: BmiRecord){
        bmiRepository.deleteRecord(record.id)
    }

    suspend operator fun invoke(recordId: Long){
        bmiRepository.deleteRecord(recordId)
    }

    suspend operator fun invoke(records: List<BmiRecord>){
        records.map {
            it.id
        }.let {
            bmiRepository.deleteRecords(it)
        }
    }

    suspend operator fun invoke(recordsId: List<Long>){
        bmiRepository.deleteRecords(recordsId)
    }
}