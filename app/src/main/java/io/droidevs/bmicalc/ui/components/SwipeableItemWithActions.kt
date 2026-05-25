package io.droidevs.bmicalc.ui.components


import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@Composable
fun SwipeableItemWithActions(
    revealedDirection: DirectionMode = DirectionMode.None,
    modifier: Modifier = Modifier,
    startAction: @Composable BoxScope.() -> Unit,
    endAction: @Composable BoxScope.() -> Unit,
    content: @Composable () -> Unit,
    onExpanded: (mode : DirectionMode) -> Unit,
    onCollapsed: () -> Unit,
) {

    val direction = LocalLayoutDirection.current
    val isRlt by remember(direction) {
        derivedStateOf {
            direction == LayoutDirection.Rtl
        }
    }
    var contextStartMenuWidth by remember {
        mutableFloatStateOf(0f)
    }
    var contextEndMenuWidth by remember {
        mutableFloatStateOf(0f)
    }
    var contentWidth by remember {
        mutableFloatStateOf(0f)
    }

    val offset = remember {
        Animatable(initialValue = 0f)
    }

    val scope = rememberCoroutineScope()
    var tapOffset by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(revealedDirection) {
        if (revealedDirection == DirectionMode.Start) {
            if (isRlt)
                offset.snapTo(contextStartMenuWidth)
            else
                offset.snapTo(-contextStartMenuWidth)
        }
        else if (revealedDirection == DirectionMode.End) {
            if (isRlt)
                offset.snapTo(-contextEndMenuWidth)
            else
                offset.snapTo(contextEndMenuWidth)
        }
        else
            offset.snapTo(0f)
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Box(
            modifier = Modifier
                .onSizeChanged {
                    contextStartMenuWidth = it.width.toFloat()
                }
                .align(Alignment.CenterStart)
        ) {
            startAction()
        }
        Box(
            modifier = Modifier
                .onSizeChanged {
                    contextEndMenuWidth = it.width.toFloat()
                }
                .align(Alignment.CenterEnd)
        ) {
            endAction()
        }
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .onSizeChanged {
                    contentWidth = it.width.toFloat()
                }
                .offset {
                    IntOffset(offset.value.roundToInt(), 0)
                }
                .pointerInput(contentWidth, contextStartMenuWidth, contextEndMenuWidth) {
                    detectTapGestures(
                        onTap = { offset ->
                            tapOffset = offset.x
                        }
                    )
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { _, dragAmount ->
                            scope.launch {
                                val originalOffset = offset.value
                                var newOffset = (originalOffset + dragAmount)
                                if (tapOffset < contentWidth / 2)
                                    if (isRlt)
                                        newOffset = newOffset.coerceIn(0f, contextStartMenuWidth)
                                    else
                                        newOffset = newOffset.coerceIn(-contextEndMenuWidth, 0f)
                                else
                                    if (isRlt)
                                        newOffset = newOffset.coerceIn(-contextEndMenuWidth, 0f)
                                    else
                                        newOffset = newOffset.coerceIn(0f, contextStartMenuWidth)
                                offset.snapTo(newOffset)
                            }
                        },
                        onDragEnd = {
                            if (isRlt) {
                                if (tapOffset < contentWidth / 2) {
                                    when {
                                        offset.value >= contextStartMenuWidth / 2f ->
                                            scope.launch {
                                                offset.snapTo(contextStartMenuWidth)
                                                onExpanded(DirectionMode.Start)
                                            }

                                        offset.value <= contextStartMenuWidth / 2f ->
                                            scope.launch {
                                                offset.snapTo(0f)
                                                onCollapsed()
                                            }
                                    }
                                } else {
                                    when {
                                        offset.value > -contextEndMenuWidth / 2f ->
                                            scope.launch {
                                                offset.snapTo(0f)
                                                onCollapsed()
                                            }

                                        offset.value < -contextEndMenuWidth / 2f ->
                                            scope.launch {
                                                offset.snapTo(-contextEndMenuWidth)
                                                onExpanded(DirectionMode.End)
                                            }
                                    }
                                }
                            } else {
                                if (tapOffset < contentWidth / 2) {
                                    when {
                                        offset.value >= contextEndMenuWidth / 2f ->
                                            scope.launch {
                                                offset.snapTo(contextEndMenuWidth)
                                                onExpanded(DirectionMode.End)
                                            }

                                        offset.value <= contextEndMenuWidth / 2f ->
                                            scope.launch {
                                                offset.snapTo(0f)
                                                onCollapsed()
                                            }
                                    }
                                } else {
                                    when {
                                        offset.value > -contextStartMenuWidth / 2f ->
                                            scope.launch {
                                                offset.snapTo(0f)
                                                onCollapsed()
                                            }

                                        offset.value < -contextStartMenuWidth / 2f ->
                                            scope.launch {
                                                offset.snapTo(-contextStartMenuWidth)
                                                onExpanded(DirectionMode.End)
                                            }
                                    }
                                }
                            }
                        }
                    )
                }
        ) {
            content()
        }
    }

}

sealed class DirectionMode{
    object Start : DirectionMode()
    object End : DirectionMode()

    object None : DirectionMode()
}