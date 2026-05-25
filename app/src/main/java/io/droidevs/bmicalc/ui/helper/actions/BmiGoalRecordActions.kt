package io.droidevs.bmicalc.ui.helper.actions

import io.droidevs.bmicalc.domain.GoalFilter
import io.droidevs.bmicalc.ui.helper.UiAction
import io.droidevs.bmicalc.ui.model.BmiRecordUi

sealed class BmiGoalRecordAction : UiAction {

    object LoadMore : BmiGoalRecordAction()

    object Refresh : BmiGoalRecordAction()

    data class Select(val id : Long) : BmiGoalRecordAction()

    data class MultiSelect(val ids: Long) : BmiGoalRecordAction()

    data class Deselect(val id : Long) : BmiGoalRecordAction()

    object DeselectAll : BmiGoalRecordAction()

    data class Delete(val record: BmiRecordUi) : BmiGoalRecordAction()

    data class ChangeFilter(val goalFilter : GoalFilter?) : BmiGoalRecordAction()

}