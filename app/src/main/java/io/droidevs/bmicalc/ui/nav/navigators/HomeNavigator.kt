package io.droidevs.bmicalc.ui.nav.navigators

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import io.droidevs.bmicalc.ui.nav.roots.Destination
import io.droidevs.bmicalc.ui.nav.roots.Graph

class HomeNavigator(
    navController: NavHostController,
    startDestination: Destination) : DefaultNavigator( navController, startDestination) {

}

@Composable
fun rememberHomeNavigator(
    navController: NavHostController = rememberNavController(),
    startDestination: Destination = Graph.Home
) : HomeNavigator {
    return remember(navController,startDestination) {
        HomeNavigator(
            navController = navController,
            startDestination = startDestination,
        )
    }
}