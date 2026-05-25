package io.droidevs.bmicalc.ui.helper.states

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.droidevs.bmicalc.domain.model.ChartType
import io.droidevs.bmicalc.domain.model.TimeRange

@Stable
class ChartState(
    chartType: ChartType = ChartType.BMI,
    timeRange: TimeRange = TimeRange.Month
) {
    var chartType by mutableStateOf(chartType)
    var timeRange by mutableStateOf(timeRange)
}


@Composable
fun rememberChartState(
    initialState: ChartState?,
): ChartState {
    return remember(initialState?.chartType, initialState?.timeRange) {
        ChartState(
            chartType = initialState?.chartType ?: ChartType.BMI,
            timeRange = initialState?.timeRange ?: TimeRange.Month
        )
    }
}
