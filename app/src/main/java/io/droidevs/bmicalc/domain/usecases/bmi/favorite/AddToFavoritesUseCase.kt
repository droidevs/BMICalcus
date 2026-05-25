package io.droidevs.bmicalc.domain.usecases.bmi.favorite

import io.droidevs.bmicalc.domain.repository.FavoriteBmiRepository
import io.droidevs.wallpaper.domain.result.Result
import io.droidevs.bmicalc.domain.result.errors.DatabaseError

class AddToFavoritesUseCase(
    val repository: FavoriteBmiRepository
) {


    suspend operator fun invoke(bmiRecordId: Long, note : String) : Result<Long, DatabaseError>{
        return repository.addToFavorites(bmiRecordId = bmiRecordId, note = note)
    }
}