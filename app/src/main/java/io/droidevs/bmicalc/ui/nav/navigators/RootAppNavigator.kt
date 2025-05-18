package io.droidevs.bmicalc.ui.nav.navigators

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import io.droidevs.bmicalc.ui.nav.roots.Destination
import io.droidevs.bmicalc.ui.nav.roots.Graph
import io.droidevs.bmicalc.ui.nav.roots.Screen

class RootAppNavigator(navController: NavHostController,
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
fun rememberRootNavigator(
    navController: NavHostController = rememberNavController(),
    startDestination: Destination = Graph.Home
) : RootAppNavigator {
    return remember(navController,startDestination) {
        RootAppNavigator(
            navController = navController,
            startDestination = startDestination,
        )
    }
}