/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/21/24, 12:52 PM
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
 */

package com.akellolcc.cigars.screens

import TextStyled
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.CigarSearchFields
import com.akellolcc.cigars.mvvm.SearchCigarScreenViewModel
import com.akellolcc.cigars.screens.components.LinkButton
import com.akellolcc.cigars.screens.navigation.NavRoute
import com.akellolcc.cigars.screens.search.SearchParameterAction
import com.akellolcc.cigars.theme.Images
import com.akellolcc.cigars.theme.Localize
import com.akellolcc.cigars.theme.MaterialColors
import com.akellolcc.cigars.theme.TextStyles
import com.akellolcc.cigars.theme.loadIcon
import com.akellolcc.cigars.theme.materialColor
import kotlin.jvm.Transient

class SearchCigarScreen(route: NavRoute) :
    BaseTabListScreen<SearchCigarScreenViewModel.Actions, Cigar>(route) {
    @Transient
    override var viewModel = SearchCigarScreenViewModel()

    @Composable
    override fun RightActionMenu(onDismiss: () -> Unit) {
        CigarSearchFields.enumValues().map {
            DropdownMenuItem(
                leadingIcon = {
                    loadIcon(Images.icon_menu_sort, Size(24.0F, 24.0F))
                },
                text = {
                    TextStyled(
                        it.second,
                        TextStyles.Subhead
                    )
                },
                onClick = {
                    viewModel.sorting(it.first.value)
                    onDismiss()
                }
            )
        }
    }

    @Composable
    override fun ContentHeader(modifier: Modifier) {
        var expanded by remember { mutableStateOf(false) }
        Column(modifier = modifier) {
            viewModel.searchParams.map {
                it.showLeading = viewModel.searchParams.size > 1
                it.onAction = { action, data ->
                    when (action) {
                        SearchParameterAction.Remove -> viewModel.removeSearchParameter(it)
                        SearchParameterAction.Completed -> {
                            viewModel.setFieldValue(it.type, data)
                            viewModel.loadMore()
                        }

                        else -> {}
                    }
                }
                it.Render(!viewModel.loading)
            }
            if (viewModel.hasMoreSearchParameters) {
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
                        viewModel.sortingMenu().map {
                            DropdownMenuItem(
                                leadingIcon = {
                                    loadIcon(Images.tab_icon_search, Size(24.0F, 24.0F))
                                },
                                text = {
                                    TextStyled(
                                        it.second,
                                        TextStyles.Subhead
                                    )
                                },
                                onClick = {
                                    viewModel.addSearchParameter(it.first)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
            if (viewModel.loading && viewModel.entities.isEmpty()) {
                ListFooter(Modifier.fillMaxWidth())
            }
        }
    }

    @Composable
    override fun ListFooter(modifier: Modifier) {
        if (viewModel.loading) {
            Box(
                modifier = modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.width(32.dp),
                )
            }
        }
    }


    @Composable
    override fun EntityListRow(entity: Cigar, modifier: Modifier) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = materialColor(MaterialColors.color_primaryContainer)
            ),
            modifier = modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, start = 12.dp, end = 12.dp, bottom = 0.dp)

            ) {
                TextStyled(
                    maxLines = 2,
                    minLines = 2,
                    text = entity.name,
                    style = TextStyles.Headline,
                    keepHeight = true
                )
            }
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                TextStyled(
                    label = Localize.cigar_details_length,
                    labelSuffix = "",
                    text = entity.length,
                    style = TextStyles.Subhead,
                )
                TextStyled(
                    label = Localize.cigar_details_gauge,
                    labelSuffix = "",
                    text = "${entity.gauge}",
                    style = TextStyles.Subhead,
                )
            }
        }
    }

    override fun handleAction(event: SearchCigarScreenViewModel.Actions, navigator: Navigator?) {
        TODO("Not yet implemented")
    }
}
