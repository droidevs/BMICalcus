package io.droidevs.bmicalc.ui.layouts

import android.annotation.SuppressLint
import android.os.Build
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowInsetsCompat
import io.droidevs.bmicalc.ui.window.LayoutMode
import io.droidevs.bmicalc.ui.window.LocalWindow

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CompactLayoutWithScaffold(
    mainContent: @Composable () -> Unit,
    allowScroll: Boolean = true,
    applyPadding: Boolean = true,
    topAppBar: @Composable () -> Unit,
    floatingActionButton: @Composable () -> Unit,
    isStandalone: Boolean = true // Added parameter to check standalone mode
) {
    val context = LocalContext.current
    var statusBarHeight by remember { mutableIntStateOf(0) }
    var navigationBarHeight by remember { mutableIntStateOf(0) }

    var topPadding = 0.dp
    var bottomPadding = 0.dp

    // Apply padding only if `isStandalone` is true
    if (isStandalone && applyPadding) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            statusBarHeight = WindowInsetsCompat.toWindowInsetsCompat(
                LocalView.current.rootWindowInsets).getInsets(WindowInsetsCompat.Type.statusBars()).top
            navigationBarHeight = WindowInsetsCompat.toWindowInsetsCompat(
                LocalView.current.rootWindowInsets).getInsets(WindowInsetsCompat.Type.navigationBars()).bottom

            val statusBarHeightPx = statusBarHeight
            val statusBarHeightDp = with(LocalDensity.current) { statusBarHeightPx.toDp() }
            val navigationBarHeightPx = navigationBarHeight
            val navigationBarHeightDp = with(LocalDensity.current) { navigationBarHeightPx.toDp() }

            topPadding = 8.dp + statusBarHeightDp
            bottomPadding = 8.dp + navigationBarHeightDp
        } else {
            topPadding = 16.dp
            bottomPadding = 16.dp
        }
    }

    Scaffold(
        topBar = { topAppBar() },
        floatingActionButton = { floatingActionButton() }
    ) {
        val appLayoutMode = LocalWindow.current.layoutMode

        var sidePadding = 0.dp

        // Apply padding for different layout modes
        if (isStandalone && applyPadding) {
            sidePadding = when (appLayoutMode) {
                LayoutMode.PHONE_LANDSCAPE -> 40.dp
                LayoutMode.NARROW_TABLET -> 40.dp
                else -> 16.dp
            }
        }

        val columnPadding = PaddingValues(
            top = topPadding,
            bottom = bottomPadding,
            start = sidePadding,
            end = sidePadding
        )

        var columnModifier: Modifier = Modifier
            .fillMaxWidth()
            .padding(columnPadding)

        // Apply scrolling behavior
        if (allowScroll) {
            columnModifier = columnModifier.verticalScroll(rememberScrollState())
        }

        // Main content layout
        Column(
            modifier = columnModifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            mainContent()
        }
    }
}
