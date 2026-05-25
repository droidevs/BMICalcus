package io.droidevs.bmicalc.ui.nav.roots

import kotlinx.serialization.Serializable

@Serializable
open class Destination

@Serializable
sealed class Screen : Destination(){

    @Serializable
    object Favorites: Screen()

    @Serializable
    object GoalSetup: Screen()

    @Serializable
    data class RecordDetail(val recordId: Long) : Screen()

    @Serializable
    data class EditRecord(val recordId: Long) : Screen()

    @Serializable
    object History: Screen()

    @Serializable
    object Calculator: Screen()

    @Serializable
    object Chart : Screen()

    @Serializable
    object Settings : Screen()
}
