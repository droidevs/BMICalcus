package io.droidevs.bmicalc.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil


@Composable
fun GoalTimeline(
    startDate: Instant,
    targetDate: Instant?,
    modifier: Modifier = Modifier
) {
    targetDate?.let {
        val timeZone = TimeZone.currentSystemDefault()
        val today = Clock.System.now()
        val totalDays = startDate.daysUntil(other = targetDate, timeZone = timeZone).toFloat()
        val remainingDays = today.daysUntil(other = targetDate,timeZone = timeZone ).toFloat()

        Card(
            modifier = modifier,
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Timeline",
                    style = MaterialTheme.typography.titleMedium
                )

                LinearProgressIndicator(
                    progress = { 1 - (remainingDays / totalDays) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Today", style = MaterialTheme.typography.labelSmall)
                    Text("Target", style = MaterialTheme.typography.labelSmall)
                }

                Text(
                    text = "${remainingDays.toInt()} days remaining",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}