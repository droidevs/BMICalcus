package io.droidevs.bmicalc.ui.helper.states

import androidx.compose.runtime.Stable
import io.droidevs.bmicalc.data.model.UnitSystem
import io.droidevs.bmicalc.domain.model.BmiInputValidationResult
import io.droidevs.bmicalc.ui.helper.UiState
import io.droidevs.bmicalc.ui.model.BmiRecordUi
import io.droidevs.bmicalc.domain.result.errors.Error


@Stable
data class BmiRecordEditState(
    val unitSystem: UnitSystem = UnitSystem.METRIC,
    val original : BmiRecordUi = BmiRecordUi(),
    val edited: BmiRecordUi = BmiRecordUi(),
    val validationResult : BmiInputValidationResult = BmiInputValidationResult(),
    val error: Error? = null
) : UiState