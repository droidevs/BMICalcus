package io.droidevs.bmicalc.ui.helper.actions

import io.droidevs.bmicalc.domain.model.BmiFilter
import io.droidevs.bmicalc.ui.helper.UiAction
import io.droidevs.bmicalc.ui.model.BmiRecordUi

sealed class HistoryScreenAction : UiAction {


    data class ChangeFilter(val bmiFilter: BmiFilter) : HistoryScreenAction()
    object LoadMore : HistoryScreenAction()

    object Refresh : HistoryScreenAction()

    data class Select(val id : Long) : HistoryScreenAction()

    data class MultiSelect(val ids: Long) : HistoryScreenAction()

    data class Deselect(val id : Long) : HistoryScreenAction()

    object DeselectAll : HistoryScreenAction()

    data class Delete(val record: BmiRecordUi) : HistoryScreenAction()

    data class Favorite(val record: BmiRecordUi) : HistoryScreenAction()
}