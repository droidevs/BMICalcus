package io.droidevs.bmicalc.data.repository

import io.droidevs.bmicalc.data.model.Theme
import io.droidevs.bmicalc.data.preference.ThemePreference
import kotlinx.coroutines.flow.*

class ThemeRepositoryImpl(
    private val preference: ThemePreference
) : ThemeRepository {


    override fun getTheme(): Flow<Theme> {
        return preference.restoreTheme()
    }

    override suspend fun setTheme(theme: Theme) {
        preference.changeMode(theme)
    }
}
