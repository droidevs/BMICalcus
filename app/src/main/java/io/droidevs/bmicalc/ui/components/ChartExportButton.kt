package io.droidevs.bmicalc.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun ChartExportButton(
    modifier: Modifier = Modifier,
    exportChartAsImage: () -> Unit,
    exportDataAsCsv: () -> Unit
) {
    var showExportOptions by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        IconButton(onClick = { showExportOptions = true }) {
            Icon(Icons.Default.Share, "Export Chart")
        }

        DropdownMenu(
            expanded = showExportOptions,
            onDismissRequest = { showExportOptions = false }
        ) {
            DropdownMenuItem(
                text = { Text("Save as Image") },
                onClick = {
                    exportChartAsImage()
                    showExportOptions = false
                }
            )

            DropdownMenuItem(
                text = { Text("Export Data (CSV)") },
                onClick = {
                    exportDataAsCsv()
                    showExportOptions = false
                }
            )
        }
    }
}

