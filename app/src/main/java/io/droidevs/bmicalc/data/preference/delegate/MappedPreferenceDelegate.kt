package io.droidevs.bmicalc.data.preference.delegate

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import io.droidevs.bmicalc.domain.result.map
import io.droidevs.bmicalc.domain.result.mapResult
import io.droidevs.wallpaper.domain.result.Result
import io.droidevs.bmicalc.domain.result.errors.PreferenceError
import kotlinx.coroutines.flow.Flow

interface MapperPreferenceDelegate<T> {

    val flow: Flow<Result<T, PreferenceError>>

    suspend fun get(): Result<T, PreferenceError>

    suspend fun set(value: T): Result<T, PreferenceError>

}
class MapperPreferenceDelegateImpl<T>(
    private val dataStore: DataStore<Preferences>,
    private val keyName: String,
    private val defaultValue: T,
    private val mapperToString: (T) -> String,
    private val mapperFromString: (String) -> T
) : MapperPreferenceDelegate<T> {

    val delegate : PreferenceDelegate<String> by lazy {
        PreferenceDelegateImpl(
            dataStore,
            key = stringPreferencesKey(keyName),
            mapperToString(defaultValue)
        )
    }

    override val flow: Flow<Result<T, PreferenceError>>
        get() = delegate.flow
            .mapResult {
                mapperFromString(it)
            }


    override suspend fun get(): Result<T, PreferenceError> {
        return delegate.get().map {
            mapperFromString(it)
        }
    }

    override suspend fun set(value: T): Result<T, PreferenceError> {
        return delegate.set(mapperToString(value)).map(mapperFromString)
    }
}