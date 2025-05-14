package io.droidevs.bmicalc.ui.model

import io.droidevs.bmicalc.domain.BmiRecord
import kotlinx.datetime.Instant

data class BmiRecordUi(
    val id : Long,
    val height: Float,
    val weight: Float,
    val bmi: Float,
    val date: Instant,
    var isSelected : Boolean = false,
    val isFavorite : Boolean = false,
)


fun BmiRecord.toUiModel() : BmiRecordUi {
    return BmiRecordUi(
        id = this.id,
        height = this.height,
        weight = this.weight,
        bmi = this.bmi,
        date = date,
    )
}

fun BmiRecordUi.toDomainModel() : BmiRecord {
    return BmiRecord(
        id = id,
        height = height,
        weight = weight,
        bmi = bmi,
        date = date
    )
}