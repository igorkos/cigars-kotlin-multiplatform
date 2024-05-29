/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/28/24, 1:42 PM
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

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.testTag
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.navigator.Navigator
import com.akellolcc.cigars.databases.models.Cigar
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.mvvm.MainScreenViewModel
import com.akellolcc.cigars.mvvm.base.createViewModel
import com.akellolcc.cigars.mvvm.cigars.CigarsScreenViewModel
import com.akellolcc.cigars.mvvm.search.CigarsSearchFieldBaseViewModel
import com.akellolcc.cigars.screens.base.BaseTabListScreen
import com.akellolcc.cigars.screens.components.CigarListRow
import com.akellolcc.cigars.screens.components.TextStyled
import com.akellolcc.cigars.screens.components.search.SearchComponent
import com.akellolcc.cigars.screens.components.search.data.FilterParameter
import com.akellolcc.cigars.screens.navigation.CigarsDetailsRoute
import com.akellolcc.cigars.screens.navigation.ITabItem
import com.akellolcc.cigars.screens.navigation.NavRoute
import com.akellolcc.cigars.theme.Images
import com.akellolcc.cigars.theme.TextStyles
import com.akellolcc.cigars.theme.loadIcon
import kotlinx.serialization.Transient


class CigarsScreen(
    override val route: NavRoute
) : ITabItem<CigarsScreenViewModel> {
    @Transient
    @kotlin.jvm.Transient
    override lateinit var viewModel: CigarsScreenViewModel

    @Composable
    override fun Content() {
        viewModel = rememberScreenModel { createViewModel(CigarsScreenViewModel::class) }
        Navigator(CigarsListScreen<CigarsScreenViewModel>(route, viewModel))
    }
}

open class CigarsListScreen<V : ScreenModel>(
    route: NavRoute,
    @Transient
    @kotlin.jvm.Transient
    override var viewModel: CigarsScreenViewModel
) : BaseTabListScreen<CigarsScreenViewModel.CigarsAction, Cigar, CigarsScreenViewModel>(route) {
    @Suppress("UNCHECKED_CAST")
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
                        TextStyles.Subhead
                    )
                },
                onClick = {
                    viewModel.sorting = it as FilterParameter<Boolean>
                    onDismiss()
                }
            )
        }
    }

    override fun handleAction(event: CigarsScreenViewModel.CigarsAction, navigator: Navigator?) {
        val mainModel = createViewModel(MainScreenViewModel::class)
        when (event) {
            is CigarsScreenViewModel.CigarsAction.RouteToCigar -> {
                Log.debug("Selected cigar ${event.cigar.rowid}")
                navigator?.push(CigarDetailsScreen(CigarsDetailsRoute.apply {
                    data = event.cigar
                }))
                mainModel.isTabsVisible = false
            }

            is CigarsScreenViewModel.CigarsAction.ShowError -> TODO()
        }
    }

    @Composable
    override fun ContentHeader(modifier: Modifier) {
        if (viewModel.search) {
            SearchComponent(
                modifier = modifier.testTag("${route.route}-Search"),
                fields = viewModel.searchingFields!!,
            ) { action ->
                Log.debug("Search control action $action")
                when (action) {
                    is CigarsSearchFieldBaseViewModel.Action.ExecuteSearch -> {
                        viewModel.paging(true)
                    }

                    else -> {}
                }
            }

        }
    }

    @Composable
    override fun EntityListRow(entity: Cigar, modifier: Modifier) {
        CigarListRow(entity, modifier)
    }
}
