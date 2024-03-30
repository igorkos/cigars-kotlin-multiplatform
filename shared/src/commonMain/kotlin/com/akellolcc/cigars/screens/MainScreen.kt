package com.akellolcc.cigars.screens

import TextStyled
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
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
import cafe.adriel.voyager.navigator.tab.TabDisposable
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.akellolcc.cigars.common.theme.DefaultTheme
import com.akellolcc.cigars.navigation.CigarsRoute
import com.akellolcc.cigars.navigation.FavoritesRoute
import com.akellolcc.cigars.navigation.HumidorsRoute
import com.akellolcc.cigars.navigation.ITabItem
import com.akellolcc.cigars.navigation.NavRoute
import com.akellolcc.cigars.navigation.TabBarVisibility
import com.akellolcc.cigars.theme.TextStyles
import com.akellolcc.cigars.theme.loadIcon


class MainScreen : Screen {

    private var tabs : List<ITabItem> = listOf(
        CigarsScreen(CigarsRoute),
        HumidorsScreen(HumidorsRoute),
        FavoritesScreen(FavoritesRoute)
    )


    @OptIn(ExperimentalVoyagerApi::class)
    @Composable
    override fun Content() {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

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
                    TabNavigator(tabs[0], tabDisposable = { tabNavigator ->
                        TabDisposable(
                            navigator = tabNavigator,
                            tabs = tabs
                        )
                    }) {
                        Scaffold(
                            bottomBar = {
                                BottomTabNavigation(tabs)
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
expect fun BottomTabNavigation(tabs: List<ITabItem>)/* {
    var isTabsVisible by remember {mutableStateOf(TabBarVisibility.TabBarVisible)}

    val tabsVisibility =  remember{{ visible: Boolean ->
        isTabsVisible = if(visible) TabBarVisibility.TabBarVisible else TabBarVisibility.TabBarHidden
    }}

    AnimatedVisibility(visible = isTabsVisible == TabBarVisibility.TabBarVisible, enter = slideInVertically { height ->
        height
    }, exit = slideOutVertically { height ->
        height
    }) {
        NavigationBar {
            tabs.forEach {
                it.route.updateTabState = tabsVisibility
                TabNavigationItem(it.route, tabs)
            }
        }
    }
}*/
@Composable
fun RowScope.TabNavigationItem(tab: NavRoute, tabs: List<ITabItem>) {
    val tabNavigator = LocalTabNavigator.current
    val selected = (tabNavigator.current as ITabItem).route.route == tab.route
    NavigationBarItem(
        selected = selected,
        onClick = {
            tabNavigator.current = tabs.first{
                it.route.route == tab.route
            }
        },
        icon = { tab.icon?.let { loadIcon(it, Size(width = 24f, height = 24f))} },
        label = { TextStyled(text = tab.title, style = TextStyles.BarItemTitle) },
    )
}