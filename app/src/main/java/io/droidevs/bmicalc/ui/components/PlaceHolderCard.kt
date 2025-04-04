package io.droidevs.bmicalc.ui.components

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.Dp


@Composable
fun PlaceholderCard(
    ratio: Float = 4/3f,
    minHeight: Dp = 160.dp,
    modifier: Modifier = Modifier
) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Box(
            modifier = modifier
                .let {
                    it.aspectRatio(ratio)
                    it.heightIn(min =minHeight - 40.dp)
                    it.shrimmerEffect()
                }
                .clip(CardDefaults.shape)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = modifier
                .height(30.dp)
                .shrimmerEffect()
                .fillMaxWidth(0.7f)
        )
    }
}

fun Modifier.shrimmerEffect() : Modifier = composed {
    var size = remember { mutableStateOf(IntSize.Zero) }
    val transition = rememberInfiniteTransition(label = "")
    val startOffset by transition.animateFloat(
        initialValue = -2 * size.value.width.toFloat(),
        targetValue =  2 * size.value.height.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000)
        ), label = ""
    )
    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color(229, 227, 227, 255),
                Color(166, 165, 165, 255),
                Color(229, 227, 227, 255)
            ),
            start = Offset(startOffset,0f),
            end = Offset(startOffset + size.value.width.toFloat(), size.value.height.toFloat())
        )
    ).onGloballyPositioned {
        size.value = it.size
    }
}



// Define a data class for props
data class PreviewWallpaperCardProps(
    val modifier: Modifier
)

// Implement a CollectionPreviewParameterProvider for WallpaperCardProps
class PlaceholderWallpaperCardPropsProvider : CollectionPreviewParameterProvider<PreviewWallpaperCardProps>(
    listOf(
        PreviewWallpaperCardProps(Modifier.fillMaxWidth(0.5f)),
        PreviewWallpaperCardProps(Modifier.fillMaxWidth(0.8f))
    )
)

@Preview
@Composable
fun PlaceholderWallpaperCardPreview(
    @PreviewParameter(PlaceholderWallpaperCardPropsProvider::class) props: PreviewWallpaperCardProps
) {
    PlaceholderCard(
        modifier = props.modifier
    )
}

