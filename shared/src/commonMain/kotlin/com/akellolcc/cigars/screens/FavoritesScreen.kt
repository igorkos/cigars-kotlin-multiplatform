package com.akellolcc.cigars.screens

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import com.akellolcc.cigars.mvvm.FavoritesScreenViewModel
import com.akellolcc.cigars.navigation.ITabItem
import com.akellolcc.cigars.navigation.NavRoute
import kotlin.jvm.Transient

class FavoritesScreen(override val route: NavRoute) : ITabItem {
    private val screen = FavoritesListScreen(route)
    @Composable
    override fun Content() {
        Navigator(screen)
    }
}

class FavoritesListScreen(route: NavRoute) : CigarsListScreen(route) {

    @Transient
    override val viewModel = FavoritesScreenViewModel()

}
