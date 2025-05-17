package io.droidevs.bmicalc.data.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import io.droidevs.bmicalc.data.model.Theme
import io.droidevs.bmicalc.data.preference.delegate.PreferenceDelegateImpl
import io.droidevs.bmicalc.domain.preference.ThemePreference
import io.droidevs.bmicalc.domain.result.map
import io.droidevs.bmicalc.domain.result.mapResult
import io.droidevs.wallpaper.domain.result.Result
import io.droidevs.wallpaper.domain.result.errors.PreferenceError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ThemePreferenceDataStore(
    val dataStore: DataStore<Preferences>
) : ThemePreference {

    val delegate by lazy {
        PreferenceDelegateImpl(
            dataStore,
            stringPreferencesKey("dark_mode_prefs"),
            Theme.SYSTEM.themeName
        )
    }

    override suspend fun changeMode(theme: Theme) : Result<Theme, PreferenceError>{
        return delegate.set(theme.themeName)
            .map { Theme.fromString(it) }
    }

    override fun restoreTheme(): Flow<Result<Theme, PreferenceError>> {
        return delegate.flow
            .mapResult { Theme.fromString(it) }
    }
}