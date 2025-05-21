package io.droidevs.bmicalc.domain.usecases.validators

import io.droidevs.bmicalc.data.model.HeightUnit
import io.droidevs.bmicalc.data.model.UnitSystem
import io.droidevs.bmicalc.data.model.WeightUnit
import io.droidevs.bmicalc.domain.model.BmiInputValidationResult
import io.droidevs.bmicalc.domain.model.ValidationError

class ValidateBmiInputUseCase(){


    suspend operator fun invoke(unitSystem: UnitSystem, height: Float?, weight: Float?) : BmiInputValidationResult {
        val validation = BmiInputValidationResult()
        val weightRange = WeightUnit.getUnit(unitSystem).validRange
        val heightRange = HeightUnit.getUnit(unitSystem).validRange
        if (height != null && height !in heightRange.start..heightRange.endInclusive){
            validation.copy(heightError = ValidationError.OutOfRangeError(min = heightRange.start, max = heightRange.endInclusive))
        }
        if (weight!= null && weight !in weightRange.start..weightRange.endInclusive){
            validation.copy(weightError = ValidationError.OutOfRangeError(min =weightRange.start, max = weightRange.endInclusive))
        }
        return validation
    }
}