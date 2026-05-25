package io.droidevs.bmicalc.ui.helper.actions

import io.droidevs.bmicalc.ui.helper.UiAction
import io.droidevs.bmicalc.ui.model.BmiRecordUi

sealed class FavoredBmiRecordListActions : UiAction {
    object LoadMore : FavoredBmiRecordListActions()

    object Refresh : FavoredBmiRecordListActions()

    data class Select(val id : Long) : FavoredBmiRecordListActions()

    data class MultiSelect(val ids: Long) : FavoredBmiRecordListActions()

    data class Deselect(val id : Long) : FavoredBmiRecordListActions()

    object DeselectAll : FavoredBmiRecordListActions()

    data class Delete(val record: BmiRecordUi) : FavoredBmiRecordListActions()
}