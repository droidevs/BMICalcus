package io.droidevs.bmicalc.domain.usecases.goal

import io.droidevs.bmicalc.data.model.ActiveBmiGoal
import io.droidevs.bmicalc.data.preference.BmiGoalPreference
import io.droidevs.wallpaper.domain.result.Result
import io.droidevs.wallpaper.domain.result.errors.PreferenceError

class SetActiveBmiGoalUseCase(
    private val preference : BmiGoalPreference
) {

    suspend fun invoke(goal : ActiveBmiGoal) : Result<ActiveBmiGoal, PreferenceError> {
        return preference.setGoal(goal)
    }
}