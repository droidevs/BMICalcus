package io.droidevs.bmicalc.data.preference

import androidx.datastore.core.DataStore
import io.droidevs.bmicalc.data.model.ActiveBmiGoal
import io.droidevs.bmicalc.data.preference.exceptions.flowCatchingPreference
import io.droidevs.bmicalc.data.preference.exceptions.runCatchingPreference
import io.droidevs.wallpaper.domain.result.Result
import io.droidevs.wallpaper.domain.result.errors.PreferenceError
import kotlinx.coroutines.flow.Flow

class BmiGoalPreferenceDataStore(
    private val datastore: DataStore<ActiveBmiGoal>
) : BmiGoalPreference {
    override suspend fun setGoal(goal: ActiveBmiGoal): Result<ActiveBmiGoal, PreferenceError> {
        return runCatchingPreference {
            datastore.updateData {
                goal
            }
        }
    }

    override fun getGoal(): Flow<Result<ActiveBmiGoal, PreferenceError>> {
        return flowCatchingPreference {
            datastore.data
        }
    }
}