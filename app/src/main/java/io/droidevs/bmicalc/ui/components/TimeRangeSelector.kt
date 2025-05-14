package io.droidevs.bmicalc.ui.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.droidevs.bmicalc.model.TimeRange
import io.droidevs.bmicalc.ui.helper.states.ChartState
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus

@Composable
fun TimeRangeSelector(
    range : TimeRange?,
    onRangeSelected: (TimeRange) -> Unit,
) {
    var timeRange by remember { mutableStateOf(range) }

    val timeRanges = listOf(TimeRange.Week, TimeRange.Month, TimeRange.Year, TimeRange.Custom(), TimeRange.All)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        timeRanges.forEachIndexed { index, range ->
            FilterChip(
                selected = timeRange == range ||
                        (timeRange is TimeRange.Custom && index == 3),
                onClick = {
                    if (index == 3) {
                        timeRange = TimeRange.Custom()
                    } else {
                        timeRange = range
                        onRangeSelected(range)
                    }
                },
                label = { Text(range.text) },
                modifier = Modifier.padding()
            )
        }
    }
    if (timeRange is TimeRange.Custom) {

        val timeZone = TimeZone.currentSystemDefault()
        var startDate by remember {
            mutableStateOf(
                (timeRange as TimeRange.Custom).start ?: Clock.System.now().minus(7, DateTimeUnit.DAY, timeZone)
            )
        }
        var endDate by remember {
            mutableStateOf((timeRange as TimeRange.Custom).end ?: Clock.System.now())
        }
        var currentSelection by remember {
            mutableStateOf<SelectionMode>(SelectionMode.Start)
        }
        var showDatePicker by remember {  mutableStateOf(false) }
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = Icons.Outlined.DateRange,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )

                Text(
                    text = "Time Period",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f).padding(start = 12.dp)
                )
            }
            HorizontalDivider(
                thickness = 1.dp
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                DateSelectionTab(
                    label = "Start Date",
                    selected = currentSelection == SelectionMode.Start,
                    onClick = {
                        currentSelection = SelectionMode.Start
                        showDatePicker = true
                    }
                )
                DateSelectionTab(
                    label = "End Date",
                    selected = currentSelection == SelectionMode.End,
                    onClick = {
                        currentSelection = SelectionMode.End
                        showDatePicker = true
                    }
                )
            }

            ShowDatePicker(
                show = showDatePicker,
                onPickDate = { instant ->
                    if (currentSelection == SelectionMode.Start)
                        startDate = instant
                    else
                        endDate = instant
                    showDatePicker = false
                }
            )

            // Selected range display
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FormattedDateDisplay(instant = startDate)
                FormattedDateDisplay(instant = endDate)
            }
        }
    }
}

private sealed class SelectionMode {
    object Start : SelectionMode()
    object End : SelectionMode()
}

@Composable
fun DateSelectionTab(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier =  Modifier
){
    val t = updateTransition(selected, label = "")

    val backgroundColor = t.animateColor(label = "") {
            isSelected ->
        if (isSelected)
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
        else
            MaterialTheme.colorScheme.surface
    }

    val borderColor = t.animateColor(
        label = ""
    ) { isSelected ->
        if (isSelected)
            MaterialTheme.colorScheme.primary
        else
            MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
    }

    val textColor = t.animateColor(
        label = "text_color"
    ) { isSelected->
        if (isSelected)
            MaterialTheme.colorScheme.onPrimary
        else
            MaterialTheme.colorScheme.onSurface
    }

    Surface(
        modifier = modifier
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(8.dp),
        color = backgroundColor.value,
        border = BorderStroke(1.dp, borderColor.value),
    ) {
        Text(
            text = label,
            color = textColor.value,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 8.dp
            ),
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowDatePicker(
    show: Boolean,
    onPickDate : (Instant) -> Unit
){
    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember(show) { mutableStateOf(show) }

    Button(
        onClick = { showDatePicker = true}
    ) {
        Text( text = "Pick a Date", style = MaterialTheme.typography.labelLarge)
    }

    if(showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false},
            confirmButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                        onPickDate.invoke(Instant.fromEpochMilliseconds(datePickerState.selectedDateMillis?: Clock.System.now().toEpochMilliseconds()))
                    }
                ) {
                    Text(text = "OK")
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                title = { Text(text = "Select Date") },
                headline = { Text("Choose a date") },
                showModeToggle = true
            )
        }
    }
}