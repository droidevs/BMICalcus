package io.droidevs.bmicalc.ui.components

import androidx.compose.material3.DrawerState
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.droidevs.bmicalc.domain.model.BMICategory
import io.droidevs.bmicalc.ui.model.NavigationItem

import kotlinx.coroutines.launch

class DrawerController {
    private var drawerState: DrawerState? = null

    fun setDrawerState(state: DrawerState) {
        this.drawerState = state
    }

    suspend fun open() {
        drawerState?.open()
    }

    suspend fun close() {
        drawerState?.close()
    }

    fun isOpen(): Boolean {
        return drawerState?.isOpen == true
    }
}



@Composable
fun NavigationDrawer(
    modifier: Modifier = Modifier,
    items: List<NavigationItem>,
    selectedItem: Int = -1,
    onItemClick: (NavigationItem) -> Unit,
    drawerController: DrawerController? = null,
    content: @Composable () -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    val color1 = MaterialTheme.colorScheme.primary.copy(alpha = 0.95f)
    val color2 = MaterialTheme.colorScheme.primary.copy(alpha = 0.85f)

    SideEffect {
        drawerController?.setDrawerState(drawerState)
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.85f) // Take 85% of screen width
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .shadow(12.dp, shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)) // Elevation effect
                    .drawWithContent {
                        drawRect(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    color1,
                                    color2
                                )
                            )
                        )
                        drawContent()
                    }
                    .clip(RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)) // Smooth rounded edges
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Animated Drawer Header
                    DrawerHeader(
                        closeDrawer = {
                            coroutineScope.launch {
                                drawerState.close()
                            }
                        }
                    )

                    // Divider with a glow effect
                    Divider(
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.6f),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    // Drawer Items
                    DrawerBody(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        items = items,
                        selectedItem = selectedItem,
                        onItemClick = {
                            coroutineScope.launch {
                                drawerState.close()
                            }
                            onItemClick(it)
                        }
                    )
                }
            }
        },
        content = content
    )
}

@Composable
fun DrawerHeader(
    closeDrawer : () -> Unit
){

    var rotationAngle = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        rotationAngle.animateTo(
            targetValue = 180f,
            animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFF2193b0), Color(0xFF6dd5ed))
                )
            ),
    ) {
        // Close Button with Animation
        IconButton(
            modifier = Modifier.align(Alignment.End),
            onClick = closeDrawer,
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                modifier = Modifier
                    .size(28.dp)
                    .graphicsLayer(rotationZ = rotationAngle.value),
                tint = Color.White
            )
        }

        //Profile Image With Ripple Effect
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.2f))
                .clickable { }
                .padding(4.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            BmiDrawerIndicator(
                bmiValue = 22f
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "BMI Tracker",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold)
        )
    }
}

@Composable
fun DrawerBody(
    items: List<NavigationItem>,
    selectedItem: Int = -1,
    modifier: Modifier = Modifier,
    itemTextStyle: TextStyle = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Medium),
    onItemClick: (NavigationItem) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f))
            .padding(12.dp)
    ) {
        items(items) { item ->
            val isSelected = item.id == selectedItem

            val backgroundColor by animateColorAsState(
                targetValue = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                else Color.Transparent,
                animationSpec = tween(durationMillis = 300), label = ""
            )

            val textColor by animateColorAsState(
                targetValue = if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurface,
                animationSpec = spring(stiffness = Spring.StiffnessMedium), label = ""
            )

            val scale by animateFloatAsState(
                targetValue = if (isSelected) 1.1f else 1f,
                animationSpec = spring(stiffness = Spring.StiffnessMedium), label = ""
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(backgroundColor)
                    .clickable {
                        onItemClick(item)
                    }
                    .padding(vertical = 12.dp, horizontal = 16.dp)
                    .graphicsLayer(scaleX = scale, scaleY = scale), // Add bounce effect
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                    contentDescription = item.title,
                    tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .size(28.dp)
                        .padding(end = 12.dp)
                )

                Text(
                    text = item.title,
                    style = itemTextStyle.copy(color = textColor),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp)) // Spacing between items
        }

    }
}

@Composable
fun BmiDrawerIndicator(
    bmiValue: Float,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val bmiCategory = BMICategory.getCategory(bmiValue)

    val categoryColor = bmiCategory.color

    // Animation for continuous pulse effect
    val infiniteTransition = rememberInfiniteTransition()
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    // Click animation
    var isClicked by remember { mutableStateOf(false) }
    val clickScale by animateFloatAsState(
        targetValue = if (isClicked) 0.9f else 1f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 800f), label = ""
    )
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .size(80.dp)
            .clip(CircleShape)
            .background(categoryColor.copy(alpha = 0.8f))
            .scale(clickScale * pulseScale)
            .clickable {
                isClicked = true
                onClick()
                isClicked = false
            }
            .padding(8.dp),
        contentAlignment = Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "BMI",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Light
            )
            Text(
                text = "%.1f".format(bmiValue),
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = bmiCategory.displayName,
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.alpha(0.9f)
            )
        }
    }
}
