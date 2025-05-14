package io.droidevs.bmicalc.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import io.droidevs.bmicalc.model.BMICategory

@Composable
fun BMIScaleIndicator(
    bmiValue: Float,
    modifier : Modifier = Modifier) {
    val categories = listOf(
        Pair(0f..18.5f, BMICategory.UNDERWEIGHT.color),
        Pair(18.5f..25f, BMICategory.NORMAL.color),
        Pair(25f..30f, BMICategory.OVERWEIGHT.color),
        Pair(30f..100f, BMICategory.OBESE.color)
    )
    var barWidth by remember { mutableFloatStateOf(0f) }
    val density = LocalDensity.current
    Box(
        modifier = Modifier
            .onGloballyPositioned {
                barWidth = with(density){
                    it.size.width.toDp().value
                }
            }
            .onSizeChanged {
                val width = with(density){
                    it.width.toDp().value
                }
                if (barWidth != width)
                    barWidth = width
            }
            .fillMaxWidth(0.9f)
            .height(24.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .then(modifier)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            categories.forEach { (range, color) ->
                val weight = (range.endInclusive - range.start) / 100
                Box(
                    modifier = Modifier
                        .weight(weight)
                        .fillMaxHeight()
                        .background(color)
                )
            }
        }

        val indicatorOffset = remember { mutableIntStateOf(0) }

        // BMI Indicator
        val position = minOf(bmiValue / 100f, 1f) // Cap at 40 for display
        Box(
            modifier = Modifier
                .offset(x = (barWidth.dp).times(position * 0.95f))
                .size(24.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface)
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                )
        )
    }
}
