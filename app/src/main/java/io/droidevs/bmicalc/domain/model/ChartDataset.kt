package io.droidevs.bmicalc.domain.model

import androidx.compose.ui.graphics.Color

data class ChartDataset(
    val label: String,
    val values: List<DataPoint>,
    val color: Color
)