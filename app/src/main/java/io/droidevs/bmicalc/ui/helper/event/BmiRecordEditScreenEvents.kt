package io.droidevs.bmicalc.ui.helper.event

import io.droidevs.bmicalc.ui.helper.UiEvent

sealed interface BmiRecordEditScreenEvent : UiEvent {

    object SavedSuccessfully : BmiRecordEditScreenEvent

    object DataRestoredSuccessfully: BmiRecordEditScreenEvent

    object DataRestoreFailed: BmiRecordEditScreenEvent

    object SaveFailed : BmiRecordEditScreenEvent

    object NavigateBack : BmiRecordEditScreenEvent

}