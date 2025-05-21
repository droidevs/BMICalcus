package io.droidevs.bmicalc.ui.helper.event

sealed interface BmiRecordHistoryScreenEvent {

    data object NavigateBack : BmiRecordHistoryScreenEvent

    data object RecordDeletedSuccessfully : BmiRecordHistoryScreenEvent

    data object RecordDeleteFailed : BmiRecordHistoryScreenEvent

}