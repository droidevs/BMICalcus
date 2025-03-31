package io.droidevs.bmicalc.domain.usecases

data class PreferenceUseCases(
    val getTheme : GetThemeUseCase,
    val updateTheme :UpdateThemeUseCase,
    val getUnitSystem : GetUnitSystemUseCase,
    val updateUnitSystem : UpdateUnitSystemUseCase,
    val getLastScore : GetLastSubmittedBmiScoreUseCase,
    val updateLastScore : UpdateLastSubmittedScoreUseCase,
)