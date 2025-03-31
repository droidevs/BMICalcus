package io.droidevs.bmicalc.data.preference

import io.droidevs.bmicalc.model.Theme
import kotlinx.coroutines.flow.Flow


interface ThemePreference {

    suspend fun changeMode(theme: Theme)

    fun restoreTheme() : Flow<Theme>
}