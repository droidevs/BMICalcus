package io.droidevs.bmicalc.data.preference.exceptions

import io.droidevs.bmicalc.domain.result.flowRunCatching
import io.droidevs.bmicalc.domain.result.runCatchingResult
import io.droidevs.bmicalc.domain.result.runCatchingWithResult
import io.droidevs.wallpaper.domain.result.Result
import io.droidevs.bmicalc.domain.result.errors.PreferenceError
import kotlinx.coroutines.flow.Flow
import java.io.IOException


suspend fun <T> runCatchingPreference(
    block: suspend () -> T
) : Result<T, PreferenceError> = runCatchingResult(
    errorTransform = { e ->
        transformPreferenceError(e)
    }
) {
   block()
}

suspend fun <T> runCatchingPreferenceWithResult(
    block: suspend () -> Result<T, PreferenceError>
) : Result<T, PreferenceError> = runCatchingWithResult(
    errorTransform = { e ->
        transformPreferenceError(e)
    }
) {
    block()
}

fun <T> flowCatchingPreference(
    block: suspend () -> Flow<T>
) = flowRunCatching(
    errorTransform = { e->
        transformPreferenceError(e)
    }
){
    block()
}

private fun transformPreferenceError(e: Throwable): PreferenceError = when (e) {
    is NoSuchElementException -> PreferenceError.KeyNotFound
    is IOException -> PreferenceError.IOError
    else -> PreferenceError.UnknownError(e)
}