package io.droidevs.bmicalc.domain.model

import io.droidevs.bmicalc.data.db.entities.BmiRecordEntity
import io.droidevs.bmicalc.data.db.relations.BmiRecordWithFavorite
import kotlinx.datetime.Instant

data class BmiRecord(
    val id: Long,
    val bmi: Float,
    val height: Float,
    val weight: Float,
    val date: Instant,
    val isFavorite: Boolean = false
)
