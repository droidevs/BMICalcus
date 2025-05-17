package io.droidevs.bmicalc.domain.model

data class BmiRecord(
    val id: Long,
    val bmi: Float,
    val height: Float,
    val weight: Float,
    val date: Long,
    val isFavorite: Boolean = false
)
