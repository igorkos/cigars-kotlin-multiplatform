import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.akellolcc.cigars.theme.TextStyles
import com.akellolcc.cigars.theme.textStyle


@Composable
public fun TextStyled(text: String?, style: TextStyles = TextStyles.Headline, label: String? = null, labelStyle: TextStyles = TextStyles.Headline, maxLines: Int = Int.MAX_VALUE,
                      minLines: Int = 1, editable: Boolean = false,  modifier: Modifier = Modifier, maxHeight: Int = 0, keepHeight: Boolean = true
) {
    val textStyle = textStyle(style)
    val styleLabel = textStyle(labelStyle)
    val textMeasurer = rememberTextMeasurer()
    var textWith by remember { mutableStateOf(0) }
    var textHeight by remember { mutableStateOf(maxHeight.dp) }
    val scrollState = rememberScrollState()
    if (editable) {
        TextField(
            value = text ?: "",
            label = { TextStyled(text = label ?: "", style = TextStyles.Description, maxLines = 1, minLines = 1) },
            onValueChange = {},
            minLines = minLines,
            maxLines = maxLines,
            textStyle = textStyle)
    } else {
            val density = LocalDensity.current
            if(maxLines != Int.MAX_VALUE) {
                LazyColumn (
                    modifier = modifier
                        .height(textHeight)
                        .onSizeChanged {
                            //Log.debug("Text size for '$text' -> $it maxHeight $maxHeight")
                            textWith = it.width
                            val oneLine = with(density) {
                                textMeasurer.measure(
                                    text = text ?: "",
                                    maxLines = 1,
                                    style = textStyle,
                                    constraints = Constraints(maxWidth = textWith)
                                ).size.height.toDp()
                            }
                            val height = with(density) {
                                textMeasurer.measure(
                                    text = text ?: "",
                                    maxLines = maxLines,
                                    style = textStyle,
                                    constraints = Constraints(maxWidth = textWith)
                                ).size.height.toDp()
                            }
                            textHeight = if (maxHeight > 0) {
                                if (height < maxHeight.dp) {
                                    maxHeight.dp
                                } else {
                                    height
                                }
                            } else {
                                if (height < oneLine * maxLines && keepHeight) {
                                    oneLine * maxLines
                                } else {
                                    height
                                }
                            }
                        }
                ) {
                    label?.let {
                        item {
                            Text(
                                text = "$it:",
                                color = styleLabel.color,
                                fontSize = styleLabel.fontSize,
                                fontStyle = styleLabel.fontStyle,
                                fontFamily = styleLabel.fontFamily,
                                textAlign = styleLabel.textAlign,
                                maxLines = 1,
                                minLines = 1
                            )
                        }
                    }
                    item {
                        Text(
                            modifier = modifier,
                            text = text ?: "",
                            color = textStyle.color,
                            fontSize = textStyle.fontSize,
                            fontStyle = textStyle.fontStyle,
                            fontFamily = textStyle.fontFamily,
                            textAlign = textStyle.textAlign,
                        )
                    }
                }
            } else {
                Row {
                    label?.let {
                        Text(
                            modifier = Modifier.padding(end = 8.dp),
                            text = "$it:",
                            color = styleLabel.color,
                            fontSize = styleLabel.fontSize,
                            fontStyle = styleLabel.fontStyle,
                            fontFamily = styleLabel.fontFamily,
                            textAlign = styleLabel.textAlign,
                            maxLines = 1,
                            minLines = 1
                        )
                    }
                    Text(
                        text = text ?: "",
                        color = textStyle.color,
                        fontSize = textStyle.fontSize,
                        fontStyle = textStyle.fontStyle,
                        fontFamily = textStyle.fontFamily,
                        textAlign = textStyle.textAlign,
                        maxLines = maxLines,
                    )
                }
            }
    }
}

