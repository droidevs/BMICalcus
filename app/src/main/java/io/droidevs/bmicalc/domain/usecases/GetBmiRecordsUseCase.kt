package io.droidevs.bmicalc.domain.usecases

import io.droidevs.bmicalc.domain.repository.BmiRepository
import io.droidevs.bmicalc.domain.model.BmiFilter
import io.droidevs.bmicalc.domain.model.BmiRecord
import io.droidevs.wallpaper.domain.result.Result
import io.droidevs.wallpaper.domain.result.errors.DatabaseError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetBmiRecordsUseCase(
    private val bmiRepository: BmiRepository
) {

    suspend operator fun invoke(filter: BmiFilter?, page: Int, size: Int): Flow<Result<List<BmiRecord>, DatabaseError>>{
        return filter?.let {
            bmiRepository.getRecords(filter = filter, page = page, pageSize = size)
        }?: bmiRepository.getRecordPage(page,size)
    }
}