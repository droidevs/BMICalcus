package io.droidevs.bmicalc.ui.components

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.droidevs.bmicalc.domain.Range
import io.droidevs.bmicalc.domain.model.ChartConfig
import io.droidevs.bmicalc.domain.model.ChartDataset
import io.droidevs.bmicalc.domain.model.LabelPosition

@Composable
fun HealthChart(
    config: ChartConfig,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val xValues = config.datasets.firstOrNull()?.values?.map { it.timestamp } ?: emptyList()
        if (xValues.isEmpty()) return@Canvas

        val xLabels = calculateOptimalLabels(
            dataPoints = xValues,
            availableWidth = canvasWidth,
            minLabelSpacing = 80.dp.toPx()
        )

        drawYAxis(config, canvasWidth, canvasHeight)
        drawXAxis(config, canvasWidth, canvasHeight, xValues, xLabels)
        drawGridLines(config, canvasWidth, canvasHeight, xValues, xLabels)

        config.datasets.forEach { dataset ->
            drawDataSet(
                dataset = dataset,
                config = config,
                canvasWidth = canvasWidth,
                canvasHeight = canvasHeight,
                xValues = xValues,
                xLabels = xLabels
            )
        }
    }
}

private fun DrawScope.drawYAxis(
    config: ChartConfig,
    canvasWidth: Float,
    canvasHeight: Float
) {
    drawLine(
        color = Color.Gray,
        start = Offset(0f, 0f),
        end = Offset(0f, canvasHeight),
        strokeWidth = 2.dp.toPx()
    )

    val yLabelCount = 6
    val yStep = (config.yRange.end - config.yRange.start) / (yLabelCount - 1)

    for (i in 0 until yLabelCount) {
        val value = config.yRange.start + i * yStep
        val yPos = calculateYPosition(value, config.yRange, canvasHeight)

        drawLine(
            color = Color.Gray,
            start = Offset(0f, yPos),
            end = Offset(5.dp.toPx(), yPos),
            strokeWidth = 1.dp.toPx()
        )

        drawContext.canvas.nativeCanvas.apply {
            drawText(
                config.yLabelFormatter(value),
                10.dp.toPx(),
                yPos + (12.sp.toPx() / 3),
                Paint().apply {
                    color = android.graphics.Color.BLACK
                    textSize = 12.sp.toPx()
                    textAlign = Paint.Align.LEFT
                }
            )
        }
    }
}

private fun DrawScope.drawXAxis(
    config: ChartConfig,
    canvasWidth: Float,
    canvasHeight: Float,
    xValues: List<Long>,
    xLabels: List<LabelPosition>
) {
    drawLine(
        color = Color.Gray,
        start = Offset(0f, canvasHeight),
        end = Offset(canvasWidth, canvasHeight),
        strokeWidth = 2.dp.toPx()
    )

    xLabels.forEach { label ->
        val value = label.value as? Long ?: return@forEach
        val index = xValues.indexOf(value)
        if (index != -1) {
            val xPos = calculateXPosition(index, xValues.size, canvasWidth)

            drawLine(
                color = Color.Gray,
                start = Offset(xPos, canvasHeight),
                end = Offset(xPos, canvasHeight - 5.dp.toPx()),
                strokeWidth = 1.dp.toPx()
            )

            if (label.isMajor || xLabels.size <= 8) {
                val textPaint = Paint().apply {
                    color = android.graphics.Color.BLACK
                    textSize = 12.sp.toPx()
                    textAlign = Paint.Align.CENTER
                }
                val text = config.xLabelFormatter(label.value)
                val textWidth = textPaint.measureText(text)
                val adjustedX = xPos.coerceIn(textWidth / 2, canvasWidth - textWidth / 2)

                drawContext.canvas.nativeCanvas.drawText(
                    text,
                    adjustedX,
                    canvasHeight + 20.dp.toPx(),
                    textPaint
                )
            }
        }
    }
}

