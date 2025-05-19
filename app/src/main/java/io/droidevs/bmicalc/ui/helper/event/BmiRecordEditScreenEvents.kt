package io.droidevs.bmicalc.ui.helper.event

sealed interface BmiRecordEditScreenEvent {

    object SavedSuccessfully : BmiRecordEditScreenEvent

    object DataRestoredSuccessfully: BmiRecordEditScreenEvent

    object DataRestoreFailed: BmiRecordEditScreenEvent

    object SaveFailed : BmiRecordEditScreenEvent

    object NavigateBack : BmiRecordEditScreenEvent

}