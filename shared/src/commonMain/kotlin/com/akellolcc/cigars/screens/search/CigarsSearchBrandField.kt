/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/5/24, 11:13 PM
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.mvvm.CigarsBrandsSearchViewModel
import com.akellolcc.cigars.mvvm.CigarsSearchFieldBaseViewModel
import com.akellolcc.cigars.mvvm.createViewModel
import com.akellolcc.cigars.screens.components.TextStyled
import com.akellolcc.cigars.screens.search.data.FilterParameter
import com.akellolcc.cigars.theme.Images
import com.akellolcc.cigars.theme.TextStyles
import com.akellolcc.cigars.theme.loadIcon

class CigarsSearchBrandField(
    parameter: FilterParameter<String>,
    showLeading: Boolean = false,
    enabled: Boolean = true,
    onAction: ((SearchParameterAction, FilterParameter<String>) -> Unit)? = null
) : SearchParameterField<String>(parameter, showLeading, enabled, onAction) {

    override fun handleAction(event: Any, navigator: Navigator?) {
        when (event) {
            is CigarsSearchFieldBaseViewModel.Action.Selected -> {
                onAction(SearchParameterAction.Completed)
            }
        }
    }

    private val viewModel = createViewModel(CigarsBrandsSearchViewModel::class, parameter)
    override fun validate(): Boolean {
        return viewModel.validate()
    }


    @Composable
    override fun Content() {
        Log.debug("Render Brand field: ${viewModel.value} ${viewModel.expanded} ${viewModel.brands.size}")

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (showLeading) {
                IconButton(
                    modifier = Modifier.wrapContentSize(),
                    onClick = { onAction(SearchParameterAction.Remove) }
                ) {
                    loadIcon(Images.icon_menu_delete, Size(12.0F, 12.0F))
                }
            }
            Column(
                modifier = Modifier.weight(1f),
            ) {
                TextStyled(
                    modifier = Modifier.fillMaxWidth().onFocusChanged {
                        viewModel.onFocusChange(it.isFocused)
                    },
                    label = parameter.label,
                    text = viewModel.value,
                    enabled = enabled,
                    isError = viewModel.isError,
                    editable = true,
                    style = if (viewModel.isError) TextStyles.ErrorTitle else TextStyles.Headline,
                    onValueChange = {
                        viewModel.value = it
                    },
                    imeAction = ImeAction.Search,
                    trailingIcon = if (viewModel.brands.isNotEmpty()) {
                        @Composable {
                            IconButton(
                                modifier = Modifier.wrapContentSize(),
                                onClick = {
                                    viewModel.onDropDown(viewModel.expanded)
                                }
                            ) {
                                loadIcon(Images.icon_drop_down, Size(12.0F, 12.0F))
                            }
                        }
                    } else null,
                    supportingText = if (viewModel.annotation != null) {
                        @Composable {
                            TextStyled(
                                modifier = Modifier.fillMaxWidth().padding(top = 4.dp, bottom = 4.dp),
                                text = viewModel.annotation,
                                style = if (viewModel.isError) TextStyles.Error else TextStyles.Description
                            )
                        }
                    } else null,
                    selection = viewModel.inputSelection
                )

                DropdownMenu(expanded = viewModel.expanded,
                    onDismissRequest = { viewModel.onDropDown(true) }) {
                    viewModel.brands.map {
                        Log.debug("Brand: $it")
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
                                viewModel.onBrandSelected(it)
                            }
                        )
                    }
                }
            }
        }
    }
}