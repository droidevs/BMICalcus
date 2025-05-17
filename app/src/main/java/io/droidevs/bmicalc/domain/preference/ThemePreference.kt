package io.droidevs.bmicalc.domain.preference

import io.droidevs.bmicalc.data.model.Theme
import io.droidevs.wallpaper.domain.result.Result
import io.droidevs.wallpaper.domain.result.errors.PreferenceError
import kotlinx.coroutines.flow.Flow


interface ThemePreference {

    suspend fun changeMode(theme: Theme): Result<Theme, PreferenceError>

    fun restoreTheme() : Flow<Result<Theme, PreferenceError>>
}