/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/19/24, 11:45 PM
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
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.CigarSearchFields
import com.akellolcc.cigars.databases.extensions.CigarSortingFields
import com.akellolcc.cigars.mvvm.SearchCigarScreenViewModel
import com.akellolcc.cigars.screens.navigation.NavRoute
import com.akellolcc.cigars.theme.Images
import com.akellolcc.cigars.theme.Localize
import com.akellolcc.cigars.theme.MaterialColors
import com.akellolcc.cigars.theme.TextStyles
import com.akellolcc.cigars.theme.loadIcon
import com.akellolcc.cigars.theme.materialColor
import kotlin.jvm.Transient

class SearchCigarScreen(route: NavRoute) :
    BaseTabListScreen<SearchCigarScreenViewModel.Actions, Cigar>(route) {

    @Composable
    override fun RightActionMenu(onDismiss: () -> Unit) {
        CigarSortingFields.enumValues().map {
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
    private fun SearchField(parameter: CigarSearchFields, last: Boolean = false) {
        var expanded by remember { mutableStateOf(false) }
        var value by remember { mutableStateOf("") }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextStyled(
                modifier = Modifier.weight(1f),
                label = CigarSearchFields.localized(parameter),
                text = value,
                enabled = !viewModel.loading,
                editable = true,
                style = TextStyles.Headline,
                maxLines = 1,
                onValueChange = {
                    viewModel.setFieldValue(parameter, it)
                    value = it
                },
                onKeyboardAction = {
                    viewModel.loadMore()
                },
                imeAction = ImeAction.Search

            )
            IconButton(
                modifier = Modifier.padding(start = 8.dp).wrapContentSize(),
                onClick = { expanded = true }
            ) {
                val icon =
                    if (!last) Images.icon_menu_minus else if (CigarSearchFields.entries.size == viewModel.searchParams.size) Images.icon_menu_minus else Images.icon_menu_plus
                loadIcon(icon, Size(24.0F, 24.0F))
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
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
    }

    @Composable
    override fun ContentHeader(modifier: Modifier) {
        Column {
            viewModel.searchParams.mapIndexed { index, it ->
                SearchField(it, index == viewModel.searchParams.lastIndex)
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

    @Transient
    override var viewModel = SearchCigarScreenViewModel()

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
