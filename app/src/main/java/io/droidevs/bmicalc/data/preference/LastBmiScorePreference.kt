package io.droidevs.bmicalc.data.preference

import io.droidevs.bmicalc.model.BmiScore
import kotlinx.coroutines.flow.Flow


interface LastBmiScorePreference {

    suspend fun saveScore(score: BmiScore)

    fun retrieveScore() : Flow<BmiScore?>
}