package io.droidevs.bmicalc.domain.model

import io.droidevs.bmicalc.domain.GoalFlag
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