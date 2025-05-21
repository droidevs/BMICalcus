package io.droidevs.bmicalc.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.droidevs.bmicalc.R
import io.droidevs.bmicalc.ui.model.AppBarMenuItem
import io.droidevs.bmicalc.ui.theme.blueYellowGradient


import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * A customizable AppBar composable that supports animated visibility of navigation icons and actions.
 *
 * @param navigationIcon A composable lambda representing the navigation icon.
 * @param actions A composable lambda representing the actions in the AppBar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedAppBar(
    title: String? = stringResource(id = R.string.app_name),
    navigationIcon: @Composable () -> Unit,
    actions: @Composable () -> Unit,
) {
    val transition = rememberInfiniteTransition(label = "")
    val shimmerOffset by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = LinearEasing)
        ), label = ""
    )

    TopAppBar(
        title = {
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(animationSpec = tween(500)) + scaleIn(initialScale = 0.8f),
                exit = fadeOut(animationSpec = tween(300)) + scaleOut(targetScale = 1.1f)
            ) {
                Text(
                    text = title ?: stringResource(id = R.string.app_name),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.graphicsLayer {
                        shadowElevation = 8f
                        shape = RoundedCornerShape(12.dp)
                    }
                )
            }
        },
        modifier = Modifier
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f)
                    ),
                    start = Offset(x = shimmerOffset, y = 0f),
                    end = Offset(x = 0f, y = shimmerOffset)
                )
            )
            .padding(vertical = 6.dp)
            .shadow(8.dp, shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
            .blur(8.dp),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent, // Background is handled by gradient.
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White
        ),
        navigationIcon = {
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + scaleIn(initialScale = 0.8f),
                exit = fadeOut() + scaleOut(targetScale = 1.2f)
            ) {
                navigationIcon()
            }
        },
        actions = {
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + scaleIn(initialScale = 0.8f) + expandHorizontally(),
                exit = fadeOut() + scaleOut(targetScale = 1.2f) + shrinkHorizontally()
            ) {
                actions()
            }
        }
    )
}


