package io.droidevs.bmicalc.data.preference.delegate

import androidx.datastore.core.DataStore
import io.droidevs.bmicalc.data.preference.exceptions.flowCatchingPreference
import io.droidevs.bmicalc.domain.result.errors.PreferenceError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import io.droidevs.wallpaper.domain.result.Result

interface ProtoReadDelegate<D> {

    fun <T> get(
        defaultValue: T,
        mapper: ((D) -> T)? = null
    ): Flow<Result<T, PreferenceError>>


}
class ProtoReadDelegateImpl<D>(
    private val dataStore: DataStore<D>
) : ProtoReadDelegate<D> {

    override fun <T> get(
        defaultValue: T,
        mapper: ((D) -> T)?
    ): Flow<Result<T, PreferenceError>> = flowCatchingPreference {
        dataStore.data.map { value->
            mapper?.let { mapper
                mapper(value)
            }?: defaultValue
        }
    }
}
