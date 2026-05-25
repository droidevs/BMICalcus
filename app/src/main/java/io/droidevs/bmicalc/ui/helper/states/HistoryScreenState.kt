package io.droidevs.bmicalc.ui.helper.states


import io.droidevs.bmicalc.data.model.UnitSystem
import io.droidevs.bmicalc.domain.model.BmiFilter
import io.droidevs.bmicalc.ui.model.BmiRecordUi
import io.droidevs.bmicalc.ui.model.LoadingMode
import io.droidevs.bmicalc.domain.result.errors.Error


data class HistoryState(
    val mode : LoadingMode = LoadingMode.Ide,
    val records : List<BmiRecordUi> = emptyList(),
    val bmiFilter : BmiFilter = BmiFilter(),
    val isSelectMode : Boolean = false,
    val error : Error? = null,
    val endReached : Boolean = false,
    val page  : Int = 0,
    val unitSystem: UnitSystem = UnitSystem.METRIC
) {
    val selectCount : Int
        get() {
            return if (isSelectMode) records.count { it.isSelected } else 0
        }
}
