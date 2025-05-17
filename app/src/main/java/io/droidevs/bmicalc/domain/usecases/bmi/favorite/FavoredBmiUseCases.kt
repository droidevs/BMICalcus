package io.droidevs.bmicalc.domain.usecases.bmi.favorite


data class FavoredBmiUseCases(
    val get : GetFavoredBmiRecordsUseCase,
    val add : AddToFavoritesUseCase,
    val delete : DeleteFavoredBmiRecordUseCase,
    val update : UpdateFavoredRecordUseCase
)