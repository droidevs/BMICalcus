package io.droidevs.bmicalc.ui.nav.navigators

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import io.droidevs.bmicalc.ui.nav.roots.Destination
import io.droidevs.bmicalc.ui.nav.roots.Graph

class HomeDashboardNavigator(navController: NavHostController,
                             startDestination: Destination
) : DefaultNavigator(navController, startDestination) {

    override fun <T : Destination> navigateTo(destination: T) {
        navController.navigate(destination) {
            // Pop up to start destination (inclusive=false keeps start in stack)
            popUpTo(currentStartDestination) {
                saveState = true
                inclusive = true
            }
            // Avoid multiple copies of same destination
            launchSingleTop = true
            // Restore state if already exists
            restoreState = true
        }
    }
}

@Composable
fun rememberHomeDashboardNavigator(
    navController: NavHostController = rememberNavController(),
    startDestination: Destination = Graph.Home
) : HomeDashboardNavigator {
    return remember(navController,startDestination) {
        HomeDashboardNavigator(
            navController = navController,
            startDestination = startDestination,
        )
    }
}