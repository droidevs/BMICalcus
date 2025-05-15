package io.droidevs.bmicalc.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.droidevs.bmicalc.domain.GoalFlag
import io.droidevs.bmicalc.domain.GoalStatus

@Entity(tableName = "bmi_goals")
data class BmiGoalEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val targetBmi: Float,

    @ColumnInfo(name = "target_date")
    val targetDate: Long?, // Stored as epoch millis

    @ColumnInfo(name = "initial_date")
    val initialDate: Long?,

    @ColumnInfo(name = "motivation")
    val motivation: String,

    @ColumnInfo(name = "flag")
    val flag : GoalFlag,
)
