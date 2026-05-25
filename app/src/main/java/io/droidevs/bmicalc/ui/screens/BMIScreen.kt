package io.droidevs.bmicalc.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.droidevs.bmicalc.domain.model.BmiResult
import io.droidevs.bmicalc.ui.components.AnimatedAppBar
import io.droidevs.bmicalc.ui.helper.actions.BmiCalculatorScreenAction
import io.droidevs.bmicalc.ui.components.BMIScaleIndicator
import io.droidevs.bmicalc.ui.components.BmiCategoriesTable
import io.droidevs.bmicalc.ui.components.BmiInputSection
import io.droidevs.bmicalc.ui.components.CurrentBmiCard
import io.droidevs.bmicalc.ui.components.GoalMotivationSection
import io.droidevs.bmicalc.ui.components.GoalProgressVisualization
import io.droidevs.bmicalc.ui.components.GoalTimeline
import io.droidevs.bmicalc.ui.components.MenuAppBar
import io.droidevs.bmicalc.ui.components.NoBmiRecordsCard
import io.droidevs.bmicalc.ui.components.UnitSystemToggle
import io.droidevs.bmicalc.ui.helper.states.BmiCalculatorState
import io.droidevs.bmicalc.ui.layouts.DoubleFoldedLayout
import io.droidevs.bmicalc.ui.layouts.DoubleLayoutWithScaffold
import io.droidevs.bmicalc.ui.layouts.CompactLayoutWithScaffold
import io.droidevs.bmicalc.ui.window.LocalWindow
import kotlinx.datetime.Instant

// BMIScreen.kt
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BMIScreen(
    state : BmiCalculatorState,
    onAction : (BmiCalculatorScreenAction) -> Unit,
    goToSettings: () -> Unit,
    toggleDrawer: () -> Unit
) {
    val layoutMode = LocalWindow.current.layoutMode
    if (layoutMode.isSplitScreen()){
        BMICalculatorSplitScreen(
            state = state,
            onAction = onAction,
            goToSettings = goToSettings,
            toggleDrawer = toggleDrawer
        )
    }
    else if (layoutMode.isSplitFoldable()){
        BmiCalculatorSplitFoldable(
            state = state,
            onAction = onAction,
            goToSettings = goToSettings,
            toggleDrawer = toggleDrawer
        )
    }
    else {
        CompactLayoutWithScaffold(
            topAppBar = {
                BmiCalculatorAppBar(
                    goToSettings = goToSettings,
                    toggleDrawer = toggleDrawer
                )
            },
            floatingActionButton = {},
        ) {
            LeftContent(
                state = state,
                onAction = onAction,
                enableScroll = false
            )
            RightContent(
                state = state,
                onAction = onAction,
                enableScroll = false
            )
        }
    }
}

@Composable
private fun BmiCalculatorSplitFoldable(
    state: BmiCalculatorState,
    onAction: (BmiCalculatorScreenAction) -> Unit,
    goToSettings: () -> Unit,
    toggleDrawer: () -> Unit
){
    DoubleFoldedLayout(
        topAppBar = {
            BmiCalculatorAppBar(
                goToSettings = goToSettings,
                toggleDrawer = toggleDrawer
            )
        },
        mainPanel = {
            LeftContent(
                state = state,
                onAction = onAction
            )
        },
        detailsPanel = {
            RightContent(
                state = state,
                onAction = onAction
            )
        }
    )
}

@Composable
private fun BMICalculatorSplitScreen(
    state: BmiCalculatorState,
    onAction: (BmiCalculatorScreenAction) -> Unit,
    goToSettings: () -> Unit,
    toggleDrawer: () -> Unit
){
    DoubleLayoutWithScaffold(
        topAppBar = {
            BmiCalculatorAppBar(
                goToSettings = goToSettings,
                toggleDrawer = toggleDrawer
            )
        },
        leftContent = {
            LeftContent(
                state = state,
                onAction = onAction
            )
        },
        rightContent = {
            RightContent(
                state = state,
                onAction = onAction
            )
        }
    )
}

