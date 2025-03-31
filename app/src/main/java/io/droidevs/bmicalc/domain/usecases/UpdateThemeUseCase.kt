package io.droidevs.bmicalc.domain.usecases

import io.droidevs.bmicalc.data.preference.ThemePreference
import io.droidevs.bmicalc.model.Theme

class UpdateThemeUseCase(
    private val preference : ThemePreference
) {

    suspend fun invoke(theme: Theme) {
        preference.changeMode(theme)
    }
}