package io.droidevs.bmicalc.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.droidevs.bmicalc.R
import io.droidevs.bmicalc.data.model.HeightUnit
import io.droidevs.bmicalc.data.model.UnitSystem
import io.droidevs.bmicalc.data.model.WeightUnit
import io.droidevs.bmicalc.domain.model.getCategory
import io.droidevs.bmicalc.ui.components.BMIScaleIndicator
import io.droidevs.bmicalc.ui.components.MeasurementCard
import io.droidevs.bmicalc.ui.components.NoteDialog
import io.droidevs.bmicalc.ui.helper.actions.BmiRecordDetailsAction
import io.droidevs.bmicalc.ui.helper.states.BmiRecordDetailScreenState
import io.droidevs.bmicalc.ui.model.BmiRecordUi

@Composable
fun BmiRecordDetailsScreen(
    state: BmiRecordDetailScreenState,
    onAction : (BmiRecordDetailsAction) -> Unit
) {
    val category = getCategory(state.record.bmi)
    val colorTransition by animateColorAsState(
        targetValue = category.color,
        animationSpec = tween(durationMillis = 800), label = ""
    )

    val animatedProgress by animateFloatAsState(
        targetValue = (state.record.bmi / 40f).coerceIn(0f,1f),
        animationSpec = spring(
            dampingRatio = 0.6f,
            stiffness = Spring.StiffnessLow
        ), label = ""
    )
    // Favorite animation state
    var isFavorite by remember(state) { mutableStateOf(state.record.isFavorite) }
    val favoriteScale by animateFloatAsState(
        targetValue = if (isFavorite) 1.2f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "favoriteScale"
    )

    Card(
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(8.dp),
    ) {
        Box {

            Column(
                modifier = Modifier
                    .background(colorTransition.copy(alpha = 0.1f))
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Animated BMI Value
                AnimatedContent(
                    targetState = state.record.bmi,
                    transitionSpec = {
                        slideInVertically { height -> height } +
                                fadeIn() togetherWith
                                slideOutVertically { height -> -height } +
                                fadeOut()
                    }, label = ""
                ) { bmiValue ->
                    Text(
                        text = "%.1f".format(bmiValue),
                        style = MaterialTheme.typography.displayLarge,
                        color = colorTransition,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                BMIScaleIndicator(
                    bmiValue = animatedProgress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .padding(vertical = 16.dp)
                )

                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Column(
                        modifier = Modifier.padding(top = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Category Chip
                        AnimatedContent(
                            targetState = category,
                            transitionSpec = {
                                scaleIn() togetherWith scaleOut()
                            }, label = ""
                        ) { category ->

                            Text(
                                text = category.displayName,
                                style = MaterialTheme.typography.titleLarge,
                                color = colorTransition,
                                modifier = Modifier
                                    .background(
                                        color = colorTransition.copy(alpha = 0.2f),
                                        shape = CircleShape
                                    )
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Measurements

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            val heightUnit = HeightUnit.getUnit(state.unitSystem)
                            val weightUnit = WeightUnit.getUnit(state.unitSystem)

                            MeasurementCard(
                                value = UnitSystem.DEFAULT.convertHeight(state.record.height, state.unitSystem),
                                unit = heightUnit.text,
                                icon = ImageVector.vectorResource(R.drawable.ic_height),
                                color = colorTransition
                            )
                            MeasurementCard(
                                value = UnitSystem.DEFAULT.convertWeight(state.record.weight, state.unitSystem),
                                unit = weightUnit.text,
                                icon = ImageVector.vectorResource(R.drawable.ic_weight),
                                color = colorTransition
                            )
                        }
                    }
                }
            }

            // Action buttons (top-right)
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Delete button
                    IconButton(
                        onClick = {
                            onAction(BmiRecordDetailsAction.DeleteAction)
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                color = MaterialTheme.colorScheme.errorContainer,
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }

                    var showFavoredNoteDialog by remember {  mutableStateOf(false) }
                    // Favorite button
                    IconButton(
                        onClick = {
                            isFavorite = !isFavorite
                            if (isFavorite)
                                showFavoredNoteDialog = true
                            else
                                onAction(BmiRecordDetailsAction.UnfavoriteAction)
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .graphicsLayer { scaleX = favoriteScale; scaleY = favoriteScale }
                            .background(
                                color = if (isFavorite) MaterialTheme.colorScheme.primaryContainer
                                else MaterialTheme.colorScheme.surfaceVariant,
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite
                            else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    NoteDialog(
                        openDialog = showFavoredNoteDialog,
                        onDismiss = { showFavoredNoteDialog = false },
                        onNoteEntered = { note ->
                            onAction(BmiRecordDetailsAction.FavoriteAction(note))
                        }
                    )
                }
            }
        }
    }
}