private fun DrawScope.drawGridLines(
    config: ChartConfig,
    canvasWidth: Float,
    canvasHeight: Float,
    xValues: List<Long>,
    xLabels: List<LabelPosition>
) {
    xLabels.filter { it.isMajor }.forEach { label ->
        val value = label.value as? Long ?: return@forEach
        val index = xValues.indexOf(value)
        if (index != -1) {
            val xPos = calculateXPosition(index, xValues.size, canvasWidth)
            drawLine(
                color = Color.LightGray,
                start = Offset(xPos, 0f),
                end = Offset(xPos, canvasHeight),
                strokeWidth = 0.5.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f), 0f)
            )
        }
    }

    val yLabelCount = 6
    val yStep = (config.yRange.end - config.yRange.start) / (yLabelCount - 1)

    for (i in 1 until yLabelCount - 1) {
        val value = config.yRange.start + i * yStep
        val yPos = calculateYPosition(value, config.yRange, canvasHeight)

        drawLine(
            color = Color.LightGray,
            start = Offset(0f, yPos),
            end = Offset(canvasWidth, yPos),
            strokeWidth = 0.5.dp.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f), 0f)
        )
    }
}

private fun DrawScope.drawDataSet(
    dataset: ChartDataset,
    config: ChartConfig,
    canvasWidth: Float,
    canvasHeight: Float,
    xValues: List<Long>,
    xLabels: List<LabelPosition>
) {
    if (dataset.values.isEmpty()) return

    val path = Path()
    val firstValue = dataset.values.first()
    val firstX = calculateXPosition(0, dataset.values.size, canvasWidth)
    val firstY = calculateYPosition(firstValue.value, config.yRange, canvasHeight)
    path.moveTo(firstX, firstY)

    dataset.values.forEachIndexed { index, point ->
        if (index == 0) return@forEachIndexed
        val x = calculateXPosition(index, dataset.values.size, canvasWidth)
        val y = calculateYPosition(point.value, config.yRange, canvasHeight)
        path.lineTo(x, y)
    }

    drawPath(
        path = path,
        color = dataset.color,
        style = Stroke(width = 3.dp.toPx())
    )

    xLabels.forEach { label ->
        val value = label.value as? Long ?: return@forEach
        val dataIndex = xValues.indexOf(value)
        if (dataIndex in dataset.values.indices) {
            val x = calculateXPosition(dataIndex, dataset.values.size, canvasWidth)
            val y = calculateYPosition(dataset.values[dataIndex].value, config.yRange, canvasHeight)

            drawCircle(
                color = dataset.color,
                radius = 6.dp.toPx(),
                center = Offset(x, y)
            )

            if (label.isMajor) {
                drawCircle(
                    color = Color.White,
                    radius = 10.dp.toPx(),
                    center = Offset(x, y),
                    style = Stroke(width = 2.dp.toPx())
                )
            }
        }
    }
}

private fun calculateOptimalLabels(
    dataPoints: List<Long>,
    availableWidth: Float,
    minLabelSpacing: Float
): List<LabelPosition> {
    val maxLabels = (availableWidth / minLabelSpacing).toInt().coerceAtLeast(2)
    val step = (dataPoints.size / maxLabels).coerceAtLeast(1)

    return dataPoints.mapIndexedNotNull { index, value ->
        when {
            index == 0 || index == dataPoints.lastIndex ->
                LabelPosition(value, isMajor = true)
            index % step == 0 ->
                LabelPosition(value, isMajor = index % (step * 2) == 0)
            else -> null
        }
    }
}

private fun calculateXPosition(
    index: Int,
    xCount: Int,
    canvasWidth: Float
): Float {
    if (xCount <= 1) return 0f
    return index.toFloat() / (xCount - 1) * canvasWidth
}

private fun calculateYPosition(
    value: Float,
    yRange: Range,
    canvasHeight: Float
): Float {
    val span = (yRange.end - yRange.start).takeIf { it != 0f } ?: 1f
    val normalized = (value - yRange.start) / span
    return canvasHeight - (normalized * canvasHeight)
}
