package io.droidevs.bmicalc.domain.usecases.bmi

import io.droidevs.bmicalc.domain.repository.BmiRepository
import io.droidevs.bmicalc.domain.model.BmiRecord
import io.droidevs.wallpaper.domain.result.Result
import io.droidevs.bmicalc.domain.result.errors.DatabaseError
import kotlinx.coroutines.flow.Flow


class GetBmiRecordByIdUseCase(
    private val bmiRepository: BmiRepository
) {

    suspend operator fun invoke(id: Long) : Flow<Result<BmiRecord, DatabaseError>> {
        return bmiRepository.getRecord(id)
    }
}