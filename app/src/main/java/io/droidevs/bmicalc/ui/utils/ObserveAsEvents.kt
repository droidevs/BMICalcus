package io.droidevs.bmicalc.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

@Composable
fun <T> ObserveAsEvents(
    flow : Flow<T>,
    key1: Any? = null,
    key2: Any? = null,
    key3: Any? = null,
    onEvent: suspend (T) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(
        lifecycleOwner.lifecycle, key1, key2, key3
    ) {
        lifecycleOwner.repeatOnLifecycle(
            state = Lifecycle.State.STARTED
        ) {
            withContext(Dispatchers.Main.immediate) {
                flow.collect {
                    onEvent(it)
                }
            }
        }
    }
}

@Composable
fun <T> ObserveAsEventsCompose(
    flow: Flow<T>,
    key1: Any? = null,
    key2: Any? = null,
    key3: Any? = null,
    onEvent: @Composable (T) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    var latestEvent by rememberSaveable { mutableStateOf<T?>(null) }

    LaunchedEffect(lifecycleOwner.lifecycle, key1, key2, key3) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                flow.collect { event ->
                    latestEvent = event
                }
            }
        }
    }

    // Handle the event in composition context
    latestEvent?.let { event ->
        onEvent(event)
        // Reset after handling to prevent reprocessing
        latestEvent = null
    }
}