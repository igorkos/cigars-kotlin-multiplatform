/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/9/24, 10:56 PM
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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.semantics.CollectionInfo
import androidx.compose.ui.semantics.collectionInfo
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.selectableGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.akellolcc.cigars.theme.Images
import com.akellolcc.cigars.theme.Localize
import com.akellolcc.cigars.theme.MaterialColors
import com.akellolcc.cigars.theme.TextStyles
import com.akellolcc.cigars.theme.loadIcon
import com.akellolcc.cigars.theme.materialColor
import com.akellolcc.cigars.utils.ui.pxToDp
import dev.icerock.moko.resources.ColorResource
import dev.icerock.moko.resources.ImageResource

data class ValuePickerItem(
    val value: Any?,
    val label: String,
    val icon: ImageResource?
)

@Composable
fun ValuePicker(
    modifier: Modifier = Modifier,
    label: String?,
    value: ValuePickerItem?,
    items: List<ValuePickerItem>?,
    onClick: ((ValuePickerItem) -> Unit)? = null,
    backgroundColor: ColorResource? = null
) {
    var expanded by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf(value) }
    var with by remember { mutableStateOf(0) }

    LaunchedEffect(items) {
        items?.let {
            if (it.size == 1 && it[0].value != null && selected?.value != it[0].value) {
                selected = it[0]
                onClick?.invoke(it[0])
            }
        }
    }

    Column(
        modifier = modifier.fillMaxWidth().height(60.dp).background(
            materialColor(backgroundColor ?: MaterialColors.color_surfaceVariant),
            TextFieldDefaults.shape
        ).onSizeChanged {
            with = it.width
        }.semantics(mergeDescendants = true) {
            isTraversalGroup = true
            selectableGroup()
            contentDescription = label ?: ""
            stateDescription = "${selected?.label}:${expanded}"
            collectionInfo = CollectionInfo(items?.size ?: 0, 0)
        },
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 4.dp, start = 16.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(0.8f)
            ) {
                CompositionLocalProvider(
                    LocalContentColor provides materialColor(MaterialColors.color_onSurfaceVariant)
                ) {
                    TextStyled(
                        label,
                        label ?: "",
                        TextStyles.Description,
                        labelStyle = TextStyles.None
                    )
                }
                TextStyled(
                    selected?.label,
                    selected?.label ?: "",
                    TextStyles.Subhead,
                    labelStyle = TextStyles.None,
                    maxLines = 1,
                    minLines = 1
                )
            }
            Column(
                modifier = Modifier.weight(0.2f)
            ) {
                IconButton(
                    modifier = Modifier.fillMaxSize().semantics {
                        contentDescription = Localize.value_picker_drop_down_action
                    },
                    onClick = {
                        expanded = !expanded
                    }
                ) {
                    loadIcon(
                        Images.icon_drop_down,
                        Size(12.0F, 12.0F),
                        tint = materialColor(
                            if ((items?.size ?: 0) > 1) MaterialColors.color_onSurfaceVariant else MaterialColors.color_transparent
                        )
                    )
                }
            }
        }

        DropdownMenu(
            expanded = expanded,
            modifier = Modifier.width(with.pxToDp()).requiredSizeIn(maxHeight = 200.dp)
                .background(materialColor(MaterialColors.color_surfaceVariant)).semantics {
                    contentDescription = Localize.value_picker_drop_down_menu
                },
            onDismissRequest = { expanded = false }
        ) {
            items?.forEach {
                DropdownMenuItem(
                    modifier = Modifier.width(with.pxToDp()),
                    leadingIcon = { if (it.icon != null) loadIcon(it.icon) },
                    text = {
                        TextStyled(
                            it.label,
                            it.label,
                            TextStyles.Subhead,
                            labelStyle = TextStyles.None,
                            maxLines = 1,
                            minLines = 1
                        )
                    },
                    onClick = {
                        expanded = false
                        selected = it
                        onClick?.invoke(it)
                    }
                )
            }
        }
    }
}

