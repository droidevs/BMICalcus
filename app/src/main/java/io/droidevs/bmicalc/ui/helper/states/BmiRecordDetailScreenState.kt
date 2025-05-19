package io.droidevs.bmicalc.ui.helper.states

import androidx.compose.runtime.Stable
import io.droidevs.bmicalc.data.model.UnitSystem
import io.droidevs.bmicalc.ui.helper.UiState
import io.droidevs.bmicalc.ui.model.BmiRecordUi
import io.droidevs.wallpaper.domain.result.errors.Error

@Stable
data class BmiRecordDetailScreenState(
    val unitSystem: UnitSystem = UnitSystem.METRIC,
    val record: BmiRecordUi = BmiRecordUi(),
    val error: Error? = null
) : UiState