package io.droidevs.bmicalc.domain.usecases.bmi

import io.droidevs.bmicalc.domain.repository.BmiRepository
import io.droidevs.bmicalc.domain.model.BmiRecord

class DeleteBmiRecordUseCase(
    private val bmiRepository: BmiRepository,
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