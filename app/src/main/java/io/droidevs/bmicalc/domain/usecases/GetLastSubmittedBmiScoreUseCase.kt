package io.droidevs.bmicalc.domain.usecases

import io.droidevs.bmicalc.data.preference.LastBmiScorePreference
import io.droidevs.bmicalc.data.model.BmiScore
import kotlinx.coroutines.flow.Flow

class GetLastSubmittedBmiScoreUseCase(
    private val preference : LastBmiScorePreference
) {

    fun invoke() : Flow<BmiScore?>{
        return preference.retrieveScore()
    }
}