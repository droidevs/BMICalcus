package io.droidevs.bmicalc.data.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import io.droidevs.bmicalc.data.model.UnitSystem
import io.droidevs.bmicalc.data.preference.exceptions.flowCatchingPreference
import io.droidevs.bmicalc.data.preference.exceptions.runCatchingPreference
import io.droidevs.wallpaper.domain.result.Result
import io.droidevs.wallpaper.domain.result.errors.PreferenceError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UnitSystemDataStore(
    private val dataStore : DataStore<Preferences>
) : UnitSystemPreference {
    override suspend fun saveUnitSystem(unitSystem: UnitSystem): Result<UnitSystem, PreferenceError> {
        return runCatchingPreference {
            dataStore.edit {
                it[key] = unitSystem.name
            }[key]?.let {
                UnitSystem.getUnit(it)
            }?: UnitSystem.METRIC
        }
    }

    override fun getUnitSystem(): Flow<Result<UnitSystem, PreferenceError>> {
        return flowCatchingPreference {
            dataStore.data.map {
                val name = it[key]
                UnitSystem.getUnit(name?: UnitSystem.METRIC.name)
            }
        }
    }

    private val key = stringPreferencesKey("unit_system")
}