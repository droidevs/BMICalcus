package io.droidevs.bmicalc.data.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import io.droidevs.bmicalc.model.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ThemePreferenceDataStore(
    val dataStore: DataStore<Preferences>
) : ThemePreference {
    override suspend fun changeMode(theme: Theme) {
        dataStore.edit {
            it[key] = theme.themeName
        }
    }

    override fun restoreTheme(): Flow<Theme> {
        return dataStore.data.map {
            val themeName = it[key]?: Theme.SYSTEM.themeName
            Theme.fromString(themeName)
        }
    }

    private val key = stringPreferencesKey("dark_mode_prefs")
}