package io.droidevs.bmicalc.domain.model

data class DataPoint(
    val timestamp: Long,
    val value: Float,
    val label: String = ""
)