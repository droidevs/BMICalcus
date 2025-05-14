package io.droidevs.bmicalc.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.droidevs.bmicalc.data.model.UnitSystem


@Composable
fun  UnitSystemToggle(
    unitSystem: UnitSystem,
    onToggle: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Text("Units:")
        Spacer(modifier = Modifier.width(8.dp))
        Switch(
            checked = unitSystem == UnitSystem.IMPERIAL,
            onCheckedChange = {
                onToggle.invoke()
            }
        )
        Text(
            text = if (unitSystem == UnitSystem.METRIC) "Metric" else "Imperial",
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}