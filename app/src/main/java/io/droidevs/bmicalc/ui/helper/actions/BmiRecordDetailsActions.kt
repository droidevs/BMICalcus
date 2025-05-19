package io.droidevs.bmicalc.ui.helper.actions

import io.droidevs.bmicalc.ui.helper.UiAction

open class BmiRecordDetailsAction : UiAction {

    data object RefreshRecord : BmiRecordDetailsAction()

    object DeleteAction : BmiRecordDetailsAction()

    object EditAction : BmiRecordDetailsAction()

    data class FavoriteAction(val note : String = "") : BmiRecordDetailsAction()

    object UnfavoriteAction : BmiRecordDetailsAction()

}