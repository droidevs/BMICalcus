package io.droidevs.bmicalc.data.db

import androidx.room.Embedded

data class FavoriteWithBmiData(
    @Embedded
    val favorite: FavoriteBmiRecordEntity,

    @Embedded(prefix = "b_")
    val bmiData: BmiRecordEntity
)