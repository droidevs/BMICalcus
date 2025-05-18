package io.droidevs.bmicalc.ui.model

import androidx.compose.ui.graphics.vector.ImageVector
import io.droidevs.bmicalc.ui.nav.roots.Destination


data class NavigationItem(
    val id: Int,
    val title : String,
    val selectedIcon : ImageVector,
    val unselectedIcon : ImageVector,
    var root : Destination
)