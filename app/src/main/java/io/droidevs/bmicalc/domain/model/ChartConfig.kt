package io.droidevs.bmicalc.domain.model


import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import io.droidevs.bmicalc.domain.Range
import io.droidevs.bmicalc.utils.format
import kotlin.math.abs
import kotlinx.datetime.Instant

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
        fun formatDateLabel(value: Any): String {
            val timestamp = value as? Long ?: return value.toString()
            return Instant.fromEpochMilliseconds(timestamp).format("MMM d")
        }

        fun computeRange(values: List<Float>): Range {
            val minValue = values.minOrNull()
            val maxValue = values.maxOrNull()
            if (minValue == null || maxValue == null) {
                return Range(start = 0f, end = 1f)
            }
            if (minValue == maxValue) {
                val padding = if (minValue == 0f) 1f else abs(minValue) * 0.1f
                return Range(start = minValue - padding, end = maxValue + padding)
            }
            return Range(start = minValue * 0.9f, end = maxValue * 1.1f)
        }

        when (dataType) {

            DataType.BMI -> {
                val bmiPoints = data.map { DataPoint(value = it.bmi, timestamp = it.date) }
                val bmiValues = bmiPoints.map { it.value }
                ChartConfig(
                    yRange = computeRange(bmiValues),
                    timRange = timeRange,
                    yLabelFormatter = { "%.1f".format(it) },
                    xLabelFormatter = { formatDateLabel(it) },
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
                    yRange = computeRange(heightValues),
                    timRange = timeRange,
                    yLabelFormatter = { "%.0f cm".format(it) },
                    xLabelFormatter = { formatDateLabel(it) },
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
                    yRange = computeRange(weightValues),
                    timRange = timeRange,
                    yLabelFormatter = { "%.1f kg".format(it) },
                    xLabelFormatter = { formatDateLabel(it) },
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
                val combinedValues = heightValues + weightValues
                ChartConfig(
                    yRange = computeRange(combinedValues),
                    timRange = timeRange,
                    yLabelFormatter = { if (it > 100) "%.0f cm".format(it) else "%.1f kg".format(it) },
                    xLabelFormatter = { formatDateLabel(it) },
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
