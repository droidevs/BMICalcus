package io.droidevs.bmicalc.ui.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.droidevs.bmicalc.domain.model.BMICategory
import io.droidevs.bmicalc.data.model.UnitSystem
import io.droidevs.bmicalc.ui.model.BmiRecordUi
import io.droidevs.bmicalc.ui.window.LayoutMode
import io.droidevs.bmicalc.ui.window.LocalWindow
import kotlinx.datetime.Instant


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun BmiRecordCard(
    unitSystem: UnitSystem,
    record: BmiRecordUi,
    onClick: () -> Unit,
    onFavorite: () -> Unit,
    onDelete: () -> Unit
) {
    val layoutMode = LocalWindow.current.layoutMode
    val isVertical = remember(layoutMode) {
        derivedStateOf{
            layoutMode == LayoutMode.PHONE_LANDSCAPE
        }
    }
    var showDeleteDialog by remember { mutableStateOf(false) }
    val category = BMICategory.getCategory(record.bmi)
    val categoryColor = category.color

    val interactionSource = remember { MutableInteractionSource() }
    var showMenu by remember { mutableStateOf(false) }
    val isPressed = interactionSource.collectIsPressedAsState()

    val transition = updateTransition(isPressed.value, label = "")
    val elevation = transition.animateDp(
        label = ""
    ) { isPressed ->
        if (isPressed)
            3.dp
        else
            1.dp
    }
    val backgroundColor = transition.animateColor(label = "") { isPressed ->
        if (isPressed)
            MaterialTheme.colorScheme.surfaceVariant
        else
            categoryColor
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .combinedClickable(
                interactionSource = interactionSource,
                onClick = onClick,
                onLongClick = {
                    showMenu = true
                },
                indication = LocalIndication.current
            ),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor.value
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = elevation.value
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "BMI: ${"%.1f".format(record.bmi)}",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = category.displayName,
                    color = categoryColor,
                    style = MaterialTheme.typography.labelLarge
                )
            }
            if (!isVertical.value) {
                Spacer(modifier = Modifier.height(8.dp))

                val displayHeight = UnitSystem.METRIC.convertHeight(record.height, unitSystem)
                val displayWeight = UnitSystem.METRIC.convertWeight(record.weight, unitSystem)
                val heightUnit = if (unitSystem == UnitSystem.METRIC) "cm" else "in"
                val weightUnit = if (unitSystem == UnitSystem.METRIC) "kg" else "lb"

                Text(
                    text = "Height: %.1f %s".format(displayHeight, heightUnit),
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Weight: %.1f %s".format(displayWeight, weightUnit),
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                val formatDate = rememberSmartDateFormat()
                Text(text = formatDate(Instant.fromEpochMilliseconds(record.date)))
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Record") },
            text = { Text("Are you sure you want to delete this BMI record?") },
            confirmButton = {
                TextButton(onClick = {
                    onDelete()
                    showDeleteDialog = false
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showMenu){
        BmiRecordMenu(
            onDelete = { showDeleteDialog = true },
            onFavorite = onFavorite,
            onDismiss = { showMenu = false }
        )
    }
}


@Composable
fun BmiRecordCardWithActions(
    revealedDirection: DirectionMode,
    unitSystem: UnitSystem,
    record: BmiRecordUi,
    onClick: () -> Unit,
    onFavorite: () -> Unit,
    onDelete: () -> Unit,
    onExpanded: (direction: DirectionMode) -> Unit,
    onCollapse: () -> Unit,
){
    SwipeableItemWithActions(
        revealedDirection = revealedDirection,
        startAction = {
            ActionIcon(
                onClick = onFavorite,
                icon = Icons.Default.Favorite,
                backgroundColor = Color.Green.copy(alpha = 0.7f),
                tint = MaterialTheme.colorScheme.onBackground
            )
        },
        endAction = {
            ActionIcon(
                onClick = onDelete,
                icon = Icons.Default.Delete,
                backgroundColor = Color.Red.copy(alpha = 0.7f),
                tint = MaterialTheme.colorScheme.onBackground
            )
        },
        onExpanded = onExpanded,
        onCollapsed = onCollapse,
        content = {
            BmiRecordCard(
                unitSystem = unitSystem,
                record = record,
                onClick = onClick,
                onDelete = onDelete,
                onFavorite = onFavorite
            )
        }
    )
}

private fun Float.convert(currentUnitSystem: UnitSystem, targetUnitSystem: UnitSystem): Float{
    if (currentUnitSystem == targetUnitSystem)
        return this
    return currentUnitSystem.convertWeight(this, targetUnitSystem)
}
