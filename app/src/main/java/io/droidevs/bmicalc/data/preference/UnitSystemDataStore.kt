package io.droidevs.bmicalc.data.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import io.droidevs.bmicalc.data.model.UnitSystem
import io.droidevs.bmicalc.data.preference.delegate.PreferenceDelegate
import io.droidevs.bmicalc.data.preference.delegate.PreferenceDelegateImpl
import io.droidevs.bmicalc.data.preference.exceptions.flowCatchingPreference
import io.droidevs.bmicalc.data.preference.exceptions.runCatchingPreference
import io.droidevs.bmicalc.domain.preference.UnitSystemPreference
import io.droidevs.bmicalc.domain.result.map
import io.droidevs.bmicalc.domain.result.mapResult
import io.droidevs.wallpaper.domain.result.Result
import io.droidevs.wallpaper.domain.result.errors.PreferenceError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UnitSystemDataStore(
    private val dataStore : DataStore<Preferences>
) : UnitSystemPreference {


    val delegate by lazy {
        PreferenceDelegateImpl(
            dataStore,
            stringPreferencesKey("unit_system"),
            UnitSystem.METRIC.name
        )
    }
    override suspend fun saveUnitSystem(unitSystem: UnitSystem): Result<UnitSystem, PreferenceError> {
        return delegate.set(unitSystem.name)
            .map { UnitSystem.getUnit(it) }
    }

    override fun getUnitSystem(): Flow<Result<UnitSystem, PreferenceError>> {
        return delegate.flow
            .mapResult { UnitSystem.getUnit(it) }
    }
}