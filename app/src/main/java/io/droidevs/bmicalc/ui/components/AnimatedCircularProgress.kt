package io.droidevs.bmicalc.ui.components


import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedCircularProgress(
    modifier: Modifier = Modifier,
    _size: Int = 32
) {
    val transition = rememberInfiniteTransition(label = "")

    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 300)
        ), label = ""
    )

    Canvas(modifier = modifier.size(_size.dp)) {
        val strokeWidth = 4.dp.toPx()
        val radius = size.minDimension / 2 - strokeWidth / 2
        val center = Offset(size.width / 2, size.height / 2)

        drawCircle(
            brush = Brush.linearGradient(
                colors = listOf(Color.Red, Color.Yellow, Color.Green)
            ),
            center = center,
            radius = radius,
            style = Stroke(width = strokeWidth)
        )

        drawArc(
            brush = Brush.linearGradient(
                colors = listOf(Color.Cyan, Color.Magenta, Color.Blue)
            ),
            startAngle = -90f,
            sweepAngle = 360 * progress,
            useCenter = false,
            topLeft = Offset(center.x - radius, center.y - radius),
            size = Size(radius * 2, radius * 2),
            style = Stroke(width = strokeWidth)
        )
    }
}

data class ProgressProps(val size: Int)

class ProgressPropsPreviewProvider : CollectionPreviewParameterProvider<ProgressProps>(
    listOf(
        ProgressProps(size = 32),
        ProgressProps(size = 48),
        ProgressProps(size = 64)
    )
)

@Preview
@Composable
fun PreviewAnimatedCircularProgress(
    @PreviewParameter(ProgressPropsPreviewProvider::class) props: ProgressProps
) {
    AnimatedCircularProgress(_size = props.size)
}