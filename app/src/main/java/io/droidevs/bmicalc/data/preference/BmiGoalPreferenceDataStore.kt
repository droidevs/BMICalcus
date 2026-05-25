package io.droidevs.bmicalc.data.preference

import androidx.datastore.core.DataStore
import io.droidevs.bmicalc.data.model.ActiveBmiGoal
import io.droidevs.bmicalc.data.preference.delegate.ProtoReadDelegate
import io.droidevs.bmicalc.data.preference.delegate.ProtoReadDelegateImpl
import io.droidevs.bmicalc.data.preference.delegate.ProtoWriteDelegate
import io.droidevs.bmicalc.data.preference.delegate.ProtoWriteDelegateImpl
import io.droidevs.bmicalc.domain.preference.BmiGoalPreference
import io.droidevs.wallpaper.domain.result.Result
import io.droidevs.bmicalc.domain.result.errors.PreferenceError
import kotlinx.coroutines.flow.Flow

class BmiGoalPreferenceDataStore(
    private val datastore: DataStore<ActiveBmiGoal>
) : BmiGoalPreference,
    ProtoReadDelegate<ActiveBmiGoal> by ProtoReadDelegateImpl(datastore),
    ProtoWriteDelegate<ActiveBmiGoal> by ProtoWriteDelegateImpl(datastore) {

    override suspend fun setGoal(goal: ActiveBmiGoal): Result<ActiveBmiGoal, PreferenceError> {
        return set(
            value = goal
        )
    }

    override fun getGoal(): Flow<Result<ActiveBmiGoal, PreferenceError>> {
        return get(
            defaultValue = ActiveBmiGoal()
        )
    }
}