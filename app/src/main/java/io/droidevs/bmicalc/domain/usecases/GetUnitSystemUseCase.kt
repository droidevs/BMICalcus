package io.droidevs.bmicalc.domain.usecases

import io.droidevs.bmicalc.data.preference.UnitSystemPreference
import io.droidevs.bmicalc.data.model.UnitSystem
import io.droidevs.wallpaper.domain.result.Result
import io.droidevs.wallpaper.domain.result.errors.PreferenceError
import kotlinx.coroutines.flow.Flow

class GetUnitSystemUseCase(
    private val unitSystemPreference: UnitSystemPreference
) {

    fun invoke() : Flow<Result<UnitSystem, PreferenceError>>{
        return unitSystemPreference.getUnitSystem()
    }
}