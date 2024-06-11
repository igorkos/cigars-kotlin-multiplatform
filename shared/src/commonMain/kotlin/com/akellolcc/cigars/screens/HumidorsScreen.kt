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
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.akellolcc.cigars.databases.models.Humidor
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.mvvm.MainScreenViewModel
import com.akellolcc.cigars.mvvm.base.createViewModel
import com.akellolcc.cigars.mvvm.humidor.HumidorsViewModel
import com.akellolcc.cigars.screens.base.BaseTabListScreen
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
    @kotlinx.serialization.Transient
    @Transient
    override lateinit var viewModel: HumidorsViewModel

    @Composable
    override fun Content() {
        viewModel = rememberScreenModel { createViewModel(HumidorsViewModel::class) }
        LocalNavigator.currentOrThrow.push(HumidorsListScreen(route, viewModel))
    }
}

class HumidorsListScreen(
    route: NavRoute,
    @kotlinx.serialization.Transient
    @Transient
    override var viewModel: HumidorsViewModel
) :
    BaseTabListScreen<Humidor, HumidorsViewModel>(route) {

    @ExperimentalMaterial3Api
    @Composable
    override fun topTabBar(scrollBehavior: TopAppBarScrollBehavior?) {
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
            title = {
                TextStyled(
                    text = route.title,
                    Localize.nav_header_title_desc,
                    TextStyles.ScreenTitle,
                    labelStyle = TextStyles.None
                )
            },
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
                    text = Localize.humidor_cigars(
                        entity.count,
                        entity.holds - entity.count
                    ),
                    Localize.cigar_details_notes,
                    style = TextStyles.Subhead,
                    labelStyle = TextStyles.None,
                )
            }
        }
    }

    override fun handleAction(event: Any, navigator: Navigator) {
        val mainModel = createViewModel(MainScreenViewModel::class)
        when (event) {
            is HumidorsViewModel.Action.RouteToHumidor -> {
                Log.debug("Selected humidor ${event.humidor.rowid}")
                mainModel.isTabsVisible = false
                navigator.push(HumidorCigarsScreen(HumidorCigarsRoute.apply {
                    this.data = event.humidor
                }))
            }

            is HumidorsViewModel.Action.AddHumidor -> {
                mainModel.isTabsVisible = false
                navigator.push(
                    HumidorDetailsScreen(
                        HumidorDetailsRoute
                            .apply {
                                this.data = null
                            })
                )
            }

            else -> super.handleAction(event, navigator)
        }
    }

}



