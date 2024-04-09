package com.akellolcc.cigars.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.akellolcc.cigars.theme.imagePainter

interface ITabItem : Tab {
    val route: NavRoute
    override val options: TabOptions
        @Composable
        get() {
            val icon = imagePainter(route.icon)
            return remember {
                TabOptions(
                    index = 0u,
                    title = route.title,
                    icon = icon
                )
            }
        }
}
