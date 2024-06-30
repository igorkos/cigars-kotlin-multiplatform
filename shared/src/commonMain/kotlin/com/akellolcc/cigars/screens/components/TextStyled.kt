/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/24/24, 5:19 PM
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization.Companion.Sentences
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.screens.components.transformations.InputMode
import com.akellolcc.cigars.theme.Images
import com.akellolcc.cigars.theme.Localize
import com.akellolcc.cigars.theme.TextStyles
import com.akellolcc.cigars.theme.loadIcon
import com.akellolcc.cigars.theme.textStyle

internal data class TextStyledState(val editable: Boolean, val vertical: Boolean, val labelVisible: Boolean, val input: String = "") {
    override fun toString(): String {
        return "{\"editable\":$editable, \"vertical\":$vertical, \"labelVisible\":$labelVisible, \"input\":$input}"
    }
}


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
    inputMode: InputMode? = null,
    center: Boolean = false,
    vertical: Boolean = false,
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Default,
    isError: Boolean = false,
    prefix: String? = null,
    supportingText: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    onValueChange: ((String) -> Unit)? = null,
    onKeyboardAction: ((ImeAction) -> Unit)? = null
) {
    Column(modifier = modifier.semantics(mergeDescendants = true) {
        isTraversalGroup = true
        contentDescription = label
        stateDescription = TextStyledState(editable, vertical, labelStyle != TextStyles.None).toString()
    }.wrapContentSize()) {
        if (editable) {
            TextFieldStyled(
                text,
                style,
                label,
                labelStyle,
                prefix,
                inputMode ?: InputMode.Text,
                imeAction,
                center,
                enabled,
                maxLines,
                minLines,
                isError,
                trailingIcon,
                onValueChange,
                onKeyboardAction,
                supportingText
            )
        } else {
            TextWithLabel(text, label, style, labelStyle, vertical, modifier, labelSuffix, center, maxLines)
        }
    }
}

/**
 * Static text with label
 */
@Composable
internal fun TextWithLabel(
    text: String?,
    label: String,
    styleText: TextStyles,
    styleLabel: TextStyles,
    vertical: Boolean,
    modifier: Modifier = Modifier,
    labelSuffix: String,
    center: Boolean,
    maxLines: Int
) {
    val textStyle = textStyle(styleText).copy(textAlign = if (center) TextAlign.Center else TextAlign.Justify)
    val labelStyle = textStyle(styleLabel)

    @Composable
    fun context() {
        if (styleLabel != TextStyles.None) {
            Text(
                modifier = Modifier.padding(end = if (vertical) 0.dp else 8.dp),
                text = "$label$labelSuffix",
                color = labelStyle.color,
                fontSize = labelStyle.fontSize,
                fontStyle = labelStyle.fontStyle,
                fontFamily = labelStyle.fontFamily,
                textAlign = labelStyle.textAlign,
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
            softWrap = maxLines > 1,
            maxLines = maxLines,
        )
    }
    if (vertical) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            context()
        }
    } else {
        Row(modifier = modifier) {
            context()
        }
    }
}

/**
 * Editable text field
 */

/**
 * Keyboard actions
 */
@Composable
internal fun keyboardActions(onKeyboardAction: ((ImeAction) -> Unit)?): KeyboardActions {
    val keyboardController = LocalSoftwareKeyboardController.current
    var hide by remember { mutableStateOf(false) }

    SideEffect {
        if (hide) {
            keyboardController?.hide()
            hide = false
        }
    }

    return KeyboardActions(
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
        })
}

/**
 * Trailing icon
 */
@Composable
internal fun TrailingIcon(
    inputMode: InputMode, onValueChange: ((String) -> Unit)?,
    onKeyboardAction: ((ImeAction) -> Unit)?
) {
    if (inputMode == InputMode.Text) {
        IconButton(
            modifier = Modifier.wrapContentSize().semantics(mergeDescendants = true) {
                contentDescription = Localize.styled_text_field_action
            },
            onClick = {
                onValueChange?.invoke("")
                onKeyboardAction?.invoke(ImeAction.Default)
            }
        ) {
            loadIcon(Images.icon_menu_delete, Size(8.0F, 8.0F))
        }
    }
}

/**
 * Label
 */
@Composable
internal fun TextFieldLabel(label: String, labelStyle: TextStyles) {
    val styleLabel = textStyle(labelStyle)
    if (labelStyle != TextStyles.None) {
        Text(
            modifier = Modifier.semantics(mergeDescendants = true) {
                contentDescription = Localize.styled_text_field_label
            },
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
}

/**
 * Prefix
 */
@Composable
internal fun TextFieldPrefix(prefix: String?, style: TextStyle) {
    if (!prefix.isNullOrEmpty()) {
        Text(
            modifier = Modifier.wrapContentSize(),
            text = prefix,
            color = style.color,
            fontSize = style.fontSize,
            fontStyle = style.fontStyle,
            fontFamily = style.fontFamily,
            textAlign = style.textAlign,
            maxLines = 1,
        )
    }
}

@Composable
internal fun TextFieldStyled(
    text: String?,
    style: TextStyles,
    label: String,
    labelStyle: TextStyles,
    prefix: String? = null,
    inputMode: InputMode,
    imeAction: ImeAction,
    center: Boolean,
    enabled: Boolean = true,
    maxLines: Int,
    minLines: Int,
    isError: Boolean,
    trailingIcon: @Composable (() -> Unit)? = null,
    onValueChange: ((String) -> Unit)? = null,
    onKeyboardAction: ((ImeAction) -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
) {
    val textStyle = textStyle(style).copy(textAlign = if (center) TextAlign.Center else TextAlign.Justify)

    var textFieldState by remember {
        mutableStateOf(
            TextFieldValue(
                text = text?.let {
                    inputMode.fromTransformed(it)
                } ?: "",
                selection = when {
                    text.isNullOrEmpty() -> TextRange.Zero
                    //selection != null -> selection
                    else -> TextRange(text.length, text.length)
                }
            )
        )
    }

    LaunchedEffect(text) {
        if (!inputMode.compare(text, textFieldState.text)) {
            val fieldText = inputMode.fromTransformed(text)
            textFieldState = textFieldState.copy(text = fieldText)
        }
    }

    TextField(
        value = textFieldState,
        onValueChange = {
            if (inputMode.validate(it.text)) {
                val input = inputMode.toTransformed(it.text, "TextStyled")
                textFieldState = it.copy(text = inputMode.fromTransformed(input))
                onValueChange?.invoke(input)
            }
        },
        modifier = Modifier.fillMaxWidth().semantics(mergeDescendants = true) {
            contentDescription = Localize.styled_text_field_edit
            stateDescription = textFieldState.text
        },
        enabled = enabled,
        textStyle = textStyle,
        label = { TextFieldLabel(label, labelStyle) },
        trailingIcon = { trailingIcon ?: TrailingIcon(inputMode, onValueChange, onKeyboardAction) },
        prefix = { TextFieldPrefix(prefix, textStyle) },
        supportingText = supportingText,
        isError = isError,
        visualTransformation = inputMode.visualTransformation,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = inputMode.toKeyboardType(),
            autoCorrect = true,
            capitalization = Sentences,
            imeAction = imeAction
        ),
        keyboardActions = keyboardActions(onKeyboardAction),
        singleLine = maxLines == 1,
        minLines = minLines,
        maxLines = maxLines,
    )
}