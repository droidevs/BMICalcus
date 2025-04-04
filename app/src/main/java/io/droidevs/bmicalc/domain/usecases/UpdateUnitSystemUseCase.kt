package io.droidevs.bmicalc.domain.usecases

import io.droidevs.bmicalc.data.preference.UnitSystemPreference
import io.droidevs.bmicalc.data.model.UnitSystem

class UpdateUnitSystemUseCase(
    private val preference: UnitSystemPreference
) {

    suspend fun invoke(unitSystem : UnitSystem){
        preference.saveUnitSystem(unitSystem)
    }
}