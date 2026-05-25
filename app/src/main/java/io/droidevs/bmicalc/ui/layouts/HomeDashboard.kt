package io.droidevs.bmicalc.ui.layouts


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import io.droidevs.bmicalc.R
import io.droidevs.bmicalc.ui.components.AppNavRail
import io.droidevs.bmicalc.ui.components.BottomNavigationBar
import io.droidevs.bmicalc.ui.components.DrawerController
import io.droidevs.bmicalc.ui.components.NavigationDrawer
import io.droidevs.bmicalc.ui.model.NavigationItem
import io.droidevs.bmicalc.ui.nav.navigators.HomeDashboardNavigator
import io.droidevs.bmicalc.ui.nav.navigators.rememberHomeDashboardNavigator
import io.droidevs.bmicalc.ui.nav.roots.Graph
import io.droidevs.bmicalc.ui.nav.chartScreen
import io.droidevs.bmicalc.ui.nav.editRecordScreen
import io.droidevs.bmicalc.ui.nav.historyScreen
import io.droidevs.bmicalc.ui.nav.homeNavHost
import io.droidevs.bmicalc.ui.nav.recordDetailScreen
import io.droidevs.bmicalc.ui.nav.roots.Screen
import io.droidevs.bmicalc.ui.window.LocalWindow
import kotlinx.coroutines.launch


@Composable
fun HomeDashboard() {

    val layoutMode = LocalWindow.current.layoutMode

    val navigator = rememberHomeDashboardNavigator()
    val navBackStackEntry by navigator.navController.currentBackStackEntryAsState()
    val drawerController = remember { DrawerController() }
    val scope = rememberCoroutineScope()
    val toggleDrawer: () -> Unit = {
        scope.launch {
            if (drawerController.isOpen()) {
                drawerController.close()
            } else {
                drawerController.open()
            }
        }
        Unit
    }

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
            val route = navBackStackEntry?.destination?.route.orEmpty()
            when {
                route.contains(Graph.Home::class.qualifiedName.orEmpty()) -> navigationItems[0]
                route.contains(Graph.Chart::class.qualifiedName.orEmpty()) -> navigationItems[1]
                route.contains(Graph.History::class.qualifiedName.orEmpty()) -> navigationItems[2]
                else -> navigationItems[0]
            }
        }
    }

    if (layoutMode.showNavDrawer()){
        NavigationDrawer(
            items = navigationItems,
            onItemClick = { item ->
                navigator.navigateTo(item.root)
            },
            selectedItem = selectedItem.id,
            drawerController = drawerController
        ){
            DashboardMainContent(navigator, toggleDrawer)
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
                        onClick = { item ->
                            navigator.navigateTo(item.root)
                        },
                        selectedItem = selectedItem.id
                    )
                    DashboardMainContent(navigator, toggleDrawer)
                }
            } else {
                DashboardMainContent(navigator, toggleDrawer)
            }
        }
    }
}

@Composable
fun DashboardMainContent(
    navigator: HomeDashboardNavigator,
    toggleDrawer: () -> Unit
){
    NavHost (
        navController = navigator.navController,
        startDestination = navigator.startDestination
    ){
        homeNavHost(
            gotoSettings = {},
            toggleDrawer = toggleDrawer
        )
        historyScreen(
            navigator = navigator,
            onDrawerMenuClick = toggleDrawer,
            onSettingsClick = {}
        )
        chartScreen()
        recordDetailScreen(navigator)
        editRecordScreen(navigator)

    }
}

