package io.droidevs.bmicalc.data.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import io.droidevs.bmicalc.data.model.BmiScore
import io.droidevs.bmicalc.data.preference.delegate.MapperPreferenceDelegate
import io.droidevs.bmicalc.data.preference.delegate.PreferenceDelegate
import io.droidevs.bmicalc.data.preference.delegate.PreferenceDelegateImpl
import io.droidevs.bmicalc.data.preference.delegate.ProtoReadDelegate
import io.droidevs.bmicalc.data.preference.exceptions.flowCatchingPreference
import io.droidevs.bmicalc.data.preference.exceptions.runCatchingPreference
import io.droidevs.bmicalc.domain.preference.LastBmiScorePreference
import io.droidevs.bmicalc.domain.result.map
import io.droidevs.bmicalc.domain.result.mapResult
import io.droidevs.wallpaper.domain.result.Result
import io.droidevs.wallpaper.domain.result.errors.PreferenceError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LastBmiScorePreferenceDataStore(
    val dataStore: DataStore<Preferences>
) : LastBmiScorePreference {

    val delegate by lazy {
        PreferenceDelegateImpl(
            dataStore = dataStore,
            key = floatPreferencesKey("last_bmi_score_prefs"),
            defaultValue = 0f
        )
    }
    override suspend fun saveScore(score: BmiScore): Result<BmiScore, PreferenceError> =
        delegate.set(score.value).map { score->
            BmiScore(score)
        }

    override fun getScore(): Flow<Result<BmiScore, PreferenceError>> =
        delegate.flow.mapResult { score->
            BmiScore(score)
        }
}