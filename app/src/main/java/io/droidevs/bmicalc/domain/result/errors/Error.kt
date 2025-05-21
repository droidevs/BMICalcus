package io.droidevs.bmicalc.domain.result.errors

import io.droidevs.wallpaper.domain.result.RootError

sealed interface Error


data class InternalError(val message: String) : RootError

data class UnknownError(val message: String) : RootError