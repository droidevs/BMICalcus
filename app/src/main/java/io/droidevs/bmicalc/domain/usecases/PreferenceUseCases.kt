package io.droidevs.bmicalc.domain.usecases

import io.droidevs.bmicalc.domain.usecases.unitsystem.GetUnitSystemUseCase
import io.droidevs.bmicalc.domain.usecases.unitsystem.UpdateUnitSystemUseCase

data class PreferenceUseCases(
    val getTheme : GetThemeUseCase,
    val updateTheme :UpdateThemeUseCase,
    val getUnitSystem : GetUnitSystemUseCase,
    val updateUnitSystem : UpdateUnitSystemUseCase,
    val getLastScore : GetBmiScoreUseCase,
    val updateLastScore : UpdateLastSubmittedScoreUseCase,
)