package io.droidevs.bmicalc.domain

import io.droidevs.bmicalc.data.db.BmiRecordEntity
import kotlinx.datetime.Instant

data class BmiRecord(
    val id: Long,
    val bmi: Float,
    val height: Float,
    val weight: Float,
    val date: Instant,
)

fun BmiRecord.toEntity(): BmiRecordEntity {
    return BmiRecordEntity(
        bmi = bmi,
        height = height,
        weight = weight,
        date = date.toEpochMilliseconds(),
    )
}

fun BmiRecordEntity.toDomain(): BmiRecord {
    return BmiRecord(
        id = id,
        bmi = bmi,
        height = height,
        weight = weight,
        date = Instant.fromEpochMilliseconds(date)
    )
}

