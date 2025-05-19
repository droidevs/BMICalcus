package io.droidevs.bmicalc.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.droidevs.bmicalc.domain.model.BMICategory

@Composable
fun BmiResultSection(
    bmi: Float,
    modifier: Modifier = Modifier
) {
    val category by remember(bmi) {
        derivedStateOf {
            BMICategory.getCategory(bmi)
        }
    }
    val bmiColor = remember(category) { category.color }

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Your BMI Result",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(120.dp)
                    .background(
                        color = bmiColor.copy(alpha = 0.2f),
                        shape = CircleShape
                    )
                    .border(
                        width = 2.dp,
                        color = bmiColor,
                        shape = CircleShape
                    )
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "%.1f".format(bmi),
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "BMI",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Text(
                text = category.displayName,
                style = MaterialTheme.typography.titleLarge,
                color = bmiColor,
                modifier = Modifier.padding(top = 8.dp)
            )

            // BMI scale indicator
            BMIScaleIndicator(bmi, modifier = Modifier.padding(top = 16.dp))
        }
    }
}