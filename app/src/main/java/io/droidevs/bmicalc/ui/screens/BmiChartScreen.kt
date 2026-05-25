package io.droidevs.bmicalc.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.droidevs.bmicalc.domain.model.BmiRecord
import io.droidevs.bmicalc.domain.model.HealthRecord
import io.droidevs.bmicalc.domain.model.ChartType
import io.droidevs.bmicalc.ui.components.AnimatedChartSwitcher
import io.droidevs.bmicalc.ui.components.TimeRangeSelector
import io.droidevs.bmicalc.ui.helper.states.ChartState
import io.droidevs.bmicalc.ui.helper.states.rememberChartState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BmiChartScreen(
    records: List<BmiRecord>,
    chartState: ChartState? = null,
) {
    val state = rememberChartState(initialState = chartState)
    val chartRecords = remember(records) {
        records.map { record ->
            HealthRecord(
                date = record.date,
                height = record.height,
                weight = record.weight,
                bmi = record.bmi
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Health Progress") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            // Chart Type Selector
            ChartTypeSelector(state)

            TimeRangeSelector(
                range = state.timeRange,
                onRangeSelected = { range ->
                    state.timeRange = range
                }
            )
            // The Actual Chart
            AnimatedChartSwitcher(
                chartState = state,
                records = chartRecords
            )
        }
    }
}

@Composable
fun ChartTypeSelector(chartState: ChartState) {
    var expanded by remember { mutableStateOf(false) }
    val items = listOf(ChartType.BMI, ChartType.HEIGHT, ChartType.WEIGHT)

    Box(modifier = Modifier.padding(16.dp)) {
        OutlinedButton(onClick = { expanded = true }) {
            Text(chartState.chartType.text)
            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEachIndexed { index, item ->
                DropdownMenuItem(
                    text = { Text(text = item.text) },
                    onClick = {
                        chartState.chartType = items[index]
                        expanded = false
                    }
                )
            }
        }
    }
}




