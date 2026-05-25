package io.droidevs.bmicalc.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.droidevs.bmicalc.data.db.dao.BmiDao
import io.droidevs.bmicalc.data.db.dao.BmiGoalDao
import io.droidevs.bmicalc.data.db.dao.FavoriteBmiRecordDao
import io.droidevs.bmicalc.data.db.converters.GoalFlagConverters
import io.droidevs.bmicalc.data.db.entities.BmiGoalEntity
import io.droidevs.bmicalc.data.db.entities.BmiRecordEntity
import io.droidevs.bmicalc.data.db.entities.FavoriteBmiRecordEntity


@Database(
    entities = [
        BmiRecordEntity::class,
        FavoriteBmiRecordEntity::class,
        BmiGoalEntity::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(GoalFlagConverters::class)
abstract class AppDatabase : RoomDatabase(){
    abstract fun bmiDao() : BmiDao
    abstract fun favoriteBmiRecordDao(): FavoriteBmiRecordDao
    abstract fun bmiGoalDao(): BmiGoalDao

    companion object {

        @Volatile
        private var instance: AppDatabase? = null

        operator fun invoke(context: Context) : AppDatabase {
            return create(context)
        }

        fun create(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                val db = Room.databaseBuilder(
                    context = context.applicationContext,
                    klass = AppDatabase::class.java,
                    name = "bmi_database"
                ).fallbackToDestructiveMigration().build()
                instance = db
                db
            }
        }
    }
}
