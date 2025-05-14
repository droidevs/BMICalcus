package io.droidevs.bmicalc.ui.model

import io.droidevs.bmicalc.domain.BmiGoal
import io.droidevs.bmicalc.domain.BmiRecord
import io.droidevs.bmicalc.domain.GoalFlag
import kotlinx.datetime.Instant


data class GoalRecordUi(
    val id : Long,
    val targetBmi: Float,
    val initialDate: Instant?,
    val targetDate: Instant?,
    val motivation: String,
    val flag : GoalFlag,
    var isSelected : Boolean = false,
    val isFavorite : Boolean = false,
)


fun BmiGoal.toUiModel() : GoalRecordUi {
    return GoalRecordUi(
        id = id,
        targetBmi = targetBmi,
        initialDate = initialDate,
        targetDate = targetDate,
        motivation = motivation,
        flag = flag,
    )
}

fun GoalRecordUi.toDomainModel() : BmiGoal {
    return BmiGoal(
        id = id,
        targetBmi = targetBmi,
        initialDate = initialDate,
        targetDate = targetDate,
        motivation = motivation,
        flag = flag,
    )
}