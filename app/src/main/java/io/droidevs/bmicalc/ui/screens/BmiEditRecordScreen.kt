package io.droidevs.bmicalc.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import io.droidevs.bmicalc.ui.components.BMIScaleIndicator
import io.droidevs.bmicalc.ui.components.BmiInputSection
import io.droidevs.bmicalc.ui.components.BmiResultSection
import io.droidevs.bmicalc.ui.helper.actions.BmiRecordEditAction
import io.droidevs.bmicalc.ui.helper.states.BmiRecordEditState
import io.droidevs.bmicalc.ui.model.BmiRecordUi
import io.droidevs.bmicalc.ui.window.LayoutMode
import io.droidevs.bmicalc.ui.window.LocalWindow

@Composable
fun BmiEditRecordScreen(
    state: BmiRecordEditState,
    onAction: (BmiRecordEditAction) -> Unit
) {
    // Compact/portrait layout
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BmiInputSection(
            unitSystem = state.unitSystem,
            height = state.edited.height,
            weight = state.edited.weight,
            validation = state.validationResult,
            onChangeHeight = { onAction(BmiRecordEditAction.ChangeRecordWeight(it)) },
            onChangeWeight = { onAction(BmiRecordEditAction.ChangeRecordHeight(it)) },
            modifier = Modifier.fillMaxWidth()
        )

        BmiResultSection(
            bmi = state.edited.bmi,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                onAction(BmiRecordEditAction.SaveRecord)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
        enabled = state.validationResult.isHeightValid && state.validationResult.isWeightValid,
        ) {
        Text("Save Measurements")
    }
    }
}