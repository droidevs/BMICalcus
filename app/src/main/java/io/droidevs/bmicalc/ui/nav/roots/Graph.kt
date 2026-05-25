package io.droidevs.bmicalc.ui.nav.roots

import kotlinx.serialization.Serializable

@Serializable
sealed class Graph : Destination() {

    @Serializable
    data object Home : Graph()

    @Serializable
    data object Chart : Graph()

    @Serializable
    data object History : Graph()
}
