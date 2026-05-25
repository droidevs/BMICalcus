package io.droidevs.bmicalc.domain.usecases

import io.droidevs.bmicalc.domain.repository.BmiRepository
import io.droidevs.bmicalc.domain.model.BmiRecord
import io.droidevs.bmicalc.domain.result.errors.DatabaseError
import io.droidevs.wallpaper.domain.result.Result

class AddBmiRecordUseCase(
    private val bmiRepository: BmiRepository
) {

    suspend operator fun invoke(record: BmiRecord): Result<Long, DatabaseError> {
        return bmiRepository.addRecord(record = record)
    }
}
