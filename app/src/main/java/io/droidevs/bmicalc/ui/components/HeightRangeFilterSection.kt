package io.droidevs.bmicalc.ui.components


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import io.droidevs.bmicalc.R
import io.droidevs.bmicalc.domain.Range
import io.droidevs.bmicalc.data.model.HeightUnit
import io.droidevs.bmicalc.data.model.UnitSystem


@Composable
fun HeightRangeFilterSection(
    unitSystem: UnitSystem,
    selectedRange: Range?,
    onRangeChange: (Range?) -> Unit,
    modifier: Modifier = Modifier
) {
    val heightUnit = remember(unitSystem) { HeightUnit.getUnit(unitSystem) }

    // Track previous unit system for conversion
    var prevUnit by remember { mutableStateOf(HeightUnit.getUnit(unitSystem)) }

    var expanded by remember { mutableStateOf(false) }

    // Convert range to current units or use default range
    val (currentRange, setCurrentRange) = remember(unitSystem) {
        val convertedRange = selectedRange?.let { range ->
            Range(
                start = prevUnit.convert(range.start, unitSystem),
                end = prevUnit.convert(range.end,unitSystem)
            )
        }

        prevUnit = HeightUnit.getUnit(unitSystem) // Update previous unit system

        mutableStateOf(convertedRange ?: heightUnit.defaultRange)
    }
    // Handle external range changes (e.g., from clear action)
    LaunchedEffect(selectedRange) {
        if (selectedRange == null) {
            setCurrentRange(heightUnit.defaultRange)
        }
    }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_height),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )

                Text(
                    text = "Height Range",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f).padding(start = 12.dp)
                )
                Badge(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ) {
                    Text(
                        text = if (selectedRange == null) "Any"
                        else "%.1f - %.1f ${heightUnit.text}".format(
                            currentRange.start,
                            currentRange.end
                        ),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }

            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column {
                    // Visual ruler representation
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.surfaceVariant,
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                                        MaterialTheme.colorScheme.surfaceVariant
                                    )
                                )
                            )
                    ) {
                        // Ruler marks
                        repeat(10) { i ->
                            val position = i / 9f
                            val markValue = lerp(
                                heightUnit.validRange.start,
                                heightUnit.validRange.endInclusive,
                                position
                            )

                            Box(
                                modifier = Modifier
                                    .offset(x = (position * LocalConfiguration.current.screenWidthDp).dp)
                                    .width(1.dp)
                                    .height(if (i % 2 == 0) 24.dp else 16.dp)
                                    .background(MaterialTheme.colorScheme.onSurfaceVariant)
                                    .align(Alignment.BottomStart)
                            )

                            if (i % 2 == 0) {
                                Text(
                                    text = "%.0f".format(markValue),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier
                                        .offset(
                                            x = (position * LocalConfiguration.current.screenWidthDp).dp,
                                            y = 28.dp
                                        )
                                )
                            }
                        }

                        // Range indicators
                        val startPos = (currentRange.start - heightUnit.validRange.start) /
                                (heightUnit.validRange.endInclusive - heightUnit.validRange.start)
                        val endPos = (currentRange.end - heightUnit.validRange.start) /
                                (heightUnit.validRange.endInclusive - heightUnit.validRange.start)

                        // Selected range highlight
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = (startPos * LocalConfiguration.current.screenWidthDp).dp,
                                    end = ((1 - endPos) * LocalConfiguration.current.screenWidthDp).dp
                                )
                                .fillMaxHeight()
                                .background(
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                                    shape = RoundedCornerShape(4.dp)
                                )
                        )
                    }

                    RangeSliderFilter(
                        unit = heightUnit.text,
                        selectedRange = currentRange,
                        onRangeChange = { newRange ->
                            setCurrentRange(newRange ?: heightUnit.defaultRange)
                            onRangeChange(
                                newRange?.let { r ->
                                    Range(
                                        start = r.start,
                                        end = r.end
                                    )
                                }
                            )
                        },
                        valueRange = heightUnit.validRange,
                        valueFormatter = { value -> "%.1f".format(value) }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedButton(
                            onClick = { onRangeChange(null) },
                            border = ButtonDefaults.outlinedButtonBorder.copy(
                                width = 1.dp,
                                brush = Brush.verticalGradient(
                                    0.0f to MaterialTheme.colorScheme.outline,
                                )
                            ),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            Text("Clear")
                        }

                        Button(
                            onClick = { expanded = false },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Text("Done")
                        }
                    }
                }
            }
        }
    }
}