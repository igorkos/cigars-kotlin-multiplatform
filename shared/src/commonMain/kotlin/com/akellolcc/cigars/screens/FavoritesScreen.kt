package com.akellolcc.cigars.screens

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.LocalNavigator
import com.akellolcc.cigars.mvvm.FavoritesScreenViewModel
import com.akellolcc.cigars.navigation.ITabItem
import com.akellolcc.cigars.navigation.NavRoute
import kotlin.jvm.Transient

class FavoritesScreen(override val route: NavRoute) : ITabItem {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        navigator?.push(FavoritesListScreen(route))
        //Navigator(CigarsListScreen(route))
    }
}

class FavoritesListScreen(route: NavRoute) : CigarsListScreen(route) {

    @Transient
    override val viewModel = FavoritesScreenViewModel()

}
