package io.droidevs.bmicalc.data.repository

import io.droidevs.bmicalc.data.model.UnitSystem
import kotlinx.coroutines.flow.Flow

interface UnitSystemRepository {
    suspend fun setUnitSystem(unitSystem: UnitSystem)
    fun getUnitSystem(): Flow<UnitSystem>
}
