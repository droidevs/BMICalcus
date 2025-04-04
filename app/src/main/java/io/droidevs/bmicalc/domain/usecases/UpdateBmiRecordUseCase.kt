package io.droidevs.bmicalc.domain.usecases

import io.droidevs.bmicalc.data.repository.IBmiRepository
import io.droidevs.bmicalc.domain.BmiRecord
import io.droidevs.bmicalc.domain.toEntity

class UpdateBmiRecordUseCase(
    private val  bmiRepository: IBmiRepository
) {


    suspend fun invoke(bmiRecord: BmiRecord) {
        bmiRepository.updateRecord(bmiRecord.toEntity())
    }
}