package io.droidevs.bmicalc.ui.helper.states

import androidx.compose.runtime.Stable
import io.droidevs.bmicalc.data.model.UnitSystem
import io.droidevs.bmicalc.ui.model.BmiRecordUi

@Stable
data class BmiRecordDetailScreenState(
    val unitSystem: UnitSystem,
    val record: BmiRecordUi,
)