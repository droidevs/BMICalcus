package io.droidevs.bmicalc.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun ShrimmerCard(
    isLoading: Boolean = false,
    content: @Composable () -> Unit,
    placeholder: @Composable () -> Unit
){
    if (isLoading)
        placeholder.invoke()
    else
        content.invoke()
}


fun shrimmerCount(
    contentHeight: Dp ,
    shrimmerHeight : Dp,
    cols : Int = 1
) : Int {
    return cols * (contentHeight / shrimmerHeight).toInt() + 1
}

// Define a data class for ShrimmerCard props
data class ShrimmerCardProps(
    val isLoading: Boolean,
    val contentHeight: Dp,
    val shrimmerHeight: Dp,
    val cols: Int
)

// Implement a CollectionPreviewParameterProvider for ShrimmerCardProps
class ShrimmerCardPropsProvider : CollectionPreviewParameterProvider<ShrimmerCardProps>(
    listOf(
        ShrimmerCardProps(isLoading = true, contentHeight = 200.dp, shrimmerHeight = 50.dp, cols = 1),
        ShrimmerCardProps(isLoading = false, contentHeight = 150.dp, shrimmerHeight = 30.dp, cols = 2)
    )
)

@Preview
@Composable
fun ShrimmerPreview(
    @PreviewParameter(ShrimmerCardPropsProvider::class) props: ShrimmerCardProps
) {
    ShrimmerCard(
        isLoading = props.isLoading,
        content = {
            Modifier.height(props.contentHeight)
        },
        placeholder = {

        }
    )
}
