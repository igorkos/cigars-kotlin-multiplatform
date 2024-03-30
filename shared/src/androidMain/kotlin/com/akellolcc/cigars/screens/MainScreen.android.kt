package com.akellolcc.cigars.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.akellolcc.cigars.navigation.ITabItem
import com.akellolcc.cigars.navigation.TabBarVisibility

@Composable
actual fun BottomTabNavigation(tabs: List<ITabItem>) {
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
}