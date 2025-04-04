package io.droidevs.bmicalc.data.repository

import io.droidevs.bmicalc.data.model.BmiScore
import kotlinx.coroutines.flow.Flow

interface LastBmiScoreRepository {
    suspend fun setScore(score: BmiScore)
    fun getScore(): Flow<BmiScore?>
}
