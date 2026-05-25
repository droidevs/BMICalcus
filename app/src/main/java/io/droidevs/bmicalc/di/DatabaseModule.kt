package io.droidevs.bmicalc.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.droidevs.bmicalc.data.db.AppDatabase
import io.droidevs.bmicalc.data.db.dao.BmiDao
import io.droidevs.bmicalc.data.db.dao.BmiGoalDao
import io.droidevs.bmicalc.data.db.dao.FavoriteBmiRecordDao
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {


    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase(context)
    }


    @Provides
    fun provideMbiDao(database: AppDatabase): BmiDao {
        return database.bmiDao()
    }

    @Provides
    fun provideFavoriteBmiRecordDao(database: AppDatabase): FavoriteBmiRecordDao {
        return database.favoriteBmiRecordDao()
    }

    @Provides
    fun provideBmiGoalDao(database: AppDatabase): BmiGoalDao {
        return database.bmiGoalDao()
    }
}
