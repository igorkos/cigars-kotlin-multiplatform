package com.akellolcc.cigars.navigation

import cafe.adriel.voyager.navigator.tab.Tab

interface ITabItem : Tab {
    val route: NavRoute
}
