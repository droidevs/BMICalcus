package io.droidevs.bmicalc.data.mapers

import io.droidevs.bmicalc.data.db.entities.BmiGoalEntity
import io.droidevs.bmicalc.domain.model.BmiGoal
import kotlinx.datetime.Instant

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