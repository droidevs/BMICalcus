package io.droidevs.bmicalc.data.preference


import io.droidevs.bmicalc.data.model.UnitSystem
import io.droidevs.wallpaper.domain.result.Result
import io.droidevs.wallpaper.domain.result.errors.PreferenceError
import kotlinx.coroutines.flow.Flow

interface UnitSystemPreference {

    suspend fun saveUnitSystem(unitSystem: UnitSystem) : Result<UnitSystem, PreferenceError>

    fun getUnitSystem() : Flow<Result<UnitSystem, PreferenceError>>

}