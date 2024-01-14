package com.akellolcc.cigars.screens

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material.LocalContentColor
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabDisposable
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.akellolcc.cigars.common.theme.DefaultTheme
import com.akellolcc.cigars.navigation.NavRoute
import com.akellolcc.cigars.navigation.TabItem
import com.akellolcc.cigars.theme.LocalBackgroundTheme
import com.akellolcc.cigars.theme.MaterialColors
import com.akellolcc.cigars.theme.loadIcon
import com.akellolcc.cigars.theme.materialColor
import dev.icerock.moko.resources.compose.stringResource


class MainScreen : Screen {
    //val tabItems = arrayOf(NavRoute.Gallery, NavRoute.Albums, NavRoute.Camera)

    @OptIn(ExperimentalVoyagerApi::class, ExperimentalStdlibApi::class)
    @Composable
    override fun Content() {
        DefaultTheme {
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
            val scope = rememberCoroutineScope()
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    ModalDrawerSheet(
                        modifier = with(Modifier) {
                            fillMaxWidth(0.8f).navigationBarsPadding()
                        },
                    ) {
                        Text("Drawer title", modifier = Modifier.padding(16.dp))
                        Divider()
                        NavigationDrawerItem(
                            label = { Text(text = "Drawer Item") },
                            selected = false,
                            onClick = { /*TODO*/ }
                        )
                    }
                },
                gesturesEnabled = true
            ) {
                CompositionLocalProvider(LocalContentColor provides LocalBackgroundTheme.current.color) {
                    TabNavigator(CigarsScreen(NavRoute.Cigars), tabDisposable = {
                        TabDisposable(
                            navigator = it,
                            tabs = listOf(
                                CigarsScreen(NavRoute.Cigars),
                                HumidorsScreen(NavRoute.Humidors),
                                FavoritesScreen(NavRoute.Favorites)
                            )
                        )
                    }) {
                        Scaffold(
                            bottomBar = {
                                NavigationBar(
                                    containerColor = materialColor(MaterialColors.color_surfaceTint).copy(alpha = 0.6f),
                                ) {
                                    TabNavigationItem(CigarsScreen(NavRoute.Cigars))
                                    TabNavigationItem(HumidorsScreen(NavRoute.Humidors))
                                    TabNavigationItem(FavoritesScreen(NavRoute.Favorites))
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
    private fun RowScope.TabNavigationItem(tab: TabItem) {
        val tabNavigator = LocalTabNavigator.current
        val selected = (tabNavigator.current as TabItem).route.route == tab.route.route
        NavigationBarItem(
            selected = selected,
            onClick = { tabNavigator.current = tab as Tab },
            icon = { loadIcon(tab.route.icon!!, Size(width = 24f, height = 24f)) },
            label = { Text(text = stringResource(tab.route.title), fontSize = 12.sp) },
        )
    }
}