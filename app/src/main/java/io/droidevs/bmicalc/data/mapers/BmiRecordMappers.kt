package io.droidevs.bmicalc.data.mapers

import io.droidevs.bmicalc.data.db.entities.BmiRecordEntity
import io.droidevs.bmicalc.data.db.relations.BmiRecordWithFavorite
import io.droidevs.bmicalc.data.db.relations.FavoriteWithBmiData
import io.droidevs.bmicalc.domain.model.BmiRecord
import io.droidevs.bmicalc.domain.model.FavoredBmiRecord
import kotlinx.datetime.Instant

fun BmiRecord.toEntity(): BmiRecordEntity {
    return BmiRecordEntity(
        bmi = bmi,
        height = height,
        weight = weight,
        date = date,
        isFavored = isFavorite
    )
}

fun BmiRecordEntity.toDomain(): BmiRecord {
    return BmiRecord(
        id = id,
        bmi = bmi,
        height = height,
        weight = weight,
        date = date,
        isFavorite = isFavored
    )
}

fun BmiRecordWithFavorite.toDomain(): BmiRecord {
    return BmiRecord(
        id = bmiRecord.id,
        bmi = bmiRecord.bmi,
        height = bmiRecord.height,
        weight = bmiRecord.weight,
        date = bmiRecord.date,
        isFavorite = isFavorite
    )
}

fun FavoriteWithBmiData.toDomain(): FavoredBmiRecord {
    return FavoredBmiRecord(
        id = favorite.id,
        recordId = bmiData.id,
        bmi = bmiData.bmi,
        height = bmiData.height,
        weight = bmiData.weight,
        date = bmiData.date,
        note = favorite.customNote?: "",
    )
}
