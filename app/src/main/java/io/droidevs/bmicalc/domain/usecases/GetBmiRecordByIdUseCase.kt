package io.droidevs.bmicalc.domain.usecases

import io.droidevs.bmicalc.data.db.IBmiRepository
import io.droidevs.bmicalc.domain.BmiRecord
import io.droidevs.bmicalc.domain.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class GetBmiRecordByIdUseCase(
    private val bmiRepository: IBmiRepository
) {

    suspend operator fun invoke(id: Long) : Flow<BmiRecord> {
        return bmiRepository.getRecord(id).map {
            it.toDomain()
        }
    }
}