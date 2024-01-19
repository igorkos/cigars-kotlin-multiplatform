package com.akellolcc.cigars.navigation

import com.akellolcc.cigars.theme.Images
import dev.icerock.moko.resources.ImageResource
import com.akellolcc.cigars.theme.Localize

interface TabItem {
    val route: NavRoute
}

sealed class NavRoute(val route: String, val title: String, val icon: ImageResource? = null) {
    data object Home : NavRoute("gallery_screen", "")
    data object Login : NavRoute("login_screen", "")

    data object Cigars : NavRoute("photos_screen", Localize.title_cigars, Images.tab_icon_cigars)
    data object Humidors : NavRoute("albums_screen", Localize.title_humidors, Images.tab_icon_humidors)
    data object Favorites : NavRoute("camera_screen", Localize.title_favorites, Images.tab_icon_favorites)
}