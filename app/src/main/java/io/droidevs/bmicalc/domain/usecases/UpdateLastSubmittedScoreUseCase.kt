package io.droidevs.bmicalc.domain.usecases

import io.droidevs.bmicalc.domain.preference.LastBmiScorePreference
import io.droidevs.bmicalc.data.model.BmiScore
import io.droidevs.bmicalc.domain.result.errors.PreferenceError
import io.droidevs.wallpaper.domain.result.Result

class UpdateLastSubmittedScoreUseCase(
    private val preference : LastBmiScorePreference
) {


    suspend fun invoke(bmiScore: BmiScore): Result<BmiScore, PreferenceError> {
        return preference.saveScore(score = bmiScore)
    }
}
