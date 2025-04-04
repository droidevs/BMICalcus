package io.droidevs.bmicalc.domain.usecases

import io.droidevs.bmicalc.data.preference.UnitSystemPreference
import io.droidevs.bmicalc.data.model.UnitSystem
import kotlinx.coroutines.flow.Flow

class GetUnitSystemUseCase(
    private val unitSystemPreference: UnitSystemPreference
) {

    fun invoke() : Flow<UnitSystem>{
        return unitSystemPreference.restoreUnitSystem()
    }
}