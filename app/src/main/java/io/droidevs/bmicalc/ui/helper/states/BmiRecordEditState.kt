package io.droidevs.bmicalc.ui.helper.states

import androidx.compose.runtime.Stable
import androidx.room.RoomOpenHelper
import io.droidevs.bmicalc.data.model.UnitSystem
import io.droidevs.bmicalc.domain.model.BMICategory
import io.droidevs.bmicalc.domain.model.BmiInputValidationResult
import io.droidevs.bmicalc.ui.model.BmiRecordUi
import io.droidevs.wallpaper.domain.result.errors.Error


@Stable
data class BmiRecordEditState(
    val unitSystem: UnitSystem = UnitSystem.METRIC,
    val original : BmiRecordUi = BmiRecordUi(),
    val edited: BmiRecordUi = BmiRecordUi(),
    val validationResult : BmiInputValidationResult = BmiInputValidationResult(),
    val error: Error? = null
)