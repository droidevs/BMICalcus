package io.droidevs.bmicalc.ui.helper.actions

open class BmiRecordDetailsAction {

    object DeleteAction : BmiRecordDetailsAction()

    object UndoDeleteAction : BmiRecordDetailsAction()

    object EditAction : BmiRecordDetailsAction()

    data class FavoriteAction(val note : String = "") : BmiRecordDetailsAction()

    object UnfavoriteAction : BmiRecordDetailsAction()

}