package io.droidevs.bmicalc.data.preference.delegate

import androidx.datastore.core.DataStore
import io.droidevs.bmicalc.data.preference.exceptions.flowCatchingPreference
import io.droidevs.bmicalc.data.preference.exceptions.runCatchingPreference
import io.droidevs.bmicalc.data.preference.exceptions.runCatchingPreferenceWithResult
import kotlinx.coroutines.flow.Flow
import io.droidevs.wallpaper.domain.result.Result
import io.droidevs.wallpaper.domain.result.errors.PreferenceError
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class MappedProtoPreferenceDelegate<T, D>(
    private val dataStore: DataStore<D>,
    private val defaultValue: T?,
    private val getterMapper: (D) -> T?,
    private val setterMapper: (D, T?) -> D
) {
    val flow: Flow<Result<T?, PreferenceError>> by lazy {
        flowCatchingPreference {
            dataStore.data.map { getterMapper(it) ?: defaultValue }
        }
    }

    suspend fun get(): Result<T?, PreferenceError> = runCatchingPreferenceWithResult {
        flow.first()
    }

    suspend fun set(value: T?): Result<D, PreferenceError> = runCatchingPreference {
        dataStore.updateData { prefs ->
            setterMapper(prefs, value)
        }
    }
}
