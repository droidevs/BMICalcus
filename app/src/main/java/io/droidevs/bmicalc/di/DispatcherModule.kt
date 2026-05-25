package io.droidevs.bmicalc.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.bmicalc.dispatchers.CoroutineDispatcherProvider
import io.droidevs.bmicalc.dispatchers.DefaultDispatcherProvider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {

    @Provides
    @Singleton
    fun provideDispatchers(): CoroutineDispatcherProvider = DefaultDispatcherProvider()
}