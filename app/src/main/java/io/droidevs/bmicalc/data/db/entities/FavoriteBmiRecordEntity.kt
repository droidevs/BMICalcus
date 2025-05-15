package io.droidevs.bmicalc.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import io.droidevs.bmicalc.domain.model.BmiRecord

@Entity(
    tableName = "favorite_bmi_records",
    foreignKeys = [
        ForeignKey(
            entity = BmiRecord::class,
            parentColumns = ["id"],
            childColumns = ["bmiRecordId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class FavoriteBmiRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "bmiRecordId")
    val bmiRecordId: Long,

    @ColumnInfo(name = "customNote")
    val customNote: String? = null,

    @ColumnInfo(name = "dateFavored")
    val addedAt: Long,
)