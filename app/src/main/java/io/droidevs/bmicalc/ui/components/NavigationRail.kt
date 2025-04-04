package io.droidevs.bmicalc.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.droidevs.bmicalc.ui.model.NavigationItem
import io.droidevs.bmicalc.ui.window.LayoutMode
import io.droidevs.bmicalc.ui.window.LocalWindow

@Composable
fun AppNavRail(
    modifier: Modifier = Modifier,
    items: List<NavigationItem>,
    selectedItem: Int,
    onClick: (NavigationItem) -> Unit
) {
    val layoutMode = LocalWindow.current.layoutMode

    val railWidth by remember {
        derivedStateOf {
            if (layoutMode == LayoutMode.DOUBLE_BIG)
                120.dp
            else
                80.dp
        }
    }

    val rectColor1 = MaterialTheme.colorScheme.primary.copy(alpha = 0.95f)
    val rectColor2 = MaterialTheme.colorScheme.primary.copy(alpha = 0.85f)
    NavigationRail(
        modifier = modifier
            .width(railWidth)
            .fillMaxHeight()
            .padding(vertical = 12.dp)
            .clip(RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
            .shadow(10.dp, shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha= 0.8f)
                    )
                )
            )
            .drawWithContent {
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            rectColor1,
                            rectColor2
                        )
                    )
                )
                drawContent()
            },
        containerColor = Color.Transparent,
        header = {
            TODO()
        }
    ){
       items.forEach {
           item ->
           val isSelected = item.id == selectedItem

           val iconSize by animateDpAsState(
               targetValue = if (isSelected) 32.dp else 24.dp,
               animationSpec = tween(durationMillis = 300), label = ""
           )

           val iconColor by animateColorAsState(
               targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimary
               else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
               animationSpec = tween(durationMillis = 300), label = ""
           )

           NavigationRailItem(
               modifier = Modifier.padding(8.dp),
               selected = isSelected,
               onClick = { onClick(item) },
               label = {
                   Text(
                       text = item.title,
                       color = iconColor,
                       style = TextStyle(fontWeight = FontWeight.SemiBold)
                   )
               },
               icon = {
                   Icon(
                       imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                       contentDescription = item.title,
                       modifier = Modifier.size(iconSize),
                       tint = iconColor
                   )
               }
           )
       }
    }
}
