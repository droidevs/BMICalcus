package io.droidevs.bmicalc.domain.preference


import io.droidevs.bmicalc.data.model.UnitSystem
import io.droidevs.wallpaper.domain.result.Result
import io.droidevs.bmicalc.domain.result.errors.PreferenceError
import kotlinx.coroutines.flow.Flow

interface UnitSystemPreference {

    suspend fun saveUnitSystem(unitSystem: UnitSystem) : Result<UnitSystem, PreferenceError>

    fun getUnitSystem() : Flow<Result<UnitSystem, PreferenceError>>

}