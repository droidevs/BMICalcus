package io.droidevs.bmicalc.domain.usecases

import io.droidevs.bmicalc.data.repository.IBmiRepository
import io.droidevs.bmicalc.domain.BmiRecord
import io.droidevs.bmicalc.domain.toEntity

class AddBmiRecordUseCase(
    private val bmiRepository: IBmiRepository
) {

    suspend operator fun invoke(record: BmiRecord){
        bmiRepository.addRecord(
            record = record.toEntity()
        )
    }
}