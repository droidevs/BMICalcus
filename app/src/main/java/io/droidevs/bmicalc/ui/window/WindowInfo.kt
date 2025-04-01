package io.droidevs.bmicalc.ui.window


import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp



@Stable
data class WindowInfo(
    var windowSize: WindowSize = WindowSize(0.dp,0.dp),
    var foldableInfo: FoldableInfo? = null
){

    val layoutMode : LayoutMode
        get() {
            return getWindowLayoutMode(windowSize,foldableInfo)
        }
}


@Composable
fun rememberWindowInfo(): WindowInfo {
    val windowInfo = LocalWindow.current
    return remember { windowInfo }
}