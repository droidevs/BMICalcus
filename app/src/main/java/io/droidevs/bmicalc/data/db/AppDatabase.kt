package io.droidevs.bmicalc.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.droidevs.bmicalc.data.db.dao.BmiDao
import io.droidevs.bmicalc.data.db.entities.BmiRecordEntity


@Database(
    entities = [BmiRecordEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase(){
    abstract fun bmiDao() : BmiDao

    companion object {

        @Volatile
        private var instance: AppDatabase? = null

        operator fun invoke(context: Context) : AppDatabase {
            return create(context)
        }

        fun create(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context = context.applicationContext,
                    klass = AppDatabase::class.java,
                    name = "bmi_database"
                ).build()
                instance
            }
        }
    }
}