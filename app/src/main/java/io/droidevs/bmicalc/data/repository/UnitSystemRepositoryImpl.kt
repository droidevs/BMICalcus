package io.droidevs.bmicalc.data.repository

import io.droidevs.bmicalc.data.model.UnitSystem
import io.droidevs.bmicalc.data.preference.UnitSystemPreference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class UnitSystemRepositoryImpl(
    private val preference: UnitSystemPreference
) : UnitSystemRepository {


    // Public flow that will emit the cached or preference value
    override fun getUnitSystem(): Flow<UnitSystem> {
        return preference.restoreUnitSystem()
    }

    override suspend fun setUnitSystem(unitSystem: UnitSystem) {
        preference.saveUnitSystem(unitSystem)
    }
}
