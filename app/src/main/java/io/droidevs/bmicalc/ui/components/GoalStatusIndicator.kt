package io.droidevs.bmicalc.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.droidevs.bmicalc.R
import io.droidevs.bmicalc.domain.GoalStatus
import io.droidevs.bmicalc.domain.GoalStatus.ACHIEVED
import io.droidevs.bmicalc.domain.GoalStatus.AHEAD_OF_SCHEDULE
import io.droidevs.bmicalc.domain.GoalStatus.BEHIND_SCHEDULE
import io.droidevs.bmicalc.domain.GoalStatus.NOT_SET
import io.droidevs.bmicalc.domain.GoalStatus.ON_TRACK

@Composable
fun GoalStatusIndicator(status: GoalStatus) {

    val icon = when (status) {
        NOT_SET -> ImageVector.vectorResource(R.drawable.ic_goal)
        ON_TRACK -> ImageVector.vectorResource(R.drawable.ic_run)
        BEHIND_SCHEDULE -> ImageVector.vectorResource(R.drawable.ic_warning)
        AHEAD_OF_SCHEDULE -> ImageVector.vectorResource(R.drawable.ic_forward)
        ACHIEVED -> ImageVector.vectorResource(R.drawable.ic_check_circle)
    }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = status.color,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = status.toDisplayText(),
            color = status.color,
            fontWeight = FontWeight.Medium
        )
    }
}