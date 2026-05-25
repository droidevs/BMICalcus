package io.droidevs.bmicalc.data.preference.delegate

import androidx.datastore.core.DataStore
import io.droidevs.bmicalc.data.preference.exceptions.runCatchingPreference
import io.droidevs.bmicalc.domain.result.errors.PreferenceError
import io.droidevs.wallpaper.domain.result.Result


interface ProtoWriteDelegate<D> {


    suspend fun <T> set(
        value: T,
        update: (oldPrefs: D, newPrefs: T) -> D
    ): Result<D, PreferenceError>


    suspend fun set(
        value : D,
    ) : Result<D, PreferenceError>

    suspend fun set(
        update : (oldPrefs: D) -> D
    ) :  Result<D, PreferenceError>


}
class ProtoWriteDelegateImpl<D>(
    private val dataStore: DataStore<D>
) : ProtoWriteDelegate<D> {
    override suspend fun <T> set(
        value: T,
        update: (oldPrefs: D, newPrefs: T) -> D
    ): Result<D, PreferenceError> = runCatchingPreference {
        dataStore.updateData { prefs ->
            update(prefs, value)
        }
    }

    override suspend fun set(
        update : (oldPrefs: D) -> D
    ) :  Result<D, PreferenceError> = runCatchingPreference {
        dataStore.updateData { prefs ->
            update(prefs)
        }
    }

    override suspend fun set(value: D): Result<D, PreferenceError> = runCatchingPreference {
        dataStore.updateData {
            value
        }
    }

}
