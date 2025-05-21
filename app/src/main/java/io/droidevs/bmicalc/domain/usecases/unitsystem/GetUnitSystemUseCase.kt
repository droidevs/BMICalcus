package io.droidevs.bmicalc.domain.usecases.unitsystem

import io.droidevs.bmicalc.domain.preference.UnitSystemPreference
import io.droidevs.bmicalc.data.model.UnitSystem
import io.droidevs.wallpaper.domain.result.Result
import io.droidevs.bmicalc.domain.result.errors.PreferenceError
import kotlinx.coroutines.flow.Flow

class GetUnitSystemUseCase(
    private val unitSystemPreference: UnitSystemPreference
) {

    operator fun invoke() : Flow<Result<UnitSystem, PreferenceError>>{
        return unitSystemPreference.getUnitSystem()
    }
}