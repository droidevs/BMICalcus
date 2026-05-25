package io.droidevs.bmicalc.domain.model


import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import io.droidevs.bmicalc.domain.Range
import kotlin.math.max
import kotlin.math.min

data class ChartConfig(
    val timRange: TimeRange = TimeRange.Week,
    val yRange: Range,
    val yLabelFormatter: (Float) -> String,
    val xLabelFormatter: (Any) -> String,
    val datasets: List<ChartDataset>
)

@Composable
fun rememberChartConfig(
    dataType: DataType,
    timeRange: TimeRange,
    data: List<HealthRecord>
): ChartConfig {

    return remember(dataType, timeRange, data) {
        when (dataType) {

            DataType.BMI -> {
                val bmiPoints = data.map { DataPoint(value = it.bmi, timestamp = it.date) }
                val bmiValues = bmiPoints.map { it.value }
                ChartConfig(
                    yRange = Range(start = bmiValues.min() * 0.9f, end = (bmiValues.max() * 1.1f)),
                    timRange = timeRange,
                    yLabelFormatter = { "%.1f".format(it) },
                    xLabelFormatter = { it.toString() },
                    datasets = listOf(
                        ChartDataset(
                            label = "BMI",
                            values = bmiPoints,
                            color = Color(0xFF4BC0C0)
                        )
                    )
                )
            }
            DataType.HEIGHT -> {
                val heightPoints = data.map { DataPoint(value = it.height, timestamp = it.date) }
                val heightValues = heightPoints.map { it.value }
                ChartConfig(
                    yRange = Range(start = heightValues.min() * 0.9f, end = (heightValues.max() * 1.1f)),
                    timRange = timeRange,
                    yLabelFormatter = { "%.0f cm".format(it) },
                    xLabelFormatter = { it.toString() },
                    datasets = listOf(
                        ChartDataset(
                            label = "Height",
                            values = heightPoints,
                            color = Color(0xFF36A2EB)
                        )
                    )
                )
            }
            DataType.WEIGHT -> {
                val weightPoints = data.map { DataPoint(value = it.weight, timestamp = it.date) }
                val weightValues = weightPoints.map { it.value }
                ChartConfig(
                    yRange = Range(start = weightValues.min() * 0.9f, end = (weightValues.max() * 1.1f)),
                    timRange = timeRange,
                    yLabelFormatter = { "%.1f kg".format(it) },
                    xLabelFormatter = { it.toString() },
                    datasets = listOf(
                        ChartDataset(
                            label = "Weight",
                            values = weightPoints,
                            color = Color(0xFFFF6384)
                        )
                    )
                )
            }
            DataType.BOTH -> {
                val heightPoints = data.map { DataPoint(value = it.height, timestamp = it.date) }
                val heightValues = heightPoints.map { it.value }
                val weightPoints = data.map { DataPoint(value = it.weight, timestamp = it.date) }
                val weightValues = weightPoints.map { it.value }
                ChartConfig(
                    yRange = Range(start = min(heightValues.min(), weightValues.min()) * 0.9f, end = (max(heightValues.max(), weightValues.max()) * 1.1f)),
                    timRange = timeRange,
                    yLabelFormatter = { if (it > 100) "%.0f cm".format(it) else "%.1f kg".format(it) },
                    xLabelFormatter = { it.toString() },
                    datasets = listOf(
                        ChartDataset(
                            label = "Height",
                            values = heightPoints,
                            color = Color(0xFF36A2EB)
                        ),
                        ChartDataset(
                            label = "Weight",
                            values = weightPoints,
                            color = Color(0xFFFF6384)
                        )
                    )
                )
            }
        }
    }
}
