package io.droidevs.bmicalc.domain.usecases.bmi.favorite

import io.droidevs.bmicalc.domain.repository.FavoriteBmiRepository
import io.droidevs.bmicalc.domain.FavoredFilter
import io.droidevs.bmicalc.domain.model.FavoredBmiRecord
import io.droidevs.wallpaper.domain.result.Result
import io.droidevs.wallpaper.domain.result.errors.DatabaseError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetFavoredBmiRecordsUseCase(
    private val favoriteRepository : FavoriteBmiRepository
) {

    operator fun invoke(filter: FavoredFilter?, page: Int, pageSize: Int) : Flow<Result<List<FavoredBmiRecord>, DatabaseError>>{
        return filter?.let {
            favoriteRepository.getFavoredRecords(
                filter = filter,
                page = page,
                pageSize = pageSize
            )
        }?: favoriteRepository.getFavorites(page = page, pageSize = pageSize)
    }
}