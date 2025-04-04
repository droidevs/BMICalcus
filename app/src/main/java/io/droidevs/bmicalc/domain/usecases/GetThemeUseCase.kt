package io.droidevs.bmicalc.domain.usecases


import io.droidevs.bmicalc.data.preference.ThemePreference
import io.droidevs.bmicalc.data.model.Theme
import kotlinx.coroutines.flow.Flow

class GetThemeUseCase(
    private val preference: ThemePreference
) {
    fun invoke() : Flow<Theme>{
        return preference.restoreTheme()
    }
}