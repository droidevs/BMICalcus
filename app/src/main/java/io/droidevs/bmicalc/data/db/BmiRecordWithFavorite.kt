package io.droidevs.bmicalc.data.db

import androidx.room.ColumnInfo
import androidx.room.Embedded

data class BmiRecordWithFavorite(
    @Embedded
    val bmiRecord: BmiRecordEntity,

    @ColumnInfo(name = "isFavorite")
    val isFavorite: Boolean
)