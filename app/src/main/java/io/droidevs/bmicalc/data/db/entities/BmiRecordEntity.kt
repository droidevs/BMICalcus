package io.droidevs.bmicalc.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "bmi_records")
data class BmiRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "bmi")
    val bmi: Float,

    @ColumnInfo(name = "height")
    val height: Float,

    @ColumnInfo(name = "weight")
    val weight: Float,

    @ColumnInfo(name = "date")
    val date: Long = System.currentTimeMillis(),
)