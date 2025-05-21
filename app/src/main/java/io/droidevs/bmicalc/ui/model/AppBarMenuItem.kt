package io.droidevs.bmicalc.ui.model

import androidx.compose.ui.graphics.vector.ImageVector

data class AppBarMenuItem(
    val id: Int,
    val title: String,
    val iconRes: ImageVector,
    val action : Boolean = false
)