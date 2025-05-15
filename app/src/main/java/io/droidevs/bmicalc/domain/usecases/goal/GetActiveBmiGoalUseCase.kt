package io.droidevs.bmicalc.domain.usecases.goal

import io.droidevs.bmicalc.data.model.ActiveBmiGoal
import io.droidevs.bmicalc.data.preference.BmiGoalPreference
import io.droidevs.wallpaper.domain.result.Result
import io.droidevs.wallpaper.domain.result.errors.PreferenceError
import kotlinx.coroutines.flow.Flow


class GetActiveBmiGoalUseCase(
    private val bmiGoalPreference: BmiGoalPreference
) {


    suspend operator fun invoke() : Flow<Result<ActiveBmiGoal, PreferenceError>> {
        return bmiGoalPreference.getGoal()
    }
}