/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/22/24, 9:38 PM
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization.Companion.Sentences
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.theme.Images
import com.akellolcc.cigars.theme.TextStyles
import com.akellolcc.cigars.theme.loadIcon
import com.akellolcc.cigars.theme.textStyle


@Composable
fun TextStyled(
    text: String?,
    style: TextStyles = TextStyles.Headline,
    label: String? = null,
    labelSuffix: String = ":",
    labelStyle: TextStyles = TextStyles.Subhead,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    editable: Boolean = false,
    modifier: Modifier = Modifier,
    maxHeight: Int = 0,
    keepHeight: Boolean = true,
    onValueChange: ((String) -> Unit)? = null,
    onKeyboardAction: ((ImeAction) -> Unit)? = null,
    inputMode: KeyboardType = KeyboardType.Text,
    center: Boolean = false,
    vertical: Boolean = false,
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Default,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val textStyle =
        textStyle(style).copy(textAlign = if (center) TextAlign.Center else TextAlign.Start)
    val styleLabel = textStyle(labelStyle)
    //   val textMeasurer = rememberTextMeasurer()
    //   var textWith by remember { mutableStateOf(0) }
    //   var textHeight by remember { mutableStateOf(maxHeight.dp) }

    if (editable) {
        TextField(
            modifier = modifier,
            value = text ?: "",
            trailingIcon = {
                if (inputMode == KeyboardType.Text) {
                    IconButton(
                        modifier = Modifier.wrapContentSize(),
                        onClick = { onValueChange?.invoke("") }
                    ) {
                        loadIcon(Images.icon_menu_delete, Size(8.0F, 8.0F))
                    }
                }
            },
            label = {
                TextStyled(
                    text = label ?: "",
                    style = TextStyles.Description,
                    maxLines = 1,
                    minLines = 1
                )
            },
            onValueChange = {
                onValueChange?.invoke(it)
            },
            keyboardActions = KeyboardActions(
                onDone = {
                    Log.debug("onDone")
                    keyboardController?.hide()
                    onKeyboardAction?.invoke(ImeAction.Done)
                },
                onGo = {
                    Log.debug("onGo")
                    keyboardController?.hide()
                    onKeyboardAction?.invoke(ImeAction.Go)
                },
                onNext = {
                    Log.debug("onNext")
                    keyboardController?.hide()
                    onKeyboardAction?.invoke(ImeAction.Next)
                },
                onPrevious = {
                    Log.debug("onPrevious")
                    keyboardController?.hide()
                    onKeyboardAction?.invoke(ImeAction.Previous)
                },
                onSearch = {
                    Log.debug("onSearch")
                    keyboardController?.hide()
                    onKeyboardAction?.invoke(ImeAction.Search)
                },
                onSend = {
                    Log.debug("onSend")
                    keyboardController?.hide()
                    onKeyboardAction?.invoke(ImeAction.Send)
                }),
            minLines = minLines,
            maxLines = maxLines,
            singleLine = maxLines == 1,
            textStyle = textStyle,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = inputMode,
                autoCorrect = true,
                capitalization = Sentences,
                imeAction = imeAction
            ),
            enabled = enabled
        )
    } else {
        @Composable
        fun context() {
            label?.let {
                Text(
                    modifier = Modifier.padding(end = if (vertical) 0.dp else 8.dp),
                    text = "$it$labelSuffix",
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
                modifier = modifier,
                text = text ?: "",
                color = textStyle.color,
                fontSize = textStyle.fontSize,
                fontStyle = textStyle.fontStyle,
                fontFamily = textStyle.fontFamily,
                textAlign = textStyle.textAlign,
                maxLines = maxLines,
            )
        }
        if (vertical) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                context()
            }
        } else {
            Row {
                context()
            }
        }
    }
}

/*
val density = LocalDensity.current
        if (maxLines != Int.MAX_VALUE) {
            LazyColumn(
                modifier = modifier
                    .height(textHeight)
                    //.background(materialColor(MaterialColors.color_error))
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
                            text = "$it$labelSuffix",
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

 */