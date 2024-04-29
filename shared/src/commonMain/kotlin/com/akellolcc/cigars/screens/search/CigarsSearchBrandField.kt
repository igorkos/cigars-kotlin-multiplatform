/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/28/24, 12:24 PM
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

package com.akellolcc.cigars.screens.search

import TextStyled
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.input.ImeAction
import com.akellolcc.cigars.databases.rapid.rest.RapidCigarBrand
import com.akellolcc.cigars.theme.Images
import com.akellolcc.cigars.theme.TextStyles
import com.akellolcc.cigars.theme.loadIcon
import kotlinx.coroutines.flow.Flow

class CigarsSearchBrandField(
    parameter: SearchParam<String>,
    showLeading: Boolean,
    onAction: ((SearchParameterAction, SearchParam<String>) -> Flow<Any?>)?
) : SearchParameterField<String>(parameter, showLeading, onAction) {
    @Composable
    override fun Render(enabled: Boolean) {
        var expanded by remember { mutableStateOf(false) }
        var value by remember { mutableStateOf("") }
        var brands by remember { mutableStateOf(listOf<RapidCigarBrand>()) }
        //var selectedBrand by remember { mutableStateOf<RapidCigarBrand?>(null) }

        LaunchedEffect(value) {
            parameter.value = value
            onAction?.invoke(SearchParameterAction.LoadData, parameter)?.collect {

            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (showLeading) {
                IconButton(
                    modifier = Modifier.wrapContentSize(),
                    onClick = { onAction?.invoke(SearchParameterAction.Remove, parameter) }
                ) {
                    loadIcon(Images.icon_menu_delete, Size(12.0F, 12.0F))
                }
            }
            Column(
                modifier = Modifier.weight(1f),
            ) {
                TextStyled(
                    modifier = Modifier.fillMaxWidth(),
                    label = parameter.label,
                    text = value,
                    enabled = enabled,
                    editable = true,
                    style = TextStyles.Headline,
                    maxLines = 1,
                    onValueChange = {
                        value = it
                    },
                    onKeyboardAction = {
                        parameter.value = value
                        onAction?.invoke(SearchParameterAction.Completed, parameter)
                    },
                    imeAction = ImeAction.Search
                )
                DropdownMenu(expanded = expanded,
                    onDismissRequest = { expanded = false }) {
                    brands.map {
                        DropdownMenuItem(
                            leadingIcon = {
                                loadIcon(Images.tab_icon_search, Size(24.0F, 24.0F))
                            },
                            text = {
                                TextStyled(
                                    it.name,
                                    TextStyles.Subhead
                                )
                            },
                            onClick = {
                                value = it.name ?: ""
                                parameter.value = value
                                onAction?.invoke(SearchParameterAction.Completed, parameter)
                            }
                        )
                    }
                }
            }
        }
    }
}