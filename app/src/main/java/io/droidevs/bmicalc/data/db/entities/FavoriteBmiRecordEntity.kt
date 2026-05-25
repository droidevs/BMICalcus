package io.droidevs.bmicalc.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "favorite_bmi_records",
    foreignKeys = [
        ForeignKey(
            entity = BmiRecordEntity::class,
            parentColumns = ["id"],
            childColumns = ["bmiRecordId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["bmiRecordId"])
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
