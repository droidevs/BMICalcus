package io.droidevs.bmicalc.data.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import io.droidevs.bmicalc.data.model.UnitSystem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UnitSystemDataStore(
    private val dataStore : DataStore<Preferences>
) : UnitSystemPreference {
    override suspend fun saveUnitSystem(unitSystem: UnitSystem) {
        dataStore.edit {
            it[key] = unitSystem.name
        }
    }

    override fun restoreUnitSystem(): Flow<UnitSystem> {
        return dataStore.data.map {
            val name = it[key]
            UnitSystem.getUnit(name?: UnitSystem.METRIC.name)
        }
    }

    private val key = stringPreferencesKey("unit_system")
}