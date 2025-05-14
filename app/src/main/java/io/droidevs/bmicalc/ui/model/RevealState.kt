package io.droidevs.bmicalc.ui.model

import androidx.compose.runtime.Stable
import io.droidevs.bmicalc.ui.components.DirectionMode


@Stable
data class RevealState(
    val id: Long = -1,
    val direction: DirectionMode = DirectionMode.None
)