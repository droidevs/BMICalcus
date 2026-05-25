package io.droidevs.bmicalc.ui.components

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.droidevs.bmicalc.domain.model.ChartType
import io.droidevs.bmicalc.domain.model.DataType
import io.droidevs.bmicalc.domain.model.HealthRecord
import io.droidevs.bmicalc.domain.model.rememberChartConfig
import io.droidevs.bmicalc.ui.helper.states.ChartState

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@Composable
fun AnimatedChartSwitcher(
    chartState: ChartState,
    records: List<HealthRecord>,
    modifier: Modifier = Modifier
) {
    if (records.isEmpty()) {
        EmptyChartPlaceholder(message = "No chart data yet")
        return
    }
    val dataType = when (chartState.chartType) {
        ChartType.BMI -> DataType.BMI
        ChartType.HEIGHT -> DataType.HEIGHT
        ChartType.WEIGHT -> DataType.WEIGHT
    }
    val config = rememberChartConfig(
        dataType = dataType,
        timeRange = chartState.timeRange,
        data = records
    )
    AnimatedContent(
        targetState = chartState.chartType,
        transitionSpec = {
            fadeIn() togetherWith  fadeOut()
        },
        modifier = modifier, label = ""
    ) { targetChartType ->
        HealthChart(
            config = config,
            modifier = modifier
        )
    }
}
