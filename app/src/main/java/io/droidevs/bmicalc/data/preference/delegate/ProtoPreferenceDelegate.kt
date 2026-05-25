package io.droidevs.bmicalc.data.preference.delegate

import androidx.datastore.core.DataStore
import io.droidevs.bmicalc.data.preference.exceptions.flowCatchingPreference
import io.droidevs.bmicalc.data.preference.exceptions.runCatchingPreference
import io.droidevs.bmicalc.data.preference.exceptions.runCatchingPreferenceWithResult
import io.droidevs.bmicalc.domain.result.errors.PreferenceError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import io.droidevs.wallpaper.domain.result.Result
import kotlinx.coroutines.flow.first

interface ProtoPreferenceDelegate<T> {
    val flow: Flow<Result<T, PreferenceError>>

    suspend fun get(): Result<T, PreferenceError>

    suspend fun set(value: T): Result<T, PreferenceError>
}
class ProtoPreferenceDelegateImpl<T>(
    private val dataStore: DataStore<T>,
    private val defaultValue: T
) {
    val flow: Flow<Result<T, PreferenceError>> by lazy {
        flowCatchingPreference {
            dataStore.data.map { it ?: defaultValue }
        }
    }

    suspend fun get(): Result<T, PreferenceError> = runCatchingPreferenceWithResult {
        flow.first()
    }

    suspend fun set(value: T): Result<T, PreferenceError> = runCatchingPreference {
        dataStore.updateData { prefs ->
            value
        }
    }
}
