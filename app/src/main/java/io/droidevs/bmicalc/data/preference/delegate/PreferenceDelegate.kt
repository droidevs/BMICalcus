package io.droidevs.bmicalc.data.preference.delegate

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import io.droidevs.bmicalc.data.preference.exceptions.flowCatchingPreference
import io.droidevs.bmicalc.data.preference.exceptions.runCatchingPreference
import io.droidevs.bmicalc.data.preference.exceptions.runCatchingPreferenceWithResult
import io.droidevs.wallpaper.domain.result.Result
import io.droidevs.bmicalc.domain.result.errors.PreferenceError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


interface PreferenceDelegate<T> {

    val flow : Flow<Result<T, PreferenceError>>

    suspend fun get(): Result<T, PreferenceError>

    suspend fun set(value: T): Result<T, PreferenceError>

}
class PreferenceDelegateImpl<T>(
    private val dataStore: DataStore<Preferences>,
    private val key: Preferences.Key<T>,
    private val defaultValue: T,
) : PreferenceDelegate<T> {
    override val flow : Flow<Result<T, PreferenceError>> by lazy {
        flowCatchingPreference {
            dataStore.data
                .map { it[key] ?: defaultValue }
        }
    }



    override suspend fun get(): Result<T, PreferenceError> = runCatchingPreferenceWithResult {
        flow.first()
    }

    override suspend fun set(value: T): Result<T, PreferenceError> = runCatchingPreference {
        dataStore.edit { prefs ->
            prefs[key] = value
        }.get(key)?: defaultValue
    }
}