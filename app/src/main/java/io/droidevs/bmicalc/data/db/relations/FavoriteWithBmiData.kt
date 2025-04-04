package io.droidevs.bmicalc.data.db.relations

import androidx.room.Embedded
import io.droidevs.bmicalc.data.db.entities.BmiRecordEntity
import io.droidevs.bmicalc.data.db.entities.FavoriteBmiRecordEntity

data class FavoriteWithBmiData(
    @Embedded
    val favorite: FavoriteBmiRecordEntity,

    @Embedded(prefix = "b_")
    val bmiData: BmiRecordEntity
)