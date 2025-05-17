package io.droidevs.bmicalc.domain.usecases.bmi.favorite

import io.droidevs.bmicalc.domain.repository.FavoriteBmiRepository
import io.droidevs.wallpaper.domain.result.Result
import io.droidevs.wallpaper.domain.result.errors.DatabaseError

class DeleteFavoredBmiRecordUseCase(
    private val repository : FavoriteBmiRepository
) {

    suspend operator fun invoke(favoredRecordId: Long) : Result<Int,DatabaseError>{
        return repository.removeFromFavorites(favoredRecordId)
    }
}