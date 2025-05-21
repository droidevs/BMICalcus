package io.droidevs.bmicalc.ui.nav.roots



open class Destination

sealed class Screen : Destination(){

    object Favorites: Screen()
    object GoalSetup: Screen()

    data class RecordDetail(val recordId: Long) : Screen()

    data class EditRecord(val recordId: Long) : Screen()

    object History: Screen()

    object Calculator: Screen()

    object Chart : Screen()

    object Settings : Screen()
}