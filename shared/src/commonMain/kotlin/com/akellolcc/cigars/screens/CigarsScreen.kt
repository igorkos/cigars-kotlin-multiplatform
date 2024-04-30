/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/29/24, 1:22 PM
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

import TextStyled
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.navigator.Navigator
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.mvvm.CigarsScreenViewModel
import com.akellolcc.cigars.mvvm.MainScreenViewModel
import com.akellolcc.cigars.mvvm.createViewModel
import com.akellolcc.cigars.screens.components.CigarListRow
import com.akellolcc.cigars.screens.navigation.CigarsDetailsRoute
import com.akellolcc.cigars.screens.navigation.ITabItem
import com.akellolcc.cigars.screens.navigation.NavRoute
import com.akellolcc.cigars.screens.search.CigarFilterParameters
import com.akellolcc.cigars.screens.search.SearchComponent
import com.akellolcc.cigars.screens.search.SearchParameterAction
import com.akellolcc.cigars.theme.Images
import com.akellolcc.cigars.theme.TextStyles
import com.akellolcc.cigars.theme.loadIcon
import kotlinx.coroutines.flow.flow
import kotlin.jvm.Transient

class CigarsScreen(
    override val route: NavRoute
) : ITabItem<CigarsScreenViewModel> {
    @Transient
    override lateinit var viewModel: CigarsScreenViewModel

    @Composable
    override fun Content() {
        viewModel = rememberScreenModel { createViewModel(CigarsScreenViewModel::class) }
        Navigator(CigarsListScreen<CigarsScreenViewModel>(route, viewModel))
    }
}

open class CigarsListScreen<V : ScreenModel>(
    override val route: NavRoute,
    @Transient
    override var viewModel: CigarsScreenViewModel
) :
    BaseTabListScreen<CigarsScreenViewModel.CigarsAction, Cigar, CigarsScreenViewModel>(route) {
    @Composable
    override fun RightActionMenu(onDismiss: () -> Unit) {
        viewModel.sortingFields?.map {
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
                    viewModel.sorting = it
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
                mainModel.isTabsVisible = false
                navigator?.push(CigarDetailsScreen(CigarsDetailsRoute.apply {
                    data = event.cigar
                }))
            }

            is CigarsScreenViewModel.CigarsAction.ShowError -> TODO()
        }
    }

    @Composable
    override fun ListHeader(modifier: Modifier) {
        if (viewModel.search) {
            val fields = remember { CigarFilterParameters() }
            LaunchedEffect(fields.selected) {
                viewModel.searchingFields = fields.selected
            }
            SearchComponent(
                modifier = modifier,
                loading = viewModel.loading,
                fields = fields,
            ) { action, _ ->
                flow {
                    when (action) {
                        SearchParameterAction.Completed -> {
                            viewModel.reload()
                        }

                        else -> {}
                    }
                    emit(true)
                }
            }
        }
    }

    @Composable
    override fun EntityListRow(entity: Cigar, modifier: Modifier) {
        CigarListRow(entity, modifier)
    }
}
