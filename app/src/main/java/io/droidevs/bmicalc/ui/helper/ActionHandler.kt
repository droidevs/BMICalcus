package io.droidevs.bmicalc.ui.helper

interface ActionHandler<Action : UiAction> {
    fun onAction(action : Action)

}