package io.droidevs.bmicalc.ui.layouts


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.droidevs.bmicalc.ui.window.LayoutMode
import io.droidevs.bmicalc.ui.window.LocalWindow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoubleLayoutWithScaffold(
    leftContent: @Composable () -> Unit,
    rightContent: @Composable () -> Unit,
    topAppBar: @Composable () -> Unit,
    isStandalone: Boolean = true // Determines whether to apply external padding
) {
    val layoutMode = LocalWindow.current.layoutMode
    val sidePadding = when (layoutMode) {
        LayoutMode.DOUBLE_BIG -> 52.dp
        LayoutMode.FOLDED_SPLIT_BOOK -> 16.dp
        else -> 16.dp
    }

    Scaffold(
        topBar = { topAppBar() }
    ) { paddingValues ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .then(if (isStandalone) Modifier.padding(paddingValues) else Modifier) // Apply external padding if standalone
        ) {
            Column(
                modifier = Modifier
                    .weight(0.5f)
                    .padding(end = sidePadding) // Keep internal padding
            ) {
                leftContent()
            }

            Column(
                modifier = Modifier
                    .weight(0.5f)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f))
                    .padding(start = sidePadding) // Keep internal padding
            ) {
                rightContent()
            }
        }
    }
}
