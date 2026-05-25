package io.droidevs.bmicalc.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.bmicalc.data.db.dao.BmiDao
import io.droidevs.bmicalc.data.db.dao.BmiGoalDao
import io.droidevs.bmicalc.data.db.dao.FavoriteBmiRecordDao
import io.droidevs.bmicalc.data.repository.BmiGoalRecordRepositoryImpl
import io.droidevs.bmicalc.data.repository.BmiRecordRepositoryImpl
import io.droidevs.bmicalc.data.repository.FavoriteBmiRepositoryImpl
import io.droidevs.bmicalc.dispatchers.CoroutineDispatcherProvider
import io.droidevs.bmicalc.domain.repository.BmiGoalRecordRepository
import io.droidevs.bmicalc.domain.repository.BmiRepository
import io.droidevs.bmicalc.domain.repository.FavoriteBmiRepository
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {


    @Provides
    @Singleton
    fun provideBmiRepository(
        bmi: BmiDao
    ) : BmiRepository {
        return BmiRecordRepositoryImpl(bmiDao = bmi)
    }

    @Provides
    @Singleton
    fun provideFavoriteBmiRepository(
        favoriteDao: FavoriteBmiRecordDao
    ) : FavoriteBmiRepository {
        return FavoriteBmiRepositoryImpl(favoriteDao = favoriteDao)
    }

    @Provides
    @Singleton
    fun provideBmiGoalRepository(
        goalDao: BmiGoalDao,
        dispatcherProvider: CoroutineDispatcherProvider
    ) : BmiGoalRecordRepository {
        return BmiGoalRecordRepositoryImpl(dao = goalDao, dispatcher = dispatcherProvider)
    }
}
