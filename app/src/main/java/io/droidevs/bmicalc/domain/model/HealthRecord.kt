package io.droidevs.bmicalc.domain.model

data class HealthRecord(
    val date: Long,
    val height: Float,
    val weight: Float,
    val bmi: Float = 0f
)