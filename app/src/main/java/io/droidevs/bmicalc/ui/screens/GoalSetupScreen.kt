package io.droidevs.bmicalc.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import io.droidevs.bmicalc.R
import io.droidevs.bmicalc.domain.GoalStatus
import io.droidevs.bmicalc.ui.components.BackAppBar
import io.droidevs.bmicalc.ui.components.CurrentBmiCard
import io.droidevs.bmicalc.ui.components.GoalMotivationSection
import io.droidevs.bmicalc.ui.components.GoalProgressVisualization
import io.droidevs.bmicalc.ui.components.GoalTimeline
import io.droidevs.bmicalc.ui.components.NoBmiRecordsCard
import io.droidevs.bmicalc.ui.components.OffTrackIndicator
import io.droidevs.bmicalc.ui.helper.actions.BmiGoalAction
import io.droidevs.bmicalc.ui.helper.states.BmiGoalSetUpScreenState
import io.droidevs.bmicalc.ui.model.AppBarMenuItem
import io.droidevs.bmicalc.ui.window.LocalWindow
import io.droidevs.bmicalc.utils.format
import kotlinx.datetime.Instant
import kotlinx.datetime.toLocalDateTime

@Composable
fun GoalSetupScreen(
    state : BmiGoalSetUpScreenState,
    onAction: (BmiGoalAction) -> Unit,
    onNavigateBack: () -> Unit = {}
) {
    val layoutMode = LocalWindow.current.layoutMode
    Scaffold(
        topBar = {
            BackAppBar(
                title = "Set BMI Goal",
                onBackPressed = onNavigateBack
            )
        }
    ) { paddingValues ->
        if (layoutMode.isSplitScreen()) {
            ExpandedGoalLayout(
                uiState = state,
                onAction = onAction,
                modifier = Modifier.padding(paddingValues)
            )
        } else {
            CompactGoalLayout(
                uiState = state,
                onAction = onAction,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
private fun CompactGoalLayout(
    uiState: BmiGoalSetUpScreenState,
    onAction: (BmiGoalAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Current BMI Card (only shown when setting new goal)
        if (uiState.activeBmiGoal == null) {
            uiState.bmiScore?.let {
                CurrentBmiCard(
                    currentBmi = uiState.bmiScore.value,
                    modifier = Modifier.fillMaxWidth()
                )
            }?: NoBmiRecordsCard(modifier = Modifier.fillMaxWidth())
        }

        GoalSettingSection(
            state = uiState,
            onAction = onAction,
            modifier = Modifier.fillMaxWidth()
        )

        // Progress visualization takes more space when in compact mode
        uiState.activeBmiGoal?.let { goal ->
            GoalProgressVisualization(
                currentBmi = uiState.bmiScore?.value?: 0f,
                targetBmi = goal.targetBmi,
                progress = uiState.goalStatus,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ExpandedGoalLayout(
    uiState: BmiGoalSetUpScreenState,
    onAction: (BmiGoalAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Left Column - Form and current status
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (uiState.activeBmiGoal == null) {
                uiState.bmiScore?.let {
                    CurrentBmiCard(
                        currentBmi = uiState.bmiScore.value,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            GoalSettingSection(
                state = uiState,
                onAction = onAction,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Right Column - Progress visualization
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            uiState.activeBmiGoal?.let { goal ->
                GoalProgressVisualization(
                    currentBmi = uiState.bmiScore?.value?: 0f,
                    targetBmi = goal.targetBmi,
                    progress = uiState.goalStatus,
                    modifier = Modifier.fillMaxWidth()
                )

                // Additional space for more detailed visualization in expanded mode
                GoalTimeline(
                    startDate = Instant.fromEpochMilliseconds(goal.initialDate),
                    targetDate = goal.targetDate?.let {
                        Instant.fromEpochMilliseconds(it)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            } ?: run {
                // Placeholder/motivational content when no goal is set
                GoalMotivationSection(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalSettingSection(
    state : BmiGoalSetUpScreenState,
    onAction: (BmiGoalAction) -> Unit,
    modifier: Modifier = Modifier
) {

    val isEditable = state.goalStatus.isEditable
    val alpha by animateFloatAsState(
        targetValue = if (isEditable) 1f else 0.6f,
        animationSpec = tween(durationMillis = 300), label = ""
    )

    var showDatePicker by remember { mutableStateOf(false) }
    var showConfirmAbandon by remember { mutableStateOf(false) }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                Button(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        onAction(BmiGoalAction.SetTargetDate(it))
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Show warning if behind schedule
            if (state.goalStatus == GoalStatus.BEHIND_SCHEDULE) {
                OffTrackIndicator(modifier = Modifier.fillMaxWidth())
            }
            // Header with status
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (state.activeBmiGoal == null) "Set New Goal" else "Manage Your Goal",
                    style = MaterialTheme.typography.titleLarge
                )

                // Status chip with animation
                if (state.activeBmiGoal != null) {
                    val backgroundColor by animateColorAsState(
                        when (state.goalStatus) {
                            GoalStatus.BEHIND_SCHEDULE -> Color(0xFFD32F2F).copy(alpha = 0.2f)
                            else -> MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        }, label = ""
                    )

                    val textColor by animateColorAsState(
                        when (state.goalStatus) {
                            GoalStatus.BEHIND_SCHEDULE -> Color(0xFFD32F2F)
                            else -> MaterialTheme.colorScheme.primary
                        }, label = ""
                    )

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(backgroundColor)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = state.goalStatus.name.lowercase()
                                .replaceFirstChar { it.uppercase() }
                                .replace("_", " "),
                            color = textColor,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }

            // Target BMI input
            OutlinedTextField(
                value = state.goalInput?.targetBmi.toString(),
                onValueChange = { if (isEditable) onAction(BmiGoalAction.SetTargetBmi(it)) },
                label = { Text("Target BMI") },
                singleLine = true,
                enabled = isEditable,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                prefix = { Text("BMI", modifier = Modifier.padding(end = 4.dp)) },
                trailingIcon = {
                    Icon(Icons.Default.Info, contentDescription = "BMI Info")
                }
            )

            // Target date selector
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = { if (isEditable) showDatePicker = true },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = state.goalInput?.targetDate?.let {
                            Instant.fromEpochMilliseconds(it).format("MMM d, yyyy")
                        } ?: "Select Target Date (Optional)"
                    )
                }

                if (state.goalInput?.targetDate != null && isEditable) {
                    IconButton(
                        onClick = { onAction(BmiGoalAction.SetTargetDate(null)) }
                    ) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear date")
                    }
                }
            }

            // Motivation input
            OutlinedTextField(
                value = state.goalInput?.motivation ?: "",
                onValueChange = { if (isEditable) onAction(BmiGoalAction.SetMotivation(it)) },
                label = { Text("Motivation (Optional)") },
                modifier = Modifier.fillMaxWidth(),
                enabled = isEditable,
                maxLines = 3,
                trailingIcon = {
                    Icon(ImageVector.vectorResource(R.drawable.ic_light_bulb), contentDescription = "Motivation")
                }
            )

            // Action buttons - always show abandon regardless of status
            if (state.activeBmiGoal == null) {
                Button(
                    onClick = { onAction(BmiGoalAction.SaveGoal) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    enabled = state.goalInput.targetBmi != null
                ) {
                    Text("Set Goal")
                }
            } else {
                // Primary action button changes based on status
                when (state.goalStatus) {
                    GoalStatus.BEHIND_SCHEDULE -> {
                        Button(
                            onClick = { /* Show adjustment suggestions */ },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFFCDD2),
                                contentColor = Color(0xFFD32F2F)
                            )
                        ) {
                            Text("Get Back On Track")
                        }
                    }
                    else -> {
                        if (isEditable) {
                            Button(
                                onClick = { onAction(BmiGoalAction.SaveGoal) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                            ) {
                                Text("Update Goal")
                            }
                        }
                    }
                }

                // Always show abandon button
                OutlinedButton(
                    onClick = { showConfirmAbandon = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    border = BorderStroke(1.dp, Color(0xFFD32F2F)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFD32F2F)
                    )
                ) {
                    Text("Abandon Goal")
                }

                // Show complete button if not behind schedule
                if (state.goalStatus != GoalStatus.BEHIND_SCHEDULE && state.bmiScore != null) {
                    Button(
                        onClick = { onAction(BmiGoalAction.CompleteGoal) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50),
                            contentColor = Color.White)
                    ) {
                        Text("Mark as Completed")
                    }
                }

                // Abandon confirmation dialog
                if (showConfirmAbandon) {
                    AlertDialog(
                        onDismissRequest = { showConfirmAbandon = false },
                        title = { Text("Abandon Goal?") },
                        text = {
                            Text("This will archive your current goal as abandoned. " +
                                    "You can view it in your history later.")
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    onAction(BmiGoalAction.AbandonGoal)
                                    showConfirmAbandon = false
                                }
                            ) {
                                Text("Confirm", color = MaterialTheme.colorScheme.error)
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = { showConfirmAbandon = false }
                            ) {
                                Text("Cancel")
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun disabledTextFieldColors(): TextFieldColors {
    return OutlinedTextFieldDefaults.colors(
        disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
        disabledContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
        disabledLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
        disabledTrailingIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
        disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
    )
}
