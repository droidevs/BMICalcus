package io.droidevs.bmicalc.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.droidevs.bmicalc.domain.model.BmiRecord

@Composable
fun StatsSummary(data: List<BmiRecord>) {
    val stats = remember(data) {
        if (data.isEmpty()) return@remember null

        val bmiValues = data.map { it.bmi }
        val weightValues = data.map { it.weight }

        mapOf(
            "Average BMI" to bmiValues.average().toString(),
            "BMI Change" to (bmiValues.last() - bmiValues.first()).toString(),
            "Max Weight" to (weightValues.maxOrNull()?: "0.0").toString(),
            "Min Weight" to (weightValues.minOrNull()?: "0.0").toString(),
            "Records" to data.size.toString()
        )
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.padding(8.dp)
    ) {
        stats?.forEach { (title, value) ->
            item {
                StatCard(title, value)
            }
        }
    }
}