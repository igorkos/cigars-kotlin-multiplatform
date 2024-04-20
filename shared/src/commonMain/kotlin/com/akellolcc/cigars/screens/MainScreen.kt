/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/19/24, 6:00 PM
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.akellolcc.cigars.common.theme.DefaultTheme
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.mvvm.MainScreenViewModel
import com.akellolcc.cigars.navigation.CigarsRoute
import com.akellolcc.cigars.navigation.FavoritesRoute
import com.akellolcc.cigars.navigation.HumidorsRoute
import com.akellolcc.cigars.navigation.ITabItem
import com.akellolcc.cigars.navigation.NavRoute
import com.akellolcc.cigars.navigation.SearchCigarRoute
import com.akellolcc.cigars.theme.TextStyles
import com.akellolcc.cigars.theme.loadIcon
import kotlin.jvm.Transient

private var tabs: List<ITabItem> = listOf(
    CigarsScreen(CigarsRoute),
    HumidorsScreen(HumidorsRoute),
    FavoritesScreen(FavoritesRoute),
    SearchCigarScreen(SearchCigarRoute)
)

class MainScreen : Screen {

    @Transient
    private val viewModel = MainScreenViewModel()

    @OptIn(ExperimentalVoyagerApi::class)
    @Composable
    override fun Content() {
        var isTabsVisible by remember { mutableStateOf(true) }
        var isDrawerVisible by remember { mutableStateOf(false) }
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed) {
            isDrawerVisible = it == DrawerValue.Open
            true
        }

        viewModel.observeEvents {
            when (it) {
                is MainScreenViewModel.MainScreenActions.ShowError -> TODO()
                is MainScreenViewModel.MainScreenActions.TabsVisibility -> {
                    Log.debug("MainScreen received tabs visible ${it.isVisible}")
                    isTabsVisible = it.isVisible
                }

                is MainScreenViewModel.MainScreenActions.OpenDrawer -> {
                    //throw RuntimeException("Test Crash")
                    isDrawerVisible = it.isVisible
                }
            }
        }

        LaunchedEffect(isDrawerVisible) {
            if (isDrawerVisible) {
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
                gesturesEnabled = isTabsVisible,
            ) {
                TabNavigator(tabs[0]) {
                    Scaffold(
                        bottomBar = {
                            AnimatedVisibility(
                                visible = isTabsVisible,
                                enter = slideInVertically { height ->
                                    height
                                },
                                exit = slideOutVertically { height ->
                                    height
                                }) {
                                NavigationBar {
                                    tabs.forEach {
                                        it.route.sharedViewModel = viewModel
                                        TabNavigationItem(it.route, tabs)
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

@Composable
fun RowScope.TabNavigationItem(tab: NavRoute, tabs: List<ITabItem>) {
    val tabNavigator = LocalTabNavigator.current
    val selected = (tabNavigator.current as ITabItem).route.route == tab.route
    NavigationBarItem(
        selected = selected,
        onClick = {
            tabNavigator.current = tabs.first {
                it.route.route == tab.route
            }
        },
        icon = { loadIcon(tab.icon, Size(width = 24f, height = 24f)) },
        label = { TextStyled(text = tab.title, style = TextStyles.BarItemTitle) },
    )
}