package io.droidevs.bmicalc.ui.helper.event

sealed interface BmiRecordHistoryScreenEvent {

    data object NavigateBack : BmiRecordHistoryScreenEvent

    data object NavigateToFavorites : BmiRecordHistoryScreenEvent

    data class NavigateToRecordDetails(val recordId: Long) : BmiRecordHistoryScreenEvent

    data object RecordDeletedSuccessfully : BmiRecordHistoryScreenEvent

    data object RecordDeleteFailed : BmiRecordHistoryScreenEvent

    data object RecordFavoredSuccessfully : BmiRecordHistoryScreenEvent
    data object RecordUnfavoredSuccessfully : BmiRecordHistoryScreenEvent
    data object RecordFavoriteFailed : BmiRecordHistoryScreenEvent
    data object RecordUnfavoriteFailed : BmiRecordHistoryScreenEvent

}
