package io.droidevs.bmicalc.domain

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

enum class GoalStatus(
    val color: Color,
    val description: String
) {
    NOT_SET(
        color = Color.Gray,
        description = "No goal set"
    ),
    ON_TRACK(
        color = Color(0xFF42A5F5),
        description = "On track to goal"
    ),
    BEHIND_SCHEDULE(
        color = Color(0xFFFFA726),
        description = "Behind schedule"
    ),
    AHEAD_OF_SCHEDULE(
        color = Color(0xFFAB47BC),
        description = "Ahead of schedule"
    ),
    ACHIEVED(
        color = Color(0xFF66BB6A),
        description = "Goal achieved!"
    );

    companion object {
        fun safeValueOf(value: String): GoalStatus {
            return try {
                valueOf(value)
            } catch (e: IllegalArgumentException) {
                NOT_SET // Default value if parsing fails
            }
        }

        fun fromProgress(progress: Float, expectedProgress: Float): GoalStatus {
            return when {
                progress >= 1f -> ACHIEVED
                progress >= expectedProgress * 1.1f -> AHEAD_OF_SCHEDULE
                progress <= expectedProgress * 0.7f -> BEHIND_SCHEDULE
                else -> ON_TRACK
            }
        }
    }

    val isEditable: Boolean
        get() = this == ON_TRACK || this == AHEAD_OF_SCHEDULE || this == NOT_SET

    val isCompleted: Boolean
        get() = this == ACHIEVED

    fun toDisplayText(): String {
        return description
    }
}