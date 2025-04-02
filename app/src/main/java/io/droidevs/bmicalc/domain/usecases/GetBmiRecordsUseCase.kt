package io.droidevs.bmicalc.domain.usecases

import io.droidevs.bmicalc.data.db.BmiRepository
import io.droidevs.bmicalc.domain.BmiFilter
import io.droidevs.bmicalc.domain.BmiRecord
import io.droidevs.bmicalc.domain.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetBmiRecordsUseCase(
    private val bmiRepository: BmiRepository
) {

    suspend operator fun invoke(filter: BmiFilter?, page: Int, size: Int): Flow<List<BmiRecord>>{
        return filter?.let {
            bmiRepository.getRecordsWithFavorite(filter, page,size).map { it ->
                it.map {
                    it.toDomain()
                }
            }
        }?: bmiRepository.getRecordPage(page,size).map { records ->
            records.map {
                it.toDomain()
            }
        }
    }
}