package io.droidevs.bmicalc.ui.nav.navigators

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import io.droidevs.bmicalc.ui.nav.roots.Destination
import io.droidevs.bmicalc.ui.nav.roots.Screen

class BmiRecordDetailsScreenNavigator(
    navController: NavHostController,
    startDestination: Destination,
    val onPopDetails: () -> Unit
) : DefaultNavigator(
    navController, startDestination
) {

    override fun navigateUp() {
        val currentBackStackEntry = navController.currentBackStackEntry
        val isAtStartDestination = currentBackStackEntry?.destination?.route ==
                navController.graph.startDestinationRoute
        if (isAtStartDestination) {
            onPopDetails()
        } else {
            super.navigateUp()
        }
    }
}

@Composable
fun rememberBmiRecordDetailsScreenNavigator(
    navController: NavHostController = rememberNavController(),
    startDestination: Destination = Screen.RecordDetail(-1),
    onPopDetails: () -> Unit = {}
) : BmiRecordDetailsScreenNavigator {
    return remember {
        BmiRecordDetailsScreenNavigator(
            navController = navController,
            startDestination = startDestination,
            onPopDetails = onPopDetails
        )
    }
}