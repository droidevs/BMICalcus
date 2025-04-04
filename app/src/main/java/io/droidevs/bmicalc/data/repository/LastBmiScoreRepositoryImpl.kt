package io.droidevs.bmicalc.data.repository

import io.droidevs.bmicalc.data.model.BmiScore
import io.droidevs.bmicalc.data.preference.LastBmiScorePreference
import kotlinx.coroutines.flow.*

class LastBmiScoreRepositoryImpl(
    private val preference: LastBmiScorePreference
) : LastBmiScoreRepository {

    override fun getScore(): Flow<BmiScore?> {
        return preference.retrieveScore()
    }

    override suspend fun setScore(score: BmiScore) {
        preference.saveScore(score)
    }
}
