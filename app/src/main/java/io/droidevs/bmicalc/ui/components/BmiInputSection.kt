package io.droidevs.bmicalc.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.room.RoomOpenHelper
import io.droidevs.bmicalc.R
import io.droidevs.bmicalc.data.model.HeightUnit
import io.droidevs.bmicalc.ui.window.SizeClass
import io.droidevs.bmicalc.data.model.UnitSystem
import io.droidevs.bmicalc.data.model.WeightUnit
import io.droidevs.bmicalc.domain.model.BmiInputValidationResult
import io.droidevs.bmicalc.domain.model.ValidationError
import io.droidevs.bmicalc.ui.window.LocalWindow

@Composable
fun BmiInputSection(
    unitSystem: UnitSystem,
    height: Float?,
    weight: Float?,
    validation: BmiInputValidationResult,
    onChangeHeight: (Float?) -> Unit,
    onChangeWeight: (Float?) -> Unit,
    modifier: Modifier = Modifier
) {
    val screenSizeClass = LocalWindow.current.windowSize.getClass()
    // Responsive sizing
    val textFieldWidth by animateDpAsState(
        when (screenSizeClass.widthClass) {
            SizeClass.Compact -> 180.dp
            SizeClass.Medium -> 240.dp
            SizeClass.Expanded -> 300.dp
            else -> 180.dp
        }, label = ""
    )

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
                .width(textFieldWidth),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Enter Your Measurements",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = if (height != null) UnitSystem.DEFAULT.convertHeight(height, unitSystem).toString() else "",
                onValueChange = { newValue ->
                    val sanitized = newValue.replace(',', '.')
                    val parsed = sanitized.toFloatOrNull()
                    when {
                        sanitized.isBlank() -> onChangeHeight(null)
                        parsed != null -> onChangeHeight(unitSystem.convertHeight(parsed, UnitSystem.DEFAULT))
                    }
                },
                label = { Text(if (unitSystem == UnitSystem.METRIC) "Height (cm)" else "Height (in)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    Text(
                        if (unitSystem == UnitSystem.METRIC) "cm" else "in",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                shape = MaterialTheme.shapes.large,
                isError = validation.heightError != null,
                supportingText = {
                    validation.heightError?.let {
                        when(it){
                            is ValidationError.OutOfRangeError -> {
                                Text(
                                    text = stringResource(
                                        R.string.validation_error_height_range,
                                        it.min,
                                        it.max
                                    )
                                )
                            }
                        }
                    }
                }
            )

            OutlinedTextField(
                value = if (weight != null) UnitSystem.DEFAULT.convertWeight(weight, unitSystem).toString() else "",
                onValueChange = { newValue ->
                    val sanitized = newValue.replace(',', '.')
                    val parsed = sanitized.toFloatOrNull()
                    when {
                        sanitized.isBlank() -> onChangeWeight(null)
                        parsed != null -> onChangeWeight(unitSystem.convertWeight(parsed, UnitSystem.DEFAULT))
                    }
                },
                label = { Text(if (unitSystem == UnitSystem.METRIC) "Weight (kg)" else "Weight (lbs)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    Text(
                        if (unitSystem == UnitSystem.METRIC) "kg" else "lb",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                shape = MaterialTheme.shapes.large,
                isError = validation.weightError != null,
                supportingText = {
                    validation.weightError?.let {
                        when(it){
                            is ValidationError.OutOfRangeError -> {
                                Text(
                                    text = stringResource(
                                        R.string.validation_error_weight_range,
                                        it.min,
                                        it.max
                                    )
                                )
                            }
                        }
                    }
                }
            )
        }
    }
}
