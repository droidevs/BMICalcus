package io.droidevs.bmicalc.domain.usecases.bmi

import io.droidevs.bmicalc.domain.repository.BmiRepository
import io.droidevs.bmicalc.domain.model.BmiRecord
import io.droidevs.wallpaper.domain.result.Result
import io.droidevs.bmicalc.domain.result.errors.DatabaseError

class UpdateBmiRecordUseCase(
    private val  bmiRepository: BmiRepository
) {

    suspend operator fun invoke(bmiRecord: BmiRecord) : Result<Int, DatabaseError> {
        return bmiRepository.updateRecord(bmiRecord)
    }
}