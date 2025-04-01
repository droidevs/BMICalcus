package io.droidevs.bmicalc.ui.helper

interface ActionHandler<Action : UiAction> {
    suspend fun onAction(action : Action)

}