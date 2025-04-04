package io.droidevs.bmicalc.ui.components

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider


@Composable
fun ProgressIndicator(
    modifier: Modifier = Modifier,
    progress: Float = -1F,
    strokeCap: StrokeCap = StrokeCap.Round,
    circular: Boolean = true,
) {
    if (progress <= -1F) {
        if (circular) {
            CircularProgressIndicator(
                modifier = modifier.testTag("circular-progress"),
                strokeCap = strokeCap,
            )
        } else {
            LinearProgressIndicator(
                modifier = modifier.testTag("linear-progress"),
                strokeCap = strokeCap,
            )
        }
    } else {
        if (circular) {
            CircularProgressIndicator(
                progress = { progress },
                modifier = modifier
                    .testTag("circular-progress")
                    .semantics { contentDescription = "Progress $progress" },
                strokeCap = strokeCap,
            )
        } else {
            LinearProgressIndicator(
                progress = { progress },
                modifier = modifier
                    .testTag("linear-progress")
                    .semantics { contentDescription = "Progress $progress" },
                strokeCap = strokeCap,
            )
        }
    }
}

// Define a data class for props
data class ProgressIndicatorProps(
    val progress: Float,
    val strokeCap: StrokeCap,
    val circular: Boolean,
    val modifier: Modifier
)

// Implement a CollectionPreviewParameterProvider for ProgressIndicatorProps
class ProgressIndicatorPropsProvider : CollectionPreviewParameterProvider<ProgressIndicatorProps>(
    listOf(
        ProgressIndicatorProps(progress = -1F, strokeCap = StrokeCap.Round, circular = true, modifier = Modifier.testTag("circular-default")),
        ProgressIndicatorProps(progress = 0.5F, strokeCap = StrokeCap.Butt, circular = false, modifier = Modifier.testTag("linear-half"))
    )
)

@Preview
@Composable
fun ProgressIndicatorPreview(
    @PreviewParameter(ProgressIndicatorPropsProvider::class) props: ProgressIndicatorProps
) {
    ProgressIndicator(
        modifier = props.modifier,
        progress = props.progress,
        strokeCap = props.strokeCap,
        circular = props.circular
    )
}
