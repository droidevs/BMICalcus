package io.droidevs.bmicalc.ui.layouts


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import io.droidevs.bmicalc.R
import io.droidevs.bmicalc.ui.components.AppNavRail
import io.droidevs.bmicalc.ui.components.BottomNavigationBar
import io.droidevs.bmicalc.ui.components.NavigationDrawer
import io.droidevs.bmicalc.ui.model.NavigationItem
import io.droidevs.bmicalc.ui.nav.navigators.DefaultNavigator
import io.droidevs.bmicalc.ui.nav.navigators.Navigator
import io.droidevs.bmicalc.ui.nav.navigators.RootAppNavigator
import io.droidevs.bmicalc.ui.nav.navigators.rememberRootNavigator
import io.droidevs.bmicalc.ui.nav.roots.Graph
import io.droidevs.bmicalc.ui.window.LocalWindow


@Composable
fun HomeDashboard(navController: NavController) {

    val layoutMode = LocalWindow.current.layoutMode

    val navigator = rememberRootNavigator()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val navigationItems: List<NavigationItem> = listOf(
        NavigationItem(
            id = 1,
            title = "BMI",
            selectedIcon = ImageVector.vectorResource(R.drawable.ic_calc_filled),
            unselectedIcon = ImageVector.vectorResource(R.drawable.ic_calc),
            root = Graph.Home
        ),
        NavigationItem(
            id = 2,
            title = "Chart",
            selectedIcon = ImageVector.vectorResource(R.drawable.ic_chart_filled),
            unselectedIcon = ImageVector.vectorResource(R.drawable.ic_chart),
            root = Graph.Chart
        ),
        NavigationItem(
            id = 3,
            title = "History",
            selectedIcon = ImageVector.vectorResource(R.drawable.ic_history_filled),
            unselectedIcon = ImageVector.vectorResource(R.drawable.ic_history),
            root = Graph.History
        )
    )

    val selectedItem by remember {
        derivedStateOf {
            if (navBackStackEntry?.destination?.route == "bmi")
                navigationItems[0]
            else if (navBackStackEntry?.destination?.route == "chart")
                navigationItems[1]
            else if (navBackStackEntry?.destination?.route == "history")
                navigationItems[2]
            else
                navigationItems[0]
        }
    }

    if (layoutMode.showNavDrawer()){
        NavigationDrawer(
            items = navigationItems,
            onItemClick = { item ->
                navigator.navigateTo(item.root)
            },
            selectedItem = selectedItem.id
        ){
            DashboardMainContent(navigator)
        }
    }
    else {
        Scaffold(
            bottomBar = {
                if (layoutMode.showBottomNav()) {
                    BottomNavigationBar(
                        navigationItems = navigationItems,
                        onNavigateTo = { item->
                           navigator.navigateTo(item.root)
                        },
                        selected = selectedItem.id
                    )
                }
            }
        ) { padding ->
            if (layoutMode.showNavRail()) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    AppNavRail(
                        items = navigationItems,
                        onClick = {
                            TODO()
                        },
                        selectedItem = selectedItem.id
                    )
                    DashboardMainContent(navigator)
                }
            } else {
                DashboardMainContent(navigator)
            }
        }
    }
}

@Composable
fun DashboardMainContent(navigator: RootAppNavigator){
    NavHost (
        navController = navigator.navController,
        startDestination = navigator.startDestination
    ){
        composable<Graph.Home> {

        }
        composable<Graph.History> {

        }
        composable<Graph.Chart> {

        }

    }
}

