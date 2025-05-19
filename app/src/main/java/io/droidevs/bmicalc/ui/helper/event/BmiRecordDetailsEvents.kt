package io.droidevs.bmicalc.ui.helper.event

import io.droidevs.bmicalc.domain.model.BmiRecord
import io.droidevs.bmicalc.ui.helper.UiEvent

open class BmiRecordDetailsEvent : UiEvent {

    data class NavigateToEditPage(val recordId : Long) : BmiRecordDetailsEvent()

    object  FavoredSuccessfully : BmiRecordDetailsEvent()

    object UnfavoredSuccessfully : BmiRecordDetailsEvent()

    object FailedToFavorite : BmiRecordDetailsEvent()

    object FailedToUnfavored : BmiRecordDetailsEvent()

    object DeletedSuccessfully : BmiRecordDetailsEvent()

    object FailedToDelete : BmiRecordDetailsEvent()

}