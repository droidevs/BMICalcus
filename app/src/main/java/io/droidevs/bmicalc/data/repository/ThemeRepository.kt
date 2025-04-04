package io.droidevs.bmicalc.data.repository

import io.droidevs.bmicalc.data.model.Theme
import kotlinx.coroutines.flow.Flow

interface ThemeRepository {
    suspend fun setTheme(theme: Theme)
    fun getTheme(): Flow<Theme>
}
