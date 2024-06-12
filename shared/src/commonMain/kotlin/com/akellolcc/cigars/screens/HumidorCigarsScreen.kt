/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/11/24, 7:35 PM
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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.navigator.Navigator
import com.akellolcc.cigars.databases.models.HumidorCigar
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.mvvm.MainScreenViewModel
import com.akellolcc.cigars.mvvm.base.createViewModel
import com.akellolcc.cigars.mvvm.humidor.HumidorCigarsScreenViewModel
import com.akellolcc.cigars.mvvm.search.CigarsSearchFieldBaseViewModel
import com.akellolcc.cigars.screens.base.BaseTabListScreen
import com.akellolcc.cigars.screens.components.BackButton
import com.akellolcc.cigars.screens.components.CigarListRow
import com.akellolcc.cigars.screens.components.TextStyled
import com.akellolcc.cigars.screens.components.search.SearchComponent
import com.akellolcc.cigars.screens.components.search.data.FilterParameter
import com.akellolcc.cigars.screens.navigation.CigarsDetailsRoute
import com.akellolcc.cigars.screens.navigation.HumidorDetailsRoute
import com.akellolcc.cigars.screens.navigation.HumidorHistoryRoute
import com.akellolcc.cigars.screens.navigation.NavRoute
import com.akellolcc.cigars.theme.Images
import com.akellolcc.cigars.theme.Localize
import com.akellolcc.cigars.theme.TextStyles
import com.akellolcc.cigars.theme.loadIcon

open class HumidorCigarsScreen(override val route: NavRoute) :
    BaseTabListScreen<HumidorCigar, HumidorCigarsScreenViewModel>(
        route
    ) {

    @Composable
    override fun Content() {
        viewModel =
            rememberScreenModel { createViewModel(HumidorCigarsScreenViewModel::class, route.data) }
        super.Content()
    }

    override fun handleAction(
        event: Any,
        navigator: Navigator
    ) {
        val mainModel = createViewModel(MainScreenViewModel::class)
        when (event) {
            is HumidorCigarsScreenViewModel.CigarsAction.RouteToCigar -> {
                Log.debug("Selected cigar ${event.cigar.rowid}")
                mainModel.isTabsVisible = false
                navigator.push(CigarDetailsScreen(CigarsDetailsRoute.apply {
                    data = event.cigar
                }))
            }

            is HumidorCigarsScreenViewModel.CigarsAction.AddCigar -> {
                mainModel.isTabsVisible = false
                navigator.push(CigarDetailsScreen(CigarsDetailsRoute.apply {
                    this.data = null
                }))
            }

            is HumidorCigarsScreenViewModel.CigarsAction.RouteToHumidorDetails -> {
                mainModel.isTabsVisible = false
                navigator.push(
                    HumidorDetailsScreen(
                        HumidorDetailsRoute
                            .apply {
                                this.data = event.humidor
                            })
                )
            }

            is HumidorCigarsScreenViewModel.CigarsAction.OpenHistory -> {
                mainModel.isTabsVisible = false
                navigator.push(
                    HumidorHistoryScreen(
                        HumidorHistoryRoute
                            .apply {
                                this.data = viewModel.humidor
                            })
                )
            }

            else -> super.handleAction(event, navigator)
        }
    }

    @Composable
    override fun topTabBarNavigation() {
        BackButton {
            viewModel.onBackPress()
        }
    }

    override fun performTabBarActions(action: FilterParameter<*>) {
        when (action.key) {
            Localize.cigar_details_top_bar_history_desc -> {
                viewModel.openHistory()
            }

            Localize.cigar_details_top_bar_info_desc -> {
                viewModel.humidorDetails()
            }

            else -> {
                super.performTabBarActions(action)
            }
        }
    }

    @Composable
    override fun topTabBarActions() {
        IconButton(onClick = { viewModel.addCigar() }) {
            loadIcon(Images.icon_menu_plus, Size(24.0F, 24.0F))
        }
        super.topTabBarActions()
    }

    @Composable
    override fun ContentHeader(modifier: Modifier) {
        if (viewModel.search && viewModel.searchingFields != null) {
            SearchComponent(
                modifier = modifier.semantics { contentDescription = Localize.screen_list_filter_control_descr },
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
        } else {
            Column(modifier = Modifier.padding(bottom = 16.dp)) {
                TextStyled(
                    text = viewModel.humidor.name,
                    Localize.nav_header_title_desc,
                    TextStyles.ScreenTitle,
                    labelStyle = TextStyles.None
                )
                TextStyled(
                    text = Localize.humidor_cigars(
                        viewModel.humidor.count,
                        viewModel.humidor.holds - viewModel.humidor.count
                    ),
                    Localize.humidor_details_humidor,
                    style = TextStyles.Subhead,
                    labelStyle = TextStyles.None
                )
            }
        }
    }

    @Composable
    override fun EntityListRow(entity: HumidorCigar, modifier: Modifier) {
        CigarListRow(entity, modifier)
    }

}
