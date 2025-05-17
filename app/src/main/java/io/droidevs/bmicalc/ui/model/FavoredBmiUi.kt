package io.droidevs.bmicalc.ui.model


import io.droidevs.bmicalc.domain.model.FavoredBmiRecord


data class FavoredBmiUi(
    val id : Long,
    val height: Float,
    val weight: Float,
    val bmi: Float,
    val date: Long,
    var isSelected : Boolean = false,
)


fun FavoredBmiRecord.toUiModel() : FavoredBmiUi {
    return FavoredBmiUi(
        id = this.id,
        height = this.height,
        weight = this.weight,
        bmi = this.bmi,
        date = date,
    )
}

//fun FavoredBmiUi.toDomainModel() : FavoredBmiRecord {
//    return FavoredBmiRecord(
//        id = id,
//        recordId = ,
//        height = height,
//        weight = weight,
//        bmi = bmi,
//        date = date
//    )
//}