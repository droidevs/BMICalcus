package io.droidevs.bmicalc.domain.usecases.unitsystem

import io.droidevs.bmicalc.domain.preference.UnitSystemPreference
import io.droidevs.bmicalc.data.model.UnitSystem
import io.droidevs.wallpaper.domain.result.Result
import io.droidevs.bmicalc.domain.result.errors.PreferenceError

class UpdateUnitSystemUseCase(
    private val preference: UnitSystemPreference
) {

    suspend fun invoke(unitSystem : UnitSystem) : Result<UnitSystem, PreferenceError>{
        return preference.saveUnitSystem(unitSystem)
    }
}