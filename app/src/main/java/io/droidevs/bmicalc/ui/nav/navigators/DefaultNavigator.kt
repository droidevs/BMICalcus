package io.droidevs.bmicalc.ui.nav.navigators


import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import io.droidevs.bmicalc.ui.nav.roots.Destination
import io.droidevs.bmicalc.ui.nav.roots.Screen

open class DefaultNavigator(
    val navController: NavHostController,
    val startDestination: Destination,
) : Navigator {
    final override var currentStartDestination by mutableStateOf(startDestination)
        private set

    // Navigate to a screen while maintaining back stack
    override fun <T : Destination> navigateTo(destination: T) {
        navController.navigate(destination ?: error("Unknown destination"))
    }

    // Reset to start destination
    override fun resetToStart() {
        navController.popBackStack(
            currentStartDestination,
            inclusive = false
        )
    }

    override fun navigateUp() {
        navController.navigateUp()
    }

    // Change the active start destination (for multi-graph apps)
    fun <T : Destination> updateStartDestination(newStart: T) {
        currentStartDestination = newStart
        resetToStart()
    }
}

