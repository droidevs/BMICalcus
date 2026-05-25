package io.droidevs.bmicalc.domain.usecases

import io.droidevs.bmicalc.domain.preference.ThemePreference
import io.droidevs.bmicalc.data.model.Theme
import io.droidevs.bmicalc.domain.result.errors.PreferenceError
import io.droidevs.wallpaper.domain.result.Result

class UpdateThemeUseCase(
    private val preference : ThemePreference
) {

    suspend fun invoke(theme: Theme): Result<Theme, PreferenceError> {
        return preference.changeMode(theme)
    }
}
