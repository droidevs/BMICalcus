package io.droidevs.bmicalc.ui.snackbar

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

object SnackBarController {

    private val _events = Channel<SnackBarEvent>()

    val events = _events.receiveAsFlow()

    suspend fun sendEvent(event: SnackBarEvent){
        _events.send(event)
    }
}