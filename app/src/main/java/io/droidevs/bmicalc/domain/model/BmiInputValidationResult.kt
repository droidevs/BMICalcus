package io.droidevs.bmicalc.domain.model

data class BmiInputValidationResult (
    val heightError: ValidationError? = null,
    val weightError: ValidationError? = null
) {
    val isHeightValid: Boolean = heightError == null
    val isWeightValid: Boolean = weightError == null
}