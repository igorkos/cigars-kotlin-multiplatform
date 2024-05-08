/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/8/24, 11:16 AM
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

package com.akellolcc.cigars.screens.components.search

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
import com.akellolcc.cigars.mvvm.search.CigarsSearchFieldBaseViewModel
import com.akellolcc.cigars.screens.components.LinkButton
import com.akellolcc.cigars.screens.components.TextStyled
import com.akellolcc.cigars.screens.components.search.data.FilterCollection
import com.akellolcc.cigars.theme.Localize
import com.akellolcc.cigars.theme.TextStyles
import com.akellolcc.cigars.theme.loadIcon

private data class SearchComponentImplement(
    val modifier: Modifier = Modifier,
    val fields: FilterCollection,
    val onAction: (CigarsSearchFieldBaseViewModel.Action) -> Unit
) {

    val viewModel = createViewModel(CigarsSearchControlViewModel::class, fields)

    @Composable
    fun Content() {

        LaunchedEffect(Unit) {
            viewModel.observeEvents {
                Log.debug("Search control get event: $it")
                when (it) {
                    is CigarsSearchFieldBaseViewModel.Action.ExecuteSearch -> {
                        if (viewModel.isAllValid) {
                            onAction(it)
                        }
                    }

                    else -> {}
                }
            }
        }

        Column() {
            viewModel.fields.controls.map {
                it.showLeading = fields.showLeading
                it.onAction = { action ->
                    viewModel.onFieldAction(action)
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
                                    viewModel.onFieldAction(CigarsSearchFieldBaseViewModel.Action.AddField(it))
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
    onAction: (CigarsSearchFieldBaseViewModel.Action) -> Unit
) {
    val field = remember { fields }
    val searchComponent = remember {
        mutableStateOf<SearchComponentImplement?>(null)
    }

    LaunchedEffect(Unit) {
        searchComponent.value = SearchComponentImplement(modifier, field, onAction)
    }
    searchComponent.value?.Content()
}
