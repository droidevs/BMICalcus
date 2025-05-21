package io.droidevs.bmicalc.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid

import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import io.droidevs.bmicalc.ui.helper.actions.HistoryScreenAction
import io.droidevs.bmicalc.ui.components.AnimatedAppBar
import io.droidevs.bmicalc.ui.components.BmiRecordCardWithActions
import io.droidevs.bmicalc.ui.components.DirectionMode
import io.droidevs.bmicalc.ui.components.PlaceholderCard
import io.droidevs.bmicalc.ui.components.PullToRefresh
import io.droidevs.bmicalc.ui.components.shrimmerCount
import io.droidevs.bmicalc.ui.helper.states.HistoryState
import io.droidevs.bmicalc.ui.layouts.CompactLayoutWithScaffold
import io.droidevs.bmicalc.ui.layouts.DoubleLayoutWithScaffold
import io.droidevs.bmicalc.ui.model.BmiRecordUi
import io.droidevs.bmicalc.ui.model.LoadingMode
import io.droidevs.bmicalc.ui.model.RevealState
import io.droidevs.bmicalc.ui.nav.editRecordScreen
import io.droidevs.bmicalc.ui.nav.navigators.rememberBmiRecordDetailsScreenNavigator
import io.droidevs.bmicalc.ui.nav.recordDetailScreen
import io.droidevs.bmicalc.ui.window.LocalWindow

@Composable
fun BmiHistoryScreen(
    state: HistoryState,
    onAction: (HistoryScreenAction) -> Unit,
    onSettingsClick: () -> Unit,
    onDrawerMenuClick: () -> Unit
) {
    val layoutMode = LocalWindow.current.layoutMode
    if (layoutMode.isSplitScreen()) {
        var clickedRecord by remember { mutableStateOf<BmiRecordUi?>(null) }
        clickedRecord?.let {
            DoubleLayoutWithScaffold(
                leftContent = {
                    PortraitHistoryScreen(
                        state = state,
                        onAction = onAction,
                        onMenuClick = onDrawerMenuClick,
                        onSettingsClick = onSettingsClick
                    )
                },
                rightContent = {
                    val navigator = rememberBmiRecordDetailsScreenNavigator(
                        onPopDetails = {
                            clickedRecord = null
                        }
                    )
                    NavHost(
                        navController = navigator.navController,
                        startDestination = navigator.startDestination,
                    ) {
                        recordDetailScreen(navigator)
                        editRecordScreen(navigator)
                    }
                },
            )
        } ?: PortraitHistoryScreen(
            state = state,
            onAction = { action ->
                if (action is HistoryScreenAction.ClickRecord){
                    clickedRecord = action.record
                }else{
                    onAction(action)
                }
            },
            onMenuClick = onDrawerMenuClick,
            onSettingsClick = onSettingsClick
        )
    } else {
        PortraitHistoryScreen(
            state = state,
            onAction = onAction,
            onMenuClick = onDrawerMenuClick,
            onSettingsClick = onSettingsClick
        )
    }
}

@Composable
private fun HistoryScreenAppBar(
    onMenuClick: () -> Unit,         // Delegate drawer control to parent
    onSettingsClick: () -> Unit,      // Delegate settings navigation
    onFavoriteClick: () -> Unit,      // Delegate favorite control to parent
) {
    AnimatedAppBar(
        title = "BMI History",
        actions = {
            // Settings action
            IconButton(
                onClick = onFavoriteClick,
                modifier = Modifier.padding(end = 12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Favorite"
                )
            }
            IconButton(
                onClick = onSettingsClick,
                modifier = Modifier.padding(end = 12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings"
                )
            }
        },
        navigationIcon = {
            // Drawer menu control
            IconButton(
                onClick = onMenuClick,
                modifier = Modifier.padding(start = 12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Open navigation drawer"
                )
            }
        }
    )
}

