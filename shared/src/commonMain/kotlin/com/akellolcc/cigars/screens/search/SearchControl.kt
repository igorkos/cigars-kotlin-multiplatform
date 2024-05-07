/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/7/24, 12:03 PM
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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.mvvm.base.createViewModel
import com.akellolcc.cigars.mvvm.search.CigarsSearchControlViewModel
import com.akellolcc.cigars.screens.components.LinkButton
import com.akellolcc.cigars.screens.components.TextStyled
import com.akellolcc.cigars.screens.search.data.FilterCollection
import com.akellolcc.cigars.theme.Localize
import com.akellolcc.cigars.theme.TextStyles
import com.akellolcc.cigars.theme.loadIcon

private data class SearchComponentImplement(
    val modifier: Modifier = Modifier,
    val fields: FilterCollection,
    val onAction: (SearchParameterAction) -> Unit
) {

    val viewModel = createViewModel(CigarsSearchControlViewModel::class, fields)

    @Composable
    fun Content() {

        LaunchedEffect(Unit) {
            viewModel.observeEvents {
                Log.debug("Control event: $it")
                when (it) {
                    is CigarsSearchControlViewModel.Action.Completed -> {
                        if (viewModel.isAllValid) {
                            onAction(SearchParameterAction.Completed)
                        }
                    }

                    else -> {}
                }
            }
        }

        Column() {
            viewModel.fields.controls.map {
                it.showLeading = fields.showLeading
                it.onAction = { action, parameter ->
                    viewModel.onFieldAction(action, parameter)
                }
                it.Content()
            }
            if (fields.availableFields.isNotEmpty()) {
                Column(
                    modifier = Modifier.wrapContentSize(),
                    horizontalAlignment = Alignment.End
                ) {
                    LinkButton(
                        title = Localize.button_title_add_search_field,
                        onClick = { viewModel.expanded = true }
                    )
                    DropdownMenu(expanded = viewModel.expanded,
                        onDismissRequest = { viewModel.expanded = false }) {
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
                                    viewModel.onFieldAction(SearchParameterAction.Add, it)
                                    viewModel.expanded = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchComponent(
    modifier: Modifier = Modifier,
    fields: FilterCollection,
    onAction: (SearchParameterAction) -> Unit
) {
    val field = remember { fields }
    val searchComponent = remember {
        mutableStateOf<SearchComponentImplement?>(null)
    }

    LaunchedEffect(fields, onAction, modifier) {
        searchComponent.value = SearchComponentImplement(modifier, field, onAction)
    }
    searchComponent.value?.Content()
}
