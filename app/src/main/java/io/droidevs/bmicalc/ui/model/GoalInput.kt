package io.droidevs.bmicalc.ui.model

data class GoalInput(
    val targetDate: Long? = null,
    val targetBmi: Float = 0f,
    val motivation: String = ""
)