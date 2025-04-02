package io.droidevs.bmicalc.domain

import io.droidevs.bmicalc.data.db.FavoriteWithBmiData

class FavoredBmiRecord(
    val id: Long,
    val recordId: Long,
    val bmi: Float,
    val note: String?,
    val height: Float,
    val weight: Float,
    val date: Long,
    val favoredDate: Long,
)


fun FavoriteWithBmiData.toDomain(): FavoredBmiRecord {
    return FavoredBmiRecord(
        id = this.favorite.id,
        recordId = this.bmiData.id,
        bmi = this.bmiData.bmi,
        note = this.favorite.customNote,
        height = this.bmiData.height,
        weight = this.bmiData.weight,
        date = this.bmiData.date,
        favoredDate = this.favorite.addedAt,
    )
}