@Composable
private fun PortraitHistoryScreen(
    state : HistoryState,
    onAction: (HistoryScreenAction) -> Unit,
    isStandAlone: Boolean = true,
    onMenuClick: () -> Unit,
    onSettingsClick: () -> Unit,
){
    CompactLayoutWithScaffold(
        topAppBar = {
            HistoryScreenAppBar(
                onMenuClick = onMenuClick,
                onSettingsClick = onSettingsClick,
                onFavoriteClick = {
                    onAction(HistoryScreenAction.OnFavoriteClicked)
                }
            )
        },
        floatingActionButton = {

        },
        isStandalone = isStandAlone,
    ) {
        var revealedState by remember { mutableStateOf(RevealState()) }
        var layoutMode = LocalWindow.current.layoutMode
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize()
        ) {
            val minCellWidth by remember(maxWidth) {
                mutableStateOf(
                    when {
                        maxWidth < 400.dp -> 160.dp
                        maxWidth < 640.dp -> 200.dp
                        maxWidth < 860.dp -> 250.dp
                        maxWidth < 1080.dp -> 300.dp
                        else -> 400.dp
                    }
                )
            }

            // Aspect ratio for shimmer item (adjust as needed)
            val shimmerAspectRatio = 4f / 3f
            val minShimmerHeight = 160.dp
            val shimmerItemHeight = (minCellWidth / shimmerAspectRatio).coerceAtLeast(minShimmerHeight)

            // Calculate number of shimmer placeholders
            val numColumns = (maxWidth / minCellWidth).toInt().coerceAtLeast(1)
            val numShimmers = shrimmerCount(
                contentHeight = maxHeight,
                shrimmerHeight = shimmerItemHeight,
                cols = numColumns
            )

            PullToRefresh(
                isRefresh = state.mode == LoadingMode.Refresh,
                onRefresh = { onAction(HistoryScreenAction.Refresh) },
            ) {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Adaptive(minCellWidth),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalItemSpacing = 16.dp
                ) {
                    if (state.mode == LoadingMode.Refresh) {
                        // Show shimmer items
                        items(numShimmers) {
                            PlaceholderCard(
                                ratio = shimmerAspectRatio,
                                minHeight = minShimmerHeight
                            )
                        }
                    } else {
                        val records = state.records
                        items(records.size) { index ->
                            val item = records[index]
                            Box(
                                modifier = Modifier.animateItem()
                            ) {
                                BmiRecordCardWithActions(
                                    revealedDirection = if (revealedState.id == item.id) revealedState.direction else DirectionMode.None,
                                    record = item,
                                    onDelete = { onAction(HistoryScreenAction.Delete(item)) },
                                    onFavorite = { onAction(HistoryScreenAction.Favorite(item)) },
                                    onClick = { onAction(HistoryScreenAction.ClickRecord(item)) },
                                    onExpanded = {
                                        revealedState =
                                            revealedState.copy(id = item.id, direction = it)
                                    },
                                    onCollapse = {
                                        if (revealedState.id == item.id)
                                            revealedState = revealedState.copy(
                                                id = -1,
                                                direction = DirectionMode.None
                                            )
                                    },
                                    unitSystem = state.unitSystem,
                                )
                            }
                        }

                        // Add loading indicator at the bottom if loading more items
                        if (state.mode == LoadingMode.Append) {
                            item(span = StaggeredGridItemSpan.FullLine) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun LandscapeDynamicFeed(
    state: HistoryState,
    onClick: (BmiRecordUi) -> Unit,
    onAction: (HistoryScreenAction) -> Unit,
) {
    val revealedState by remember { mutableStateOf(RevealState()) }
    val refreshState = rememberPullToRefreshState()

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val minCellHeight by remember(maxHeight) {
            mutableStateOf(
                when {
                    maxHeight < 300.dp -> 120.dp
                    maxHeight < 480.dp -> 160.dp
                    maxHeight < 640.dp -> 200.dp
                    maxHeight < 800.dp -> 250.dp
                    else -> 300.dp
                }
            )
        }

        // Aspect ratio for shimmer item (adjust as needed)
        val shimmerAspectRatio = 3f / 4f // (Portrait items in landscape layout)
        val minShimmerWidth = 120.dp
        val shimmerItemWidth = (minCellHeight * shimmerAspectRatio).coerceAtLeast(minShimmerWidth)

        // Calculate number of shimmer placeholders
        val numRows = (maxHeight / minCellHeight).toInt().coerceAtLeast(1)
        val numShimmers = shrimmerCount(
            contentHeight = maxWidth,
            shrimmerHeight = shimmerItemWidth, // Width in horizontal layout
            cols = numRows // Rows instead of columns
        )

        PullToRefresh(
            isRefresh = state.mode == LoadingMode.Refresh,
            onRefresh = { onAction(HistoryScreenAction.Refresh) },
        ) {
            LazyHorizontalStaggeredGrid(
                rows = StaggeredGridCells.Adaptive(minCellHeight),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalItemSpacing = 16.dp
            ) {
                if (state.mode == LoadingMode.Refresh) {
                    // Show shimmer items
                    items(numShimmers) {
                        PlaceholderCard(
                            ratio = shimmerAspectRatio,
                            minHeight = minCellHeight
                        )
                    }
                } else {
                    val records = state.records
                    items(records.size) { index ->
                        val item = records[index]
                        Box(
                            modifier = Modifier.animateItem()
                        ) {
                            BmiRecordCardWithActions(
                                revealedDirection = if (revealedState.id == item.id) revealedState.direction else DirectionMode.None,
                                record = item,
                                onDelete = { onAction(HistoryScreenAction.Delete(item)) },
                                onFavorite = { onAction(HistoryScreenAction.Favorite(item)) },
                                onClick = { onClick(item) },
                                onExpanded = {
                                    revealedState.copy(id = item.id, direction = it)
                                },
                                onCollapse = {
                                    if (revealedState.id == item.id)
                                        revealedState.copy(id = -1, direction = DirectionMode.None)
                                },
                                unitSystem = state.unitSystem,
                            )
                        }
                    }

                    // Add loading indicator at the end if loading more items
                    if (state.mode == LoadingMode.Append) {
                        item(span = StaggeredGridItemSpan.FullLine) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }
    }
}



