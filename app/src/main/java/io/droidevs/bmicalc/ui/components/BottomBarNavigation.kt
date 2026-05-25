package io.droidevs.bmicalc.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.droidevs.bmicalc.ui.model.NavigationItem
import io.droidevs.bmicalc.ui.nav.roots.Destination


@Composable
fun BottomNavigationBar(
    navigationItems: List<NavigationItem>,
    selected: Int,
    onNavigateTo: (item: NavigationItem) -> Unit
) {
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .shadow(4.dp, shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f),
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f)
                    )
                )
            ),
        containerColor = Color.Transparent,
        tonalElevation = 4.dp
    ) {
        navigationItems.forEach { item ->
            val isSelected = item.id == selected

            val iconSize by animateDpAsState(
                targetValue = if (isSelected) 24.dp else 20.dp,
                animationSpec = spring(dampingRatio = 0.5f, stiffness = 300f), label = ""
            )

            val iconColor by animateColorAsState(
                targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimary
                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                animationSpec = tween(durationMillis = 300), label = ""
            )

            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigateTo(item) },
                label = {
                    Text(
                        text = item.title,
                        color = iconColor,
                        style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 11.sp)
                    )
                },
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.title,
                        modifier = Modifier
                            .size(iconSize)
                            .graphicsLayer {
                                scaleX = if (isSelected) 1.1f else 1f
                                scaleY = if (isSelected) 1.1f else 1f
                            },
                        tint = iconColor
                    )
                }
            )
        }
    }
}



@Composable
fun previewNavigationItems(): List<NavigationItem> {
    return listOf(
        NavigationItem(
            id = 0,
            title = "Home",
            selectedIcon = ImageVector.vectorResource(android.R.drawable.star_on),
            unselectedIcon = ImageVector.vectorResource(android.R.drawable.star_off),
            root = Destination()
        ),
        NavigationItem(
            id = 1,
            title = "Search",
            selectedIcon = ImageVector.vectorResource(android.R.drawable.ic_menu_search),
            unselectedIcon = ImageVector.vectorResource(android.R.drawable.ic_menu_search),
            root = Destination()
        ),
        NavigationItem(
            id = 2,
            title = "Profile",
            selectedIcon = ImageVector.vectorResource(android.R.drawable.ic_menu_manage),
            unselectedIcon = ImageVector.vectorResource(android.R.drawable.ic_menu_manage),
            root = Destination()
        ),
    )
}

// Preview for BottomNavigationBar
@Preview(showBackground = true, name = "BottomNavigationBar Preview - Home Selected")
@Composable
fun BottomNavigationBarPreviewHome() {
    MaterialTheme {
        BottomNavigationBar(
            navigationItems = previewNavigationItems(),
            selected = 0,
            onNavigateTo = {}
        )
    }
}

@Preview(showBackground = true, name = "BottomNavigationBar Preview - Search Selected")
@Composable
fun BottomNavigationBarPreviewSearch() {
    MaterialTheme {
        BottomNavigationBar(
            navigationItems = previewNavigationItems(),
            selected = 1,
            onNavigateTo = {}
        )
    }
}

@Preview(showBackground = true, name = "BottomNavigationBar Preview - Profile Selected")
@Composable
fun BottomNavigationBarPreviewProfile() {
    MaterialTheme {
        BottomNavigationBar(
            navigationItems = previewNavigationItems(),
            selected = 2,
            onNavigateTo = {}
        )
    }
}
