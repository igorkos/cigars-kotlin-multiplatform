/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/10/24, 12:08 AM
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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.akellolcc.cigars.screens.components.transformations.InputMode
import com.akellolcc.cigars.theme.MaterialColors
import com.akellolcc.cigars.theme.TextStyles
import com.akellolcc.cigars.theme.materialColor

@Composable
fun ValueCard(
    label: String?,
    value: Any?,
    editable: Boolean = false,
    vertical: Boolean = true,
    items: List<ValuePickerItem>? = null,
    inputMode: InputMode = InputMode.Text,
    modifier: Modifier = Modifier,
    onValueChange: ((String) -> Unit)? = null,
    onClick: ((ValuePickerItem?) -> Unit)? = null
) {

    if (!editable && value == null) return

    if (editable) {
        when (value) {
            is String -> {
                TextStyled(
                    value,
                    label = label ?: "",
                    TextStyles.Subhead,
                    labelStyle = TextStyles.Subhead,
                    inputMode = inputMode,
                    editable = editable,
                    modifier = modifier.fillMaxWidth(),
                    onValueChange = onValueChange
                )
            }

            is ValuePickerItem -> {
                ValuePicker(
                    modifier = modifier,
                    value = value,
                    label = label,
                    items = items,
                    onClick = onClick
                )
            }
        }
    } else {
        if (vertical) {
            OutlinedCard(
                modifier = modifier.semantics {
                    isTraversalGroup = true
                },
                colors = CardDefaults.cardColors(
                    materialColor(MaterialColors.color_primaryContainer),
                    materialColor(MaterialColors.color_onPrimaryContainer)
                ),
                shape = RoundedCornerShape(5.dp),
                border = BorderStroke(
                    if (onClick != null) 1.dp else 0.5.dp,
                    materialColor(MaterialColors.color_onPrimaryContainer)
                ),
                elevation = if (onClick != null) CardDefaults.outlinedCardElevation(8.dp) else CardDefaults.outlinedCardElevation()
            ) {
                Column(
                    modifier = Modifier.padding(8.dp).clickable(onClick = {
                        onClick?.invoke(null)
                    }).semantics(mergeDescendants = true) {
                        contentDescription = "$label $value"
                    },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (label != null) {
                        TextStyled(
                            label,
                            label,
                            TextStyles.Subhead,
                            labelStyle = TextStyles.None
                        )
                    }
                    TextStyled(
                        value as String,
                        label ?: "",
                        TextStyles.Subhead,
                        labelStyle = TextStyles.None
                    )
                }
            }
        } else {
            TextStyled(
                value as String,
                label ?: "",
                TextStyles.Subhead,
                labelStyle = TextStyles.Subhead,
                modifier = modifier,
            )
        }
    }
}
