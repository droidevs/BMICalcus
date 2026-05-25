package io.droidevs.bmicalc.di

import android.content.Context
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.droidevs.bmicalc.domain.preference.LastBmiScorePreference
import io.droidevs.bmicalc.data.preference.LastBmiScorePreferenceDataStore
import io.droidevs.bmicalc.domain.preference.ThemePreference
import io.droidevs.bmicalc.data.preference.ThemePreferenceDataStore
import io.droidevs.bmicalc.data.model.ActiveBmiGoal
import io.droidevs.bmicalc.data.preference.BmiGoalPreferenceDataStore
import io.droidevs.bmicalc.data.preference.BmiGoalSerializer
import io.droidevs.bmicalc.data.preference.UnitSystemDataStore
import io.droidevs.bmicalc.domain.preference.BmiGoalPreference
import io.droidevs.bmicalc.domain.preference.UnitSystemPreference
import java.io.File
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {


    @Provides
    @Singleton
    fun providePreferenceDataStore(@ApplicationContext context: Context): DataStore<Preferences>{
        return PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile("app_preferences") }
        )
    }

    @Provides
    @Singleton
    fun provideBmiGoalDataStore(@ApplicationContext context: Context): DataStore<ActiveBmiGoal> {
        return DataStoreFactory.create(
            serializer = BmiGoalSerializer(),
            produceFile = { File(context.filesDir, "active_bmi_goal.pb") },
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        )
    }

    @Provides
    fun provideThemePreference(dataStore: DataStore<Preferences>) : ThemePreference {
        return ThemePreferenceDataStore(dataStore)
    }

    @Provides
    fun provideLastScorePreference(dataStore: DataStore<Preferences>) : LastBmiScorePreference {
        return LastBmiScorePreferenceDataStore(dataStore)
    }

    @Provides
    fun provideUnitSystemPreference(dataStore: DataStore<Preferences>) : UnitSystemPreference {
        return UnitSystemDataStore(dataStore)
    }

    @Provides
    fun provideBmiGoalPreference(dataStore: DataStore<ActiveBmiGoal>) : BmiGoalPreference {
        return BmiGoalPreferenceDataStore(dataStore)
    }

}
