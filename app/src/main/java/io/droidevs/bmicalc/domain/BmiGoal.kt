package io.droidevs.bmicalc.domain

import io.droidevs.bmicalc.data.db.dao.BmiGoalDao
import io.droidevs.bmicalc.data.db.entities.BmiGoalEntity
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

data class BmiGoal(
    val id: Long,
    val targetBmi: Float = 0f,
    val targetDate: Instant? = null,  // Optional target date
    val initialDate: Instant? = Clock.System.now(),
    val motivation: String = "",
    val flag: GoalFlag
)

fun BmiGoalEntity.toDomain() : BmiGoal {
    return BmiGoal(
        id = id,
        targetBmi = this.targetBmi,
        targetDate = if (this.targetDate != null) Instant.fromEpochMilliseconds(this.targetDate) else null,
        initialDate = if (this.initialDate != null) Instant.fromEpochMilliseconds(this.initialDate) else null,
        motivation = this.motivation,
        flag = flag,
    )
}

fun BmiGoal.toEntity() : BmiGoalEntity {
    return BmiGoalEntity(
        id = id,
        targetBmi = targetBmi,
        targetDate = targetDate?.toEpochMilliseconds(),
        initialDate = initialDate?.toEpochMilliseconds(),
        motivation = motivation,
        flag = flag
    )
}