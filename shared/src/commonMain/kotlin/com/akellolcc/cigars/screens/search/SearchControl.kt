/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/4/24, 11:35 AM
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.mvvm.CigarsSearchControlViewModel
import com.akellolcc.cigars.mvvm.createViewModel
import com.akellolcc.cigars.screens.components.LinkButton
import com.akellolcc.cigars.screens.components.TextStyled
import com.akellolcc.cigars.screens.search.data.FilterCollection
import com.akellolcc.cigars.theme.Localize
import com.akellolcc.cigars.theme.TextStyles
import com.akellolcc.cigars.theme.loadIcon

data class SearchComponent(
    val modifier: Modifier = Modifier,
    val loading: Boolean,
    val fields: FilterCollection,
    val onAction: (SearchParameterAction) -> Unit
) {

    val viewModel = createViewModel(CigarsSearchControlViewModel::class, fields)

    @Composable
    fun Content() {
        val state by viewModel.state.collectAsState()

        LaunchedEffect(state) {
            state?.let {
                when (state) {
                    is CigarsSearchControlViewModel.Action.Completed -> {
                        if (viewModel.isAllValid) {
                            onAction(SearchParameterAction.Completed)
                        }
                    }

                    else -> {
                        Log.debug("SearchComponent state: $state")
                    }
                }
            }
        }

        Column() {
            viewModel.fields.controls.map {
                it.showLeading = fields.showLeading
                it.onAction = { action, parameter ->
                    viewModel.onFieldAction(action, parameter)
                }
                it.enabled = !loading
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
}
