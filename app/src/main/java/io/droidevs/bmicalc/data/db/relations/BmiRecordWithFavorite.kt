package io.droidevs.bmicalc.data.db.relations

import androidx.room.ColumnInfo
import androidx.room.Embedded
import io.droidevs.bmicalc.data.db.entities.BmiRecordEntity

data class BmiRecordWithFavorite(
    @Embedded
    val bmiRecord: BmiRecordEntity,

    @ColumnInfo(name = "isFavorite")
    val isFavorite: Boolean
)