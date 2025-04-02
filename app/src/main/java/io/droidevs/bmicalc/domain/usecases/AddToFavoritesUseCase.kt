package io.droidevs.bmicalc.domain.usecases

import io.droidevs.bmicalc.data.db.IFavoriteBmiRepository

class AddToFavoritesUseCase(
    val repository: IFavoriteBmiRepository
) {


    suspend fun invoke(bmiRecordId: Long, note : String?, priority: Int){
        repository.addToFavorites(bmiRecordId = bmiRecordId, note = note, priority = priority)
    }
}