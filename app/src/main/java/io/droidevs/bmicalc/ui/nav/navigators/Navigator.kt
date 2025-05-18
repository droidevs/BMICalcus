package io.droidevs.bmicalc.ui.nav.navigators

import androidx.navigation.NavController
import io.droidevs.bmicalc.ui.nav.roots.Destination

interface Navigator {

    val currentStartDestination: Destination

    fun <T : Destination> navigateTo(destination: T)

    fun resetToStart()

    fun navigateUp()

}