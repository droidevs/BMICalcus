package io.droidevs.bmicalc.domain.usecases


import io.droidevs.bmicalc.data.model.Theme
import io.droidevs.bmicalc.domain.preference.ThemePreference
import io.droidevs.bmicalc.domain.result.errors.PreferenceError
import io.droidevs.wallpaper.domain.result.Result
import kotlinx.coroutines.flow.Flow

class GetThemeUseCase(
    private val preference: ThemePreference
) {
    fun invoke(): Flow<Result<Theme, PreferenceError>> = preference.restoreTheme()
}