@Composable
private fun BmiCalculatorAppBar(
    goToSettings: () -> Unit,
    toggleDrawer: () -> Unit
){
    MenuAppBar(
        title = "BMI Calculator",
        onMenuPressed = toggleDrawer,
        onSettingPressed = goToSettings
    )
}

@Composable
private fun RightContent(
    state: BmiCalculatorState,
    onAction: (BmiCalculatorScreenAction) -> Unit,
    enableScroll: Boolean = true,
){
    val scrollModifier = if (enableScroll) {
        Modifier.verticalScroll(rememberScrollState())
    } else {
        Modifier
    }
    val sizeModifier = if (enableScroll) {
        Modifier.fillMaxSize()
    } else {
        Modifier.fillMaxWidth()
    }
    Column(
        modifier = sizeModifier.then(scrollModifier),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        state.bmiScore?.let {
            CurrentBmiCard(
                currentBmi = state.bmiScore.value,
                modifier = Modifier.fillMaxWidth()
            )
        }?: NoBmiRecordsCard(modifier = Modifier.fillMaxWidth())
        state.activeBmiGoal?.let { goal ->
            GoalProgressVisualization(
                initialBmi = goal.initialBmi,
                currentBmi = state.bmiScore?.value?: 0f,
                targetBmi = goal.targetBmi,
                progress = state.goalStatus,
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

            OutlinedButton(
                onClick = {
                    onAction(BmiCalculatorScreenAction.SetUpOrManageGoal)
                },
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(0.5f)
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary,
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Goal",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Edit Goal",
                    fontWeight = FontWeight.Medium
                )
            }
        } ?: run {
            // Placeholder/motivational content when no goal is set
            GoalMotivationSection(
                modifier = Modifier.fillMaxWidth()
            )

            FilledIconButton(
                onClick = {
                    onAction(BmiCalculatorScreenAction.SetUpOrManageGoal)
                },
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth(0.5f)
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Goal",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Go",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
@Composable
private fun LeftContent(
    state : BmiCalculatorState,
    onAction : (BmiCalculatorScreenAction) -> Unit,
    enableScroll: Boolean = true,
){
    val scrollModifier = if (enableScroll) {
        Modifier.verticalScroll(rememberScrollState())
    } else {
        Modifier
    }
    val sizeModifier = if (enableScroll) {
        Modifier.fillMaxSize()
    } else {
        Modifier.fillMaxWidth()
    }
    Column(
        modifier = sizeModifier.then(scrollModifier),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        UnitSystemToggle(
            unitSystem = state.unitSystem,
            onToggle = {
                onAction(BmiCalculatorScreenAction.ToggleUnitSystem)
            }
        )

        BmiInputSection(
            unitSystem = state.unitSystem,
            height = state.height,
            weight = state.weight,
            onChangeHeight = { onAction(BmiCalculatorScreenAction.ChangeHeight(it)) },
            onChangeWeight = { onAction(BmiCalculatorScreenAction.ChangeWeight(it)) },
            validation = state.validation
        )

        OutlinedButton(
            onClick = {
                onAction(BmiCalculatorScreenAction.CalculateBmi)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .height(48.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            enabled = state.validation.isHeightValid && state.validation.isWeightValid // Add your validation check
        ) {
            Text(
                text = "Calculate BMI",
                fontWeight = FontWeight.SemiBold
            )
        }
    }

        // BMI Result
        state.bmiResult?.let { result ->
            BmiResultSection(
                result = result
            )
        } ?: Text(
            "Enter your height and weight to calculate BMI",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )

        BmiCategoriesTable()
}

@Composable
private fun BmiResultSection(
    result : BmiResult
){
    val animatedBMI by animateFloatAsState(
        targetValue = result.value,
        animationSpec = tween(durationMillis = 1000), label = ""
    )

    Card(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(vertical = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = result.category.color.copy(alpha = 0.2f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Your BMI",
                style = MaterialTheme.typography.labelLarge
            )
            Text(
                "%.1f".format(animatedBMI),
                style = MaterialTheme.typography.displayLarge,
                color = result.category.color
            )
            Text(
                result.category.displayName,
                style = MaterialTheme.typography.headlineSmall,
                color = result.category.color
            )
        }
    }

    // BMI Scale
    BMIScaleIndicator(bmiValue = result.value)
}
