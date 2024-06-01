/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/1/24, 3:57 PM
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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import app.cash.paging.LoadStateNotLoading
import app.cash.paging.compose.collectAsLazyPagingItems
import cafe.adriel.voyager.navigator.Navigator
import com.akellolcc.cigars.databases.models.CigarSortingFields
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.mvvm.base.createViewModel
import com.akellolcc.cigars.mvvm.search.CigarsBrandsSearchViewModel
import com.akellolcc.cigars.mvvm.search.CigarsSearchFieldBaseViewModel
import com.akellolcc.cigars.screens.components.TextStyled
import com.akellolcc.cigars.screens.components.search.data.FilterParameter
import com.akellolcc.cigars.theme.Images
import com.akellolcc.cigars.theme.MaterialColors
import com.akellolcc.cigars.theme.TextStyles
import com.akellolcc.cigars.theme.loadIcon
import com.akellolcc.cigars.theme.materialColor
import com.akellolcc.cigars.utils.ui.pxToDp

class CigarsSearchBrandField(
    parameter: FilterParameter<String>,
    showLeading: Boolean = false,
    enabled: Boolean = true,
    onAction: ((CigarsSearchFieldBaseViewModel.Action) -> Unit)? = null
) : SearchParameterField<String>(parameter, showLeading, enabled, onAction) {

    override fun handleAction(event: Any, navigator: Navigator?) {
        Log.debug("Handle action: $event")
        when (event) {
            is CigarsSearchFieldBaseViewModel.Action.Selected -> {
                onAction(CigarsSearchFieldBaseViewModel.Action.FieldSearch(parameter))
            }
        }
    }

    private val viewModel = createViewModel(CigarsBrandsSearchViewModel::class, parameter)
    override fun validate(): Boolean {
        return viewModel.validate()
    }

    @Composable
    override fun Content() {
        var with by remember { mutableStateOf(0) }
        var menuHeight by remember { mutableStateOf(0) }
        val listState = rememberLazyListState()
        val pagingItems = viewModel.brands?.collectAsLazyPagingItems()

        LaunchedEffect(Unit) {
            viewModel.observeEvents("CigarsBrandsSearchViewModel") {
                handleAction(it, null)
            }
        }

        LaunchedEffect(pagingItems?.itemCount) {
            if (viewModel.hasBrands(pagingItems)) {
                menuHeight = minOf(pagingItems!!.itemCount * 40, 200)
                if (pagingItems.loadState.refresh is LoadStateNotLoading) {
                    viewModel.setState(CigarsSearchFieldBaseViewModel.Action.Loaded())
                }
                if (pagingItems.itemCount == 0) {
                    viewModel.setState(CigarsSearchFieldBaseViewModel.Action.Error())
                }
            } else {
                viewModel.setState(CigarsSearchFieldBaseViewModel.Action.Input())
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (showLeading) {
                IconButton(
                    modifier = Modifier.wrapContentSize().testTag(testTag(CigarSortingFields.Brand, "leading")),
                    onClick = { onAction(CigarsSearchFieldBaseViewModel.Action.RemoveField(parameter)) }
                ) {
                    loadIcon(Images.icon_menu_delete, Size(12.0F, 12.0F))
                }
            }
            Column(
                modifier = Modifier.weight(1f).onSizeChanged {
                    with = it.width
                },
            ) {
                TextStyled(
                    modifier = Modifier.fillMaxWidth().onFocusChanged {
                        viewModel.onFocusChange(it.isFocused)
                    }.testTag(testTag(CigarSortingFields.Brand, "input")),
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
                    trailingIcon = if (viewModel.hasBrands(pagingItems)) {
                        @Composable {
                            IconButton(
                                modifier = Modifier.wrapContentSize().testTag(testTag(CigarSortingFields.Brand, "drop_down")),
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


                DropdownMenu(expanded = viewModel.expanded && viewModel.hasBrands(pagingItems),
                    modifier = Modifier.width(with.pxToDp()).requiredSizeIn(maxHeight = 200.dp)
                        .background(materialColor(MaterialColors.color_surfaceVariant)),
                    onDismissRequest = { viewModel.onDropDown(true) }) {
                    LazyColumn(
                        state = listState,
                        verticalArrangement = Arrangement.Top,
                        modifier = Modifier.width(with.pxToDp()).height(menuHeight.dp).testTag(testTag(CigarSortingFields.Brand, "list"))
                    ) {
                        pagingItems?.let { items ->
                            items(count = items.itemCount) { index ->
                                val it = items[index]
                                DropdownMenuItem(
                                    leadingIcon = {
                                        loadIcon(Images.tab_icon_search, Size(24.0F, 24.0F))
                                    },
                                    text = {
                                        TextStyled(
                                            it?.name,
                                            TextStyles.Subhead,
                                            maxLines = 1
                                        )
                                    },
                                    onClick = {
                                        viewModel.onBrandSelected(it!!)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}