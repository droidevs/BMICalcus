package io.droidevs.bmicalc.ui.helper.actions

import io.droidevs.bmicalc.domain.model.BmiRecord
import io.droidevs.bmicalc.ui.helper.UiAction
import io.droidevs.bmicalc.ui.model.BmiRecordUi

sealed interface BmiRecordEditAction : UiAction {


    data object ReloadData : BmiRecordEditAction

    data class ChangeRecordData(
        val record: BmiRecordUi
    ) : BmiRecordEditAction

    data class ChangeRecordHeight(
        val height: Float
    ) : BmiRecordEditAction

    data class ChangeRecordWeight(
        val weight: Float
    ) : BmiRecordEditAction

    data object RestoreData : BmiRecordEditAction

    data object CancelEdit : BmiRecordEditAction

    data object SaveRecord : BmiRecordEditAction

}