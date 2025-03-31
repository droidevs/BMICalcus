package io.droidevs.bmicalc.data.preference


import io.droidevs.bmicalc.model.UnitSystem
import kotlinx.coroutines.flow.Flow

interface UnitSystemPreference {

    suspend fun saveUnitSystem(unitSystem: UnitSystem)

    fun restoreUnitSystem() : Flow<UnitSystem>

}