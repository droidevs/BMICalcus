package io.droidevs.bmicalc.data.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import io.droidevs.bmicalc.data.model.BmiScore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LastBmiScorePreferenceDataStore(
    val dataStore: DataStore<Preferences>
) : LastBmiScorePreference {
    override suspend fun saveScore(score: BmiScore) {
        dataStore.edit {
            it[key] = score.value
        }
    }

    override fun retrieveScore(): Flow<BmiScore?> {
        return dataStore.data.map {
            val score = it[key]
            score?.let {
                BmiScore(score)
            }
        }
    }

    private val key = floatPreferencesKey("last_bmi_score_prefs")
}