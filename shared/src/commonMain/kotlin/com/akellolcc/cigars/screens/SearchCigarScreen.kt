/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/10/24, 4:57 PM
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

package com.akellolcc.cigars.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.akellolcc.cigars.databases.models.Cigar
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.mvvm.MainScreenViewModel
import com.akellolcc.cigars.mvvm.base.createViewModel
import com.akellolcc.cigars.mvvm.search.CigarsSearchFieldBaseViewModel
import com.akellolcc.cigars.mvvm.search.SearchCigarScreenViewModel
import com.akellolcc.cigars.screens.base.BaseTabListScreen
import com.akellolcc.cigars.screens.components.ProgressIndicator
import com.akellolcc.cigars.screens.components.TextStyled
import com.akellolcc.cigars.screens.components.search.SearchComponent
import com.akellolcc.cigars.screens.components.search.data.FilterParameter
import com.akellolcc.cigars.screens.navigation.CigarSearchDetailsRoute
import com.akellolcc.cigars.screens.navigation.ITabItem
import com.akellolcc.cigars.screens.navigation.NavRoute
import com.akellolcc.cigars.theme.Images
import com.akellolcc.cigars.theme.Localize
import com.akellolcc.cigars.theme.MaterialColors
import com.akellolcc.cigars.theme.TextStyles
import com.akellolcc.cigars.theme.loadIcon
import com.akellolcc.cigars.theme.materialColor
import kotlin.jvm.Transient

class SearchScreen(
    override val route: NavRoute
) : ITabItem<SearchCigarScreenViewModel> {
    @kotlinx.serialization.Transient
    @Transient
    override lateinit var viewModel: SearchCigarScreenViewModel

    @Composable
    override fun Content() {
        viewModel = rememberScreenModel { createViewModel(SearchCigarScreenViewModel::class, route.data) }
        LocalNavigator.currentOrThrow.push(SearchCigarScreen(route, viewModel))
    }
}

class SearchCigarScreen(
    route: NavRoute,
    @kotlinx.serialization.Transient
    @Transient
    override var viewModel: SearchCigarScreenViewModel
) : BaseTabListScreen<Cigar, SearchCigarScreenViewModel>(route) {
    @Composable
    override fun RightActionMenu(onDismiss: () -> Unit) {
        viewModel.sortingFields?.selected?.map {
            val selected = viewModel.sorting == it
            DropdownMenuItem(
                leadingIcon = {
                    loadIcon(if (selected) Images.icon_menu_checkmark else Images.icon_menu_sort, Size(24.0F, 24.0F))
                },
                text = {
                    TextStyled(
                        it.label,
                        Localize.list_sorting_item,
                        TextStyles.Subhead,
                        labelStyle = TextStyles.None
                    )
                },
                onClick = {
                    @Suppress("UNCHECKED_CAST")
                    viewModel.sorting = it as FilterParameter<Boolean>
                    onDismiss()
                }
            )
        }
    }

    @Composable
    override fun ContentHeader(modifier: Modifier) {
        SearchComponent(
            modifier = modifier,
            fields = viewModel.searchingFields!!,
        ) { action ->
            Log.debug("Received action: $action")
            when (action) {
                is CigarsSearchFieldBaseViewModel.Action.ExecuteSearch -> {
                    viewModel.paging()
                }

                else -> {}
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
                ProgressIndicator()
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
                    text = entity.name,
                    Localize.cigar_details_name,
                    style = TextStyles.Headline,
                    labelStyle = TextStyles.None,
                    maxLines = 2,
                    minLines = 2
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

    override fun handleAction(event: Any, navigator: Navigator) {
        val mainModel = createViewModel(MainScreenViewModel::class)
        when (event) {
            is SearchCigarScreenViewModel.Actions.RouteToCigar -> {
                Log.debug("Selected cigar ${event.cigar.rowid}")
                mainModel.isTabsVisible = false
                navigator.push(CigarSearchDetailsScreen(CigarSearchDetailsRoute.apply {
                    data = event.cigar
                }))
            }

            else -> super.handleAction(event, navigator)
        }
    }
}
