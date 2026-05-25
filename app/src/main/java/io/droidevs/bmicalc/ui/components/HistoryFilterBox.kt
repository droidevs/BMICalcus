package io.droidevs.bmicalc.ui.components

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.droidevs.bmicalc.R
import io.droidevs.bmicalc.domain.Range
import io.droidevs.bmicalc.domain.model.BmiFilter
import io.droidevs.bmicalc.domain.model.TimeRange
import io.droidevs.bmicalc.data.model.UnitSystem


@SuppressLint("UnusedTransitionTargetStateParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BmiHistoryFilter(
    unitSystem: UnitSystem,
    selectedFilter: BmiFilter?,
    onFilterChange: (BmiFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    // Tab state management
    var selectedTab by remember { mutableStateOf(FilterType.HEIGHT) }
    val tabs = remember { FilterType.values().toList() }

    // Animation states
    val transition = updateTransition(selectedTab, label = "tabTransition")
    val contentAlpha by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 200) },
        label = "contentAlpha"
    ) { 1f }
    val contentOffset by transition.animateDp(
        transitionSpec = { tween(durationMillis = 300) },
        label = "contentOffset"
    ) { 0.dp }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header with tabs
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_filter_edit),
                    contentDescription = "Filter",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Filter Records",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Animated tabs
            ScrollableTabRow(
                selectedTabIndex = tabs.indexOf(selectedTab),
                edgePadding = 0.dp,
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.primary,
                divider = {},
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[tabs.indexOf(selectedTab)])
                            .padding(horizontal = 16.dp),
                        height = 3.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            ) {
                tabs.forEach { tab ->
                    val selected = tab == selectedTab
                    val tabColor by animateColorAsState(
                        targetValue = if (selected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                        animationSpec = tween(durationMillis = 200),
                        label = "tabColor"
                    )

                    Tab(
                        selected = selected,
                        onClick = { selectedTab = tab },
                        text = {
                            AnimatedContent(
                                targetState = selected,
                                transitionSpec = {
                                    fadeIn() + scaleIn() togetherWith fadeOut() + scaleOut()
                                },
                                label = "tabTextAnimation"
                            ) { isSelected ->
                                Text(
                                    text = when (tab) {
                                        FilterType.HEIGHT -> "Height"
                                        FilterType.WEIGHT -> "Weight"
                                        FilterType.TIME -> "Time"
                                    },
                                    style = if (isSelected) MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                                    else MaterialTheme.typography.labelLarge,
                                    color = tabColor,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                        },
                        selectedContentColor = MaterialTheme.colorScheme.primary,
                        unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Animated content area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 200.dp)
                    .graphicsLayer {
                        alpha = contentAlpha
                        translationY = contentOffset.toPx()
                    }
            ) {
                AnimatedContent(
                    targetState = selectedTab,
                    transitionSpec = {
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Up,
                            animationSpec = tween(durationMillis = 300)
                        ) togetherWith
                                slideOutOfContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Down,
                                    animationSpec = tween(durationMillis = 300)
                                )
                    },
                    label = "filterContent"
                ) { tab ->
                    when (tab) {
                        FilterType.HEIGHT -> {
                            HeightRangeFilterSection(
                                unitSystem = unitSystem,
                                selectedRange = selectedFilter?.height,
                                onRangeChange = { range ->
                                    onFilterChange(
                                        buildFilter(
                                            base = selectedFilter,
                                            height = range
                                        )
                                    )
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        FilterType.WEIGHT -> {
                            WeightRangeFilterSection(
                                unitSystem = unitSystem,
                                selectedRange = selectedFilter?.weight,
                                onRangeChange = { range ->
                                    onFilterChange(
                                        buildFilter(
                                            base = selectedFilter,
                                            weight = range
                                        )
                                    )
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        FilterType.TIME -> {
                            TimeRangeSelector(
                                range = selectedFilter?.time,
                                onRangeSelected = { range ->
                                    onFilterChange(
                                        buildFilter(
                                            base = selectedFilter,
                                            time = range
                                        )
                                    )
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun buildFilter(
    base: BmiFilter?,
    height: Range? = base?.height,
    weight: Range? = base?.weight,
    time: TimeRange? = base?.time
): BmiFilter {
    return BmiFilter(
        time = time,
        bmi = base?.bmi,
        weight = weight,
        height = height,
        order = base?.order
    )
}

private enum class FilterType {
    HEIGHT,
    WEIGHT,
    TIME
}