/**
 * A specialized AppBar with a back button.
 *
 * @param onBackPressed Lambda function to handle back button press events.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackAppBar(
    title: String? = null,
    menuItems: List<AppBarMenuItem> = emptyList(),
    onBackPressed: () -> Unit,
    onMenuItemClicked: ((AppBarMenuItem) -> Unit)? = null,
) {

    AnimatedAppBar(
        title = title,
        navigationIcon = {
            BackButton(onClick = onBackPressed)
        },
        actions = {
            val actionMenuItems = menuItems.filter { it.action }
            val others = menuItems.filter { !it.action }

            for (actionItem in actionMenuItems){
                IconButton(
                    onClick = {
                        onMenuItemClicked?.let { it(actionItem) }
                    }
                ) {
                    Icon(
                        imageVector = actionItem.iconRes,
                        contentDescription = actionItem.title,
                    )
                }
            }
            if (others.isNotEmpty()) {
                var expanded by remember { mutableStateOf(false) }

                Box {
                    IconButton(
                        onClick = { expanded = !expanded },
                        modifier = Modifier
                            .graphicsLayer {
                                rotationZ = if (expanded) 180f else 0f
                                scaleX = if (expanded) 0.8f else 1f
                                scaleY = if (expanded) 0.8f else 1f
                            }
                            .animateContentSize()
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert, // Three-dot menu
                            contentDescription = "More",
                            tint = Color.White
                        )
                    }
                    if (others.isNotEmpty()) {
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            others.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(item.title) },
                                    onClick = {
                                        expanded = false
                                        onMenuItemClicked?.invoke(item)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}


/**
 * A specialized AppBar with a menu button and a settings button.
 *
 * @param onMenuPressed Lambda function to handle menu button press events.
 * @param onSettingPressed Lambda function to handle settings button press events.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuAppBar(
    title: String? = null,
    menuItems: List<AppBarMenuItem> = emptyList(),
    onMenuPressed: () -> Unit,
    onSettingPressed: () -> Unit,
    onMenuItemClicked: ((AppBarMenuItem) -> Unit)? = null,
) {
    var menuButtonPressed by remember { mutableStateOf(false) }
    var settingsButtonPressed by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    AnimatedAppBar (
        title = title,
        navigationIcon = {
            MenuButton(onClick = onMenuPressed)
        },
        actions = {
            SettingsButton (onClick = onSettingPressed)

            val actionMenuItems = menuItems.filter { it.action }
            val others = menuItems.filter { !it.action }

            for (actionItem in actionMenuItems){
                IconButton(
                    onClick = {
                        onMenuItemClicked?.let { it(actionItem) }
                    }
                ) {
                    Icon(
                        imageVector = actionItem.iconRes,
                        contentDescription = actionItem.title,
                    )
                }
            }
            if (others.isNotEmpty()) {
                var expanded by remember { mutableStateOf(false) }

                Box {
                    IconButton(
                        onClick = { expanded = !expanded },
                        modifier = Modifier
                            .graphicsLayer {
                                rotationZ = if (expanded) 180f else 0f
                                scaleX = if (expanded) 0.8f else 1f
                                scaleY = if (expanded) 0.8f else 1f
                            }
                            .animateContentSize()
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert, // Three-dot menu
                            contentDescription = "More",
                            tint = Color.White
                        )
                    }
                    if (others.isNotEmpty()) {
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            others.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(item.title) },
                                    onClick = {
                                        expanded = false
                                        onMenuItemClicked?.invoke(item)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun NavigationAction(
    onClick: () -> Unit,
    icon: @Composable () -> Unit
){
    var buttonPressed by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val rippleIndication = LocalIndication.current

    val scope = rememberCoroutineScope()

    var scale by remember { mutableStateOf(1f) }
    val animatedScale by animateFloatAsState(
        targetValue = if (buttonPressed) 0.9f else 1f,
        animationSpec = tween(150)
    )

    Box(
        modifier = Modifier
            .scale(animatedScale)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
            .clickable(
                interactionSource = interactionSource,
                indication = rippleIndication
            ) {
                buttonPressed = true
                onClick.invoke()
                scope.launch {
                    delay(100)
                    buttonPressed = false
                }
            }
            .padding(8.dp)
    ) {
        icon()
    }
}

/**
 * A composable for a menu button with an icon.
 *
 * @param onClick Lambda function to handle menu button click events.
 */
@Composable
fun MenuButton(
    onClick: () -> Unit
) {
    NavigationAction (
        onClick = onClick,
    ){
        Icon(
            imageVector = Icons.Default.Menu, // Menu icon from material icons.
            contentDescription = "Toggle drawer" // Accessibility description.
        )
    }
}

/**
 * A composable for a back button with an animated gradient overlay.
 *
 * @param onClick Lambda function to handle back button click events.
 */
@Composable
fun BackButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    NavigationAction(
        onClick = onClick,
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack, // Auto-mirrored back arrow icon.
            contentDescription = stringResource(R.string.back), // Accessibility description from resources.
            modifier = Modifier
                .graphicsLayer(alpha = 0.99f) // Adjusts alpha for rendering.
                .drawWithCache {
                    onDrawWithContent {
                        drawContent() // Draws the back button icon.
                        drawRect(blueYellowGradient, blendMode = BlendMode.SrcAtop) // Adds a gradient overlay.
                    }
                },
        )
    }
}

/**
 * A composable for a settings button with an icon.
 *
 * @param onClick Lambda function to handle settings button click events.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsButton(
    onClick: () -> Unit,
) {
    var animatedRotation by remember { mutableStateOf(0f) }
    val rotation by animateFloatAsState(
        targetValue = animatedRotation,
        animationSpec = tween(300, easing = FastOutSlowInEasing)
    )

    IconButton(
        onClick = {
            animatedRotation += 360f
            onClick.invoke()
        },
        modifier = Modifier.graphicsLayer { rotationZ = rotation }
    ) {
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = "Settings",
            tint = Color.White
        )
    }
}

