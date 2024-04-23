/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/23/24, 12:40 AM
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
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.navigator.Navigator
import com.akellolcc.cigars.databases.extensions.Humidor
import com.akellolcc.cigars.databases.extensions.HumidorCigar
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.mvvm.HumidorCigarsScreenViewModel
import com.akellolcc.cigars.mvvm.MainScreenViewModel
import com.akellolcc.cigars.screens.components.CigarListRow
import com.akellolcc.cigars.screens.navigation.CigarsDetailsRoute
import com.akellolcc.cigars.screens.navigation.HumidorDetailsRoute
import com.akellolcc.cigars.screens.navigation.HumidorHistoryRoute
import com.akellolcc.cigars.screens.navigation.NavRoute
import com.akellolcc.cigars.theme.Images
import com.akellolcc.cigars.theme.Localize
import com.akellolcc.cigars.theme.MaterialColors
import com.akellolcc.cigars.theme.TextStyles
import com.akellolcc.cigars.theme.loadIcon
import com.akellolcc.cigars.theme.materialColor
import kotlin.jvm.Transient

class HumidorCigarsScreen(override val route: NavRoute) :
    BaseTabListScreen<HumidorCigarsScreenViewModel.CigarsAction, HumidorCigar, HumidorCigarsScreenViewModel>(
        route
    ) {
    @Transient
    override lateinit var viewModel: HumidorCigarsScreenViewModel

    @Composable
    override fun Content() {
        viewModel = rememberScreenModel { HumidorCigarsScreenViewModel(route.data as Humidor) }
        super.Content()
    }

    override fun handleAction(
        event: HumidorCigarsScreenViewModel.CigarsAction,
        navigator: Navigator?
    ) {
        val mainModel = route.sharedViewModel as MainScreenViewModel
        when (event) {
            is HumidorCigarsScreenViewModel.CigarsAction.RouteToCigar -> {
                Log.debug("Selected cigar ${event.cigar.rowid}")
                mainModel.isTabsVisible = false
                navigator?.push(CigarDetailsScreen(CigarsDetailsRoute.apply {
                    data = event.cigar
                    sharedViewModel = mainModel
                }))
            }

            is HumidorCigarsScreenViewModel.CigarsAction.ShowError -> TODO()
            is HumidorCigarsScreenViewModel.CigarsAction.AddCigar -> {
                mainModel.isTabsVisible = false
                navigator?.push(CigarDetailsScreen(CigarsDetailsRoute.apply {
                    this.data = null
                    sharedViewModel = mainModel
                }))
            }

            is HumidorCigarsScreenViewModel.CigarsAction.RouteToHumidorDetails -> {
                mainModel.isTabsVisible = false
                navigator?.push(
                    HumidorDetailsScreen(
                        HumidorDetailsRoute
                            .apply {
                                this.data = event.humidor
                                sharedViewModel = mainModel
                            })
                )
            }

            is HumidorCigarsScreenViewModel.CigarsAction.OpenHistory -> {
                mainModel.isTabsVisible = false
                navigator?.push(
                    HumidorHistoryScreen(
                        HumidorHistoryRoute
                            .apply {
                                this.data = viewModel.humidor
                                sharedViewModel = mainModel
                            })
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun topTabBar(scrollBehavior: TopAppBarScrollBehavior?, navigator: Navigator?) {
        val topColors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = materialColor(MaterialColors.color_transparent),
            navigationIconContentColor = materialColor(MaterialColors.color_onPrimaryContainer),
            actionIconContentColor = materialColor(MaterialColors.color_onPrimaryContainer),
        )
        LargeTopAppBar(
            title = {
                Column {
                    TextStyled(text = viewModel.humidor.name, style = TextStyles.ScreenTitle)
                    TextStyled(
                        text = Localize.humidor_cigars(
                            viewModel.humidor.count,
                            viewModel.humidor.holds - viewModel.humidor.count
                        ), style = TextStyles.Subhead
                    )
                }
            },
            colors = topColors,
            navigationIcon = {
                IconButton(onClick = {
                    navigator?.pop()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            },
            actions = {
                IconButton(onClick = { viewModel.openHistory() }) {
                    loadIcon(Images.icon_menu_history, Size(24.0F, 24.0F))
                }
                IconButton(onClick = { viewModel.humidorDetails() }) {
                    loadIcon(Images.icon_menu_info, Size(24.0F, 24.0F))
                }
                IconButton(onClick = { viewModel.addCigar() }) {
                    loadIcon(Images.icon_menu_plus, Size(24.0F, 24.0F))
                }
            })
    }

    @Composable
    override fun EntityListRow(entity: HumidorCigar, modifier: Modifier) {
        CigarListRow(entity, modifier)
    }

}
