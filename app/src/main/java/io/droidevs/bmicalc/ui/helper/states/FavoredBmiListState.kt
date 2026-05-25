package io.droidevs.bmicalc.ui.helper.states

import androidx.compose.runtime.Stable
import io.droidevs.bmicalc.data.model.UnitSystem
import io.droidevs.bmicalc.domain.result.errors.Error
import io.droidevs.bmicalc.ui.model.FavoredBmiUi
import io.droidevs.bmicalc.ui.model.LoadingMode


@Stable
data class FavoredBmiListState(
    val mode : LoadingMode = LoadingMode.Ide,
    val records : List<FavoredBmiUi> = emptyList(),
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
