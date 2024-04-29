/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/28/24, 10:07 PM
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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import com.akellolcc.cigars.screens.components.LinkButton
import com.akellolcc.cigars.theme.Localize
import com.akellolcc.cigars.theme.TextStyles
import com.akellolcc.cigars.theme.loadIcon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch

@Composable
fun <T : Comparable<T>> SearchComponent(
    modifier: Modifier = Modifier,
    loading: Boolean,
    fields: SearchParameters<T, SearchParameterField<T>>,
    onAction: (SearchParameterAction, SearchParam<T>?) -> Flow<Any?>,
) {
    var expanded by remember { mutableStateOf(false) }
    var compose by remember { mutableStateOf(false) }

    LaunchedEffect(compose) {}

    Column(modifier = modifier) {
        fields.map { field ->
            field.showLeading = fields.showLeading
            field.onAction = { action, value ->
                flow {
                    when (action) {
                        SearchParameterAction.Remove -> {
                            fields.remove(value)
                            compose = !compose
                        }

                        else -> {}
                    }
                    onAction(action, value).collect {
                        emit(it)
                    }
                }
            }
            field.Render(!loading)
        }
        if (fields.availableFields.isNotEmpty()) {
            Column(
                modifier = modifier.wrapContentSize().align(Alignment.End),
                horizontalAlignment = Alignment.End
            ) {
                LinkButton(
                    title = Localize.button_title_add_search_field,
                    onClick = { expanded = true }
                )
                DropdownMenu(expanded = expanded,
                    onDismissRequest = { expanded = false }) {
                    fields.availableFields.map {
                        DropdownMenuItem(
                            leadingIcon = {
                                loadIcon(it.icon, Size(24.0F, 24.0F))
                            },
                            text = {
                                TextStyled(
                                    it.label,
                                    TextStyles.Subhead
                                )
                            },
                            onClick = {
                                fields.add(it)
                                CoroutineScope(Dispatchers.Main).launch {
                                    onAction(SearchParameterAction.Add, it).single()
                                }
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
        if (loading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.width(32.dp),
                )
            }
        }
    }
}
