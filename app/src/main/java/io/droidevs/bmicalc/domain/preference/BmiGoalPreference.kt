package io.droidevs.bmicalc.domain.preference

import io.droidevs.bmicalc.data.model.ActiveBmiGoal
import io.droidevs.wallpaper.domain.result.Result
import io.droidevs.bmicalc.domain.result.errors.PreferenceError
import kotlinx.coroutines.flow.Flow


interface BmiGoalPreference {
    suspend fun setGoal(goal: ActiveBmiGoal) : Result<ActiveBmiGoal, PreferenceError>

    fun getGoal() : Flow<Result<ActiveBmiGoal, PreferenceError>>
}