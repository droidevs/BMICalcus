package io.droidevs.bmicalc.domain.usecases

import io.droidevs.bmicalc.data.preference.LastBmiScorePreference
import io.droidevs.bmicalc.data.model.BmiScore
import io.droidevs.wallpaper.domain.result.Result
import io.droidevs.wallpaper.domain.result.errors.PreferenceError
import kotlinx.coroutines.flow.Flow

class GetBmiScoreUseCase(
    private val preference : LastBmiScorePreference
) {

    fun invoke() : Flow<Result<BmiScore,PreferenceError>>{
        return preference.getScore()
    }
}