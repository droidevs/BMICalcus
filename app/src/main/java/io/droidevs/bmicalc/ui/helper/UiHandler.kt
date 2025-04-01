package io.droidevs.bmicalc.ui.helper

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

open class UiHandler<S : UiState, A: UiEvent>(
    private val defaultstate : S
) {
    private val _state = MutableStateFlow<S>(defaultstate)
    val state: StateFlow<S> = _state.asStateFlow()

    private val _event = MutableSharedFlow<A>()
    val event: SharedFlow<A> = _event.asSharedFlow()

    fun updateState(newState: S) {
        _state.value = newState
    }

    suspend fun sendEvent(event: A) {
        _event.emit(event)
    }
}
