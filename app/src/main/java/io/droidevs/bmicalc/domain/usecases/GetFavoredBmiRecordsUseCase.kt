package io.droidevs.bmicalc.domain.usecases

import androidx.compose.animation.core.rememberTransition
import io.droidevs.bmicalc.data.db.IFavoriteBmiRepository
import io.droidevs.bmicalc.domain.BmiFilter
import io.droidevs.bmicalc.domain.FavoredBmiRecord
import io.droidevs.bmicalc.domain.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetFavoredBmiRecordsUseCase(
    private val favoriteRepository : IFavoriteBmiRepository
) {

    fun invoke(query: String?, bmiFilter: BmiFilter? , page: Int, pageSize: Int) : Flow<List<FavoredBmiRecord>>{
        if (bmiFilter != null){
            return favoriteRepository.searchFavoritesWithFilters(
                query = query,
                filter = bmiFilter,
                page = page,
                pageSize = pageSize
            ).map { bmiData ->
                bmiData.map {
                    it.toDomain()
                }
            }
        }
        else {
            if (query == null){
                return favoriteRepository.getFavorites(page = page, pageSize = pageSize)
                    .map { bmiData ->
                        bmiData.map {
                            it.toDomain()
                        }
                    }
            }
            else {
                return favoriteRepository.searchFavoritesByNote(query = query, page = page, pageSize = pageSize)
                    .map { bmiData ->
                        bmiData.map {
                            it.toDomain()
                        }
                    }
            }
        }
    }
}