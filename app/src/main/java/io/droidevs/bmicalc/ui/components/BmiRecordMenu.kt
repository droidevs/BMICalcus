package io.droidevs.bmicalc.ui.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import io.droidevs.bmicalc.R


@Composable
fun BmiRecordMenu(
    onDelete: () -> Unit,
    onFavorite: () -> Unit,
    onDismiss: () -> Unit,
){
    val favoriteInteractionSource = remember { MutableInteractionSource() }
    val deleteInteractionSource = remember { MutableInteractionSource() }

    val isFavoritePressed = favoriteInteractionSource.collectIsPressedAsState()
    val isDeletePressed = deleteInteractionSource.collectIsPressedAsState()

    val favoriteTransition = updateTransition(isFavoritePressed, label = "")
    val deleteTransition = updateTransition(isDeletePressed, label = "")

    val favoriteContainerColor = favoriteTransition.animateColor(label = "") { isPressed->
        if (isPressed.value)
            Color.Green.copy(alpha = 0.7f)
        else
            Color.Transparent
    }

    val deleteContainerColor = deleteTransition.animateColor(label = "") { isPressed ->
        if (isPressed.value)
            Color.Red.copy(alpha = 0.7f)
        else
            Color.Transparent
    }

    Popup(
        alignment = Alignment.Center,
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .heightIn(400.dp, 600.dp),
        ) {
            TextButton(
                modifier = Modifier,
                colors = ButtonDefaults.buttonColors(
                    containerColor = favoriteContainerColor.value
                ),
                interactionSource = favoriteInteractionSource,
                onClick = onFavorite
            ){
                Text(
                    text = stringResource(R.string.favorite),
                    style = MaterialTheme.typography.labelLarge
                )
            }
            HorizontalDivider(thickness = 1.dp)
            TextButton(
                onClick = onDelete,
                colors = ButtonDefaults.buttonColors(
                    containerColor = deleteContainerColor.value
                )
            ) {
                Text(
                    text = stringResource(R.string.delete),
                    style = MaterialTheme.typography.labelLarge
                )
            }

        }
    }
}