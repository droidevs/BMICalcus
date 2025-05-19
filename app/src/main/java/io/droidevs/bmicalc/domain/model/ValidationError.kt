package io.droidevs.bmicalc.domain.model

sealed interface ValidationError {

    data class OutOfRangeError(val min: Float, val max: Float) : ValidationError

}