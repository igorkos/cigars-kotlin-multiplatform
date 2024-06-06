/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/6/24, 12:12 AM
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
 ******************************************************************************************************************************************/
package com.akellolcc.cigars.screens.components

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
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization.Companion.Sentences
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.screens.components.transformations.InputMode
import com.akellolcc.cigars.theme.Images
import com.akellolcc.cigars.theme.TextStyles
import com.akellolcc.cigars.theme.loadIcon
import com.akellolcc.cigars.theme.textStyle

@Composable
fun TextStyled(
    text: String?,
    label: String,
    style: TextStyles = TextStyles.Headline,
    labelSuffix: String = ":",
    labelStyle: TextStyles = TextStyles.Description,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    editable: Boolean = false,
    modifier: Modifier = Modifier,
    onValueChange: ((String) -> Unit)? = null,
    onKeyboardAction: ((ImeAction) -> Unit)? = null,
    inputMode: InputMode = InputMode.Text,
    center: Boolean = false,
    vertical: Boolean = false,
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Default,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null,
    selection: TextRange? = null,
    prefix: String? = null,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val textStyle = textStyle(style).copy(textAlign = if (center) TextAlign.Center else TextAlign.Justify)
    val styleLabel = textStyle(labelStyle)
    var hide by remember { mutableStateOf(false) }

    SideEffect {
        if (hide) {
            keyboardController?.hide()
            hide = false
        }
    }

    Column(modifier = Modifier.semantics(mergeDescendants = true) {
        isTraversalGroup = true
        contentDescription = label
    }) {
        if (editable) {
            var textFieldState by remember {
                mutableStateOf(
                    TextFieldValue(
                        text = text ?: "",
                        selection = when {
                            text.isNullOrEmpty() -> TextRange.Zero
                            selection != null -> selection
                            else -> TextRange(text.length, text.length)
                        }
                    )
                )
            }

            textFieldState = if (selection != null) {
                textFieldState.copy(text = inputMode.fromTransformed(text), selection = selection)
            } else {
                textFieldState.copy(text = inputMode.fromTransformed(text))
            }


            TextField(
                modifier = modifier,
                value = textFieldState,
                trailingIcon = trailingIcon ?: if (inputMode == InputMode.Text) {
                    {
                        IconButton(
                            modifier = Modifier.wrapContentSize(),
                            onClick = {
                                onValueChange?.invoke("")
                                onKeyboardAction?.invoke(ImeAction.Default)
                            }
                        ) {
                            loadIcon(Images.icon_menu_delete, Size(8.0F, 8.0F))
                        }
                    }
                } else null,
                label = if (labelStyle != TextStyles.None) {
                    {
                        Text(
                            text = label,
                            color = styleLabel.color,
                            fontSize = styleLabel.fontSize,
                            fontStyle = styleLabel.fontStyle,
                            fontFamily = styleLabel.fontFamily,
                            textAlign = styleLabel.textAlign,
                            maxLines = 1,
                            minLines = 1
                        )
                    }
                } else null,
                onValueChange = {
                    if (inputMode.validate(it.text)) {
                        textFieldState = it
                        onValueChange?.invoke(inputMode.toTransformed(it.text))
                    }
                },
                keyboardActions = KeyboardActions(
                    onDone = {
                        Log.debug("onDone")
                        hide = true
                        onKeyboardAction?.invoke(ImeAction.Done)
                    },
                    onGo = {
                        Log.debug("onGo")
                        hide = true
                        onKeyboardAction?.invoke(ImeAction.Go)
                    },
                    onNext = {
                        Log.debug("onNext")
                        hide = true
                        onKeyboardAction?.invoke(ImeAction.Next)
                    },
                    onPrevious = {
                        Log.debug("onPrevious")
                        hide = true
                        onKeyboardAction?.invoke(ImeAction.Previous)
                    },
                    onSearch = {
                        Log.debug("onSearch")
                        hide = true
                        onKeyboardAction?.invoke(ImeAction.Search)
                    },
                    onSend = {
                        Log.debug("onSend")
                        hide = true
                        onKeyboardAction?.invoke(ImeAction.Send)
                    }),
                minLines = minLines,
                maxLines = maxLines,
                singleLine = maxLines == 1,
                textStyle = textStyle,
                isError = isError,
                prefix = if (prefix.isNullOrEmpty()) null else {
                    @Composable {
                        Text(
                            modifier = Modifier.wrapContentSize(),
                            text = prefix,
                            color = textStyle.color,
                            fontSize = textStyle.fontSize,
                            fontStyle = textStyle.fontStyle,
                            fontFamily = textStyle.fontFamily,
                            textAlign = textStyle.textAlign,
                            maxLines = 1,
                        )
                    }
                },
                supportingText = supportingText,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = inputMode.toKeyboardType(),
                    autoCorrect = true,
                    capitalization = Sentences,
                    imeAction = imeAction
                ),
                enabled = enabled,
                visualTransformation = inputMode.visualTransformation()
            )
        } else {
            @Composable
            fun context() {
                if (labelStyle != TextStyles.None) {
                    Text(
                        modifier = Modifier.padding(end = if (vertical) 0.dp else 8.dp),
                        text = "$label$labelSuffix",
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
                    softWrap = maxLines > 1,
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
}
