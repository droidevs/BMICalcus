package io.droidevs.bmicalc.domain.usecases

import io.droidevs.bmicalc.data.preference.UnitSystemPreference
import io.droidevs.bmicalc.data.model.UnitSystem
import io.droidevs.wallpaper.domain.result.Result
import io.droidevs.wallpaper.domain.result.errors.PreferenceError

class UpdateUnitSystemUseCase(
    private val preference: UnitSystemPreference
) {

    suspend fun invoke(unitSystem : UnitSystem) : Result<UnitSystem, PreferenceError>{
        return preference.saveUnitSystem(unitSystem)
    }
}