package io.droidevs.bmicalc.data.db.converters

import androidx.room.TypeConverter
import io.droidevs.bmicalc.domain.GoalFlag
import io.droidevs.bmicalc.domain.GoalStatus

class GoalFlagConverters {
    @TypeConverter
    fun fromStatus(status: GoalFlag): String {
        return status.name // Stores the enum name in database
    }

    @TypeConverter
    fun toStatus(value: String): GoalFlag {
        return GoalFlag.valueOf(value)
    }
}