package io.droidevs.bmicalc.ui.helper.actions

open class BmiRecordDetailsAction {

    data object RefreshRecord : BmiRecordDetailsAction()

    object DeleteAction : BmiRecordDetailsAction()

    object EditAction : BmiRecordDetailsAction()

    data class FavoriteAction(val note : String = "") : BmiRecordDetailsAction()

    object UnfavoriteAction : BmiRecordDetailsAction()

}