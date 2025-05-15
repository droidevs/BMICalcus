package io.droidevs.bmicalc.data.model

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

// Add to your existing data classes


@Serializable
data class ActiveBmiGoal(
    val targetBmi: Float = 0f,
    val initialBmi: Float = 0f,
    val targetDate: Long? = null,  // Optional target date
    val initialDate: Long = Clock.System.now().toEpochMilliseconds(),
    val motivation: String = ""
)