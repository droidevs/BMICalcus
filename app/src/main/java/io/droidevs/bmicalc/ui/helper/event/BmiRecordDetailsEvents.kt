package io.droidevs.bmicalc.ui.helper.event

import io.droidevs.bmicalc.domain.model.BmiRecord

open class BmiRecordDetailsEvent {

    data class NavigateToEditPage(val bmiRecord : BmiRecord) : BmiRecordDetailsEvent()

    object  FavoredSuccessfully : BmiRecordDetailsEvent()

    object UnfavoredSuccessfully : BmiRecordDetailsEvent()

    object FailedToFavorite : BmiRecordDetailsEvent()

    object FailedToUnfavored : BmiRecordDetailsEvent()

    object DeletedSuccessfully : BmiRecordDetailsEvent()

    object FailedToDelete : BmiRecordDetailsEvent()
}