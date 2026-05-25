package io.droidevs.bmicalc.ui.model

import io.droidevs.bmicalc.domain.model.BmiRecord
import kotlinx.datetime.Instant

data class BmiRecordUi(
    val id : Long = -1,
    val height: Float = -1f,
    val weight: Float = -1f,
    val bmi: Float = -1f,
    val date: Long = -1,
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
        isFavorite = this.isFavorite
    )
}

fun BmiRecordUi.toDomainModel() : BmiRecord {
    return BmiRecord(
        id = id,
        height = height,
        weight = weight,
        bmi = bmi,
        date = date,
        isFavorite = isFavorite
    )
}
