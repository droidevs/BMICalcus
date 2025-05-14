package io.droidevs.bmicalc.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.droidevs.bmicalc.domain.GoalStatus


@Composable
fun GoalProgressVisualization(
    currentBmi: Float,
    targetBmi: Float,
    progress: GoalStatus,
    modifier: Modifier = Modifier
) {
    val progressColor = progress.color

    val progressText = progress.description

    val progressValue by remember(currentBmi, targetBmi) {
        derivedStateOf {
            (currentBmi / targetBmi).coerceIn(0f, 1f)
        }
    }

    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Progress bar
            LinearProgressIndicator(
                progress = { progressValue },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(12.dp)),
                color = progressColor,
                trackColor = MaterialTheme.colorScheme.surfaceVariant //progressColor.copy(alpha = 0.2f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Progress text
            Text(
                text = progressText,
                style = MaterialTheme.typography.labelLarge,
                color = progressColor
            )

            // BMI values
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Current: %.1f".format(currentBmi),
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Goal: %.1f".format(targetBmi),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            GoalStatusIndicator(
                status = progress
            )
        }
    }
}