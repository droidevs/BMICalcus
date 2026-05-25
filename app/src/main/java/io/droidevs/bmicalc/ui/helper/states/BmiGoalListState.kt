package io.droidevs.bmicalc.ui.helper.states


import io.droidevs.bmicalc.domain.result.errors.Error
import io.droidevs.bmicalc.ui.model.GoalRecordUi
import io.droidevs.bmicalc.ui.model.LoadingMode

data class BmiGoalListState(
    val mode : LoadingMode = LoadingMode.Ide,
    val records : List<GoalRecordUi> = emptyList(),
    val isSelectMode : Boolean = false,
    val error : Error? = null,
    val endReached : Boolean = false,
    val page  : Int = 0,
) {
    val selectCount : Int
        get() {
            return if (isSelectMode) records.count { it.isSelected } else 0
        }
}
