package io.droidevs.bmicalc.domain.model

import kotlinx.datetime.Instant

data class FavoredBmiRecord(
    val id: Long,
    val recordId: Long,
    val bmi: Float,
    val height: Float,
    val weight: Float,
    val date: Long,
    val note: String? = null,
)