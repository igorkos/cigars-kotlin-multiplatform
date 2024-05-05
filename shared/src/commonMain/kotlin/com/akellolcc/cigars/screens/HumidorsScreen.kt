/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/1/24, 12:36 AM
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.navigator.Navigator
import com.akellolcc.cigars.databases.extensions.Humidor
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.mvvm.HumidorsViewModel
import com.akellolcc.cigars.mvvm.MainScreenViewModel
import com.akellolcc.cigars.mvvm.createViewModel
import com.akellolcc.cigars.screens.components.TextStyled
import com.akellolcc.cigars.screens.navigation.HumidorCigarsRoute
import com.akellolcc.cigars.screens.navigation.HumidorDetailsRoute
import com.akellolcc.cigars.screens.navigation.ITabItem
import com.akellolcc.cigars.screens.navigation.NavRoute
import com.akellolcc.cigars.theme.Images
import com.akellolcc.cigars.theme.Localize
import com.akellolcc.cigars.theme.MaterialColors
import com.akellolcc.cigars.theme.TextStyles
import com.akellolcc.cigars.theme.loadIcon
import com.akellolcc.cigars.theme.materialColor
import kotlin.jvm.Transient

class HumidorsScreen(override val route: NavRoute) : ITabItem<HumidorsViewModel> {
    @Transient
    override lateinit var viewModel: HumidorsViewModel

    @Composable
    override fun Content() {
        viewModel =
            rememberScreenModel { createViewModel(HumidorsViewModel::class) }
        Navigator(HumidorsListScreen(route, viewModel))
    }
}

class HumidorsListScreen(
    route: NavRoute,
    @Transient
    override var viewModel: HumidorsViewModel
) :
    BaseTabListScreen<HumidorsViewModel.Action, Humidor, HumidorsViewModel>(route) {

    @ExperimentalMaterial3Api
    @Composable
    override fun topTabBar(scrollBehavior: TopAppBarScrollBehavior?, navigator: Navigator?) {
        CenterAlignedTopAppBar(
            navigationIcon = {
                IconButton(onClick = {
                    val mainModel = createViewModel(MainScreenViewModel::class)
                    mainModel.isDrawerVisible = true
                }) { loadIcon(Images.icon_menu_dots, Size(24.0F, 24.0F)) }
            },
            actions = {
                IconButton(onClick = { viewModel.addHumidor() }) {
                    loadIcon(Images.icon_menu_plus, Size(24.0F, 24.0F))
                }
            },
            title = { TextStyled(text = route.title, style = TextStyles.ScreenTitle) },
            scrollBehavior = scrollBehavior
        )
    }

    @Composable
    override fun EntityListRow(entity: Humidor, modifier: Modifier) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = materialColor(MaterialColors.color_primaryContainer),
            ),
            modifier = modifier
                .fillMaxWidth()
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
                    text = Localize.humidor_cigars(
                        entity.count,
                        entity.holds - entity.count
                    ),
                    style = TextStyles.Subhead
                )
            }
        }
    }

    override fun handleAction(event: HumidorsViewModel.Action, navigator: Navigator?) {
        val mainModel = createViewModel(MainScreenViewModel::class)
        when (event) {
            is HumidorsViewModel.Action.RouteToHumidor -> {
                Log.debug("Selected humidor ${event.humidor.rowid}")
                mainModel.isTabsVisible = false
                navigator?.push(HumidorCigarsScreen(HumidorCigarsRoute.apply {
                    this.data = event.humidor
                }))
            }

            is HumidorsViewModel.Action.ShowError -> TODO()
            is HumidorsViewModel.Action.AddHumidor -> {
                mainModel.isTabsVisible = false
                navigator?.push(
                    HumidorDetailsScreen(
                        HumidorDetailsRoute
                            .apply {
                                this.data = null
                            })
                )
            }
        }
    }

}



