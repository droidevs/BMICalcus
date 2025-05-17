package io.droidevs.bmicalc.ui.viewmodels

import androidx.lifecycle.ViewModel
import io.droidevs.bmicalc.domain.usecases.bmi.DeleteBmiRecordUseCase
import io.droidevs.bmicalc.domain.usecases.bmi.GetBmiRecordByIdUseCase
import io.droidevs.bmicalc.domain.usecases.bmi.favorite.AddToFavoritesUseCase
import io.droidevs.bmicalc.domain.usecases.bmi.favorite.DeleteFavoredBmiRecordUseCase
import io.droidevs.bmicalc.domain.usecases.unitsystem.GetUnitSystemUseCase
import io.droidevs.bmicalc.ui.helper.actions.BmiRecordDetailsAction

class BmiRecordDetailsViewModel(
    val getUnitSystem: GetUnitSystemUseCase,
    val getBmiRecord: GetBmiRecordByIdUseCase,
    val deleteBmiRecord: DeleteBmiRecordUseCase,
    val addToFavorites: AddToFavoritesUseCase,
    val deleteFromFavorite: DeleteFavoredBmiRecordUseCase
) : ViewModel() {


    fun onAction(action : BmiRecordDetailsAction){
        when(action){
            BmiRecordDetailsAction.EditAction -> {

            }
            BmiRecordDetailsAction.DeleteAction -> {

            }
            BmiRecordDetailsAction.FavoriteAction -> {

            }
            BmiRecordDetailsAction.UnfavoriteAction -> {

            }
            BmiRecordDetailsAction.UndoDeleteAction -> {

            }
        }
    }

}