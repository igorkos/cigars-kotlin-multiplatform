/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/13/24, 2:03 PM
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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.jetpack.ProvideNavigatorLifecycleKMPSupport
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.akellolcc.cigars.logging.AnalyticsEvents
import com.akellolcc.cigars.logging.AnalyticsParams
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.mvvm.MainScreenViewModel
import com.akellolcc.cigars.mvvm.base.ActionsViewModel
import com.akellolcc.cigars.mvvm.base.createViewModel
import com.akellolcc.cigars.screens.components.TextStyled
import com.akellolcc.cigars.screens.navigation.CigarsRoute
import com.akellolcc.cigars.screens.navigation.FavoritesRoute
import com.akellolcc.cigars.screens.navigation.HumidorsRoute
import com.akellolcc.cigars.screens.navigation.ITabItem
import com.akellolcc.cigars.screens.navigation.MainRoute
import com.akellolcc.cigars.screens.navigation.NavRoute
import com.akellolcc.cigars.screens.navigation.SearchCigarRoute
import com.akellolcc.cigars.theme.DefaultTheme
import com.akellolcc.cigars.theme.Localize
import com.akellolcc.cigars.theme.TextStyles
import com.akellolcc.cigars.theme.loadIcon
import com.akellolcc.cigars.utils.ui.BackHandler
import kotlin.jvm.Transient

private var tabs: List<ITabItem<*>> = listOf(
    CigarsScreen(CigarsRoute),
    HumidorsScreen(HumidorsRoute),
    FavoritesScreen(FavoritesRoute),
    SearchScreen(SearchCigarRoute)
)


class MainScreen :
    ITabItem<MainScreenViewModel> {
    override val route: NavRoute = MainRoute

    @kotlinx.serialization.Transient
    @Transient
    override lateinit var viewModel: MainScreenViewModel

    @OptIn(ExperimentalVoyagerApi::class, InternalVoyagerApi::class)
    @Composable
    override fun Content() {

        viewModel = rememberScreenModel { createViewModel(MainScreenViewModel::class) }

        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed) {
            viewModel.isDrawerVisible = it == DrawerValue.Open
            true
        }

        viewModel.observeEvents(tag()) {
            when (it) {
                is ActionsViewModel.CommonAction.ShowError -> TODO()
            }
        }

        LaunchedEffect(viewModel.isDrawerVisible) {
            if (viewModel.isDrawerVisible) {
                drawerState.open()
            } else {
                drawerState.close()
            }
        }

        DefaultTheme {
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    ModalDrawerSheet(
                        modifier = with(Modifier) {
                            fillMaxWidth(0.8f).navigationBarsPadding()
                        },
                    ) {
                        Text("Drawer title", modifier = Modifier.padding(16.dp))
                        HorizontalDivider()
                        NavigationDrawerItem(
                            label = { Text(text = "Drawer Item") },
                            selected = false,
                            onClick = {

                            }
                        )
                    }
                },
                gesturesEnabled = viewModel.isTabsVisible,
            ) {
                ProvideNavigatorLifecycleKMPSupport {
                    TabNavigator(
                        CigarsScreen(CigarsRoute)
                    ) {
                        val navigator = LocalNavigator.currentOrThrow
                        BackHandler {
                            Log.debug("Main Screen Back pressed")
                            if (!(navigator.lastItem is SearchCigarScreen || navigator.lastItem is HumidorsListScreen || navigator.lastItem is FavoritesListScreen || navigator.lastItem is CigarsListScreen<*>)) {
                                (navigator.lastItem as ITabItem<*>).viewModel.onBackPress()
                                navigator.dispose(this)
                                true
                            } else false
                        }
                        Scaffold(
                            bottomBar = {
                                AnimatedVisibility(
                                    visible = viewModel.isTabsVisible,
                                    enter = slideInVertically { height ->
                                        height
                                    },
                                    exit = slideOutVertically { height ->
                                        height
                                    }) {
                                    NavigationBar {
                                        tabs.map {
                                            TabNavigationItem(it)
                                        }
                                    }
                                }
                            },
                        ) {
                            CurrentTab()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RowScope.TabNavigationItem(tab: ITabItem<*>) {
    val tabNavigator = LocalTabNavigator.current
    NavigationBarItem(
        selected = tabNavigator.current == tab,
        onClick = {
            tabNavigator.current = tab
            Log.debug(
                "Selected tab ${tab.route.route}", AnalyticsEvents.ScreenEnter, mapOf(
                    AnalyticsParams.ContentId.parm to tab.route.route
                )
            )
        },
        icon = { loadIcon(tab.route.icon, Size(width = 24f, height = 24f)) },
        label = {
            TextStyled(
                tab.route.title,
                Localize.nav_tab_title_desc,
                TextStyles.BarItemTitle,
                labelStyle = TextStyles.None
            )
        },
    )
}