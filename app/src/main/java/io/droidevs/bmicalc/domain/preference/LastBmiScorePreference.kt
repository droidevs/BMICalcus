package io.droidevs.bmicalc.domain.preference

import io.droidevs.bmicalc.data.model.BmiScore
import io.droidevs.wallpaper.domain.result.Result
import io.droidevs.bmicalc.domain.result.errors.PreferenceError
import kotlinx.coroutines.flow.Flow


interface LastBmiScorePreference {

    suspend fun saveScore(score: BmiScore) : Result<BmiScore, PreferenceError>

    fun getScore() : Flow<Result<BmiScore, PreferenceError>>
}