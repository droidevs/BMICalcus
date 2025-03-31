package io.droidevs.bmicalc.domain.usecases

import io.droidevs.bmicalc.data.preference.LastBmiScorePreference
import io.droidevs.bmicalc.model.BmiScore

class UpdateLastSubmittedScoreUseCase(
    private val preference : LastBmiScorePreference
) {


    suspend fun invoke(bmiScore: BmiScore){
        preference.saveScore(score = bmiScore)
    }
}