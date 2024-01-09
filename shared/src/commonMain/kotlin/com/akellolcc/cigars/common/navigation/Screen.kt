package com.akellolcc.cigars.common.navigation

import com.akellolcc.cigars.common.localize.Localize
import com.akellolcc.cigars.common.theme.Images
import dev.icerock.moko.resources.ImageResource


interface TabItem {
    val route: NavRoute
}

sealed class NavRoute(val route: String, val title: String, val icon: ImageResource? = null) {
    data object Home : NavRoute("gallery_screen", Localize.title_gallery)
    data object Login : NavRoute("login_screen", Localize.title_login)

    data object Cigars : NavRoute("photos_screen", Localize.title_cigars, Images.tab_icon_cigars)
    data object Humidors : NavRoute("albums_screen", Localize.title_humidors, Images.tab_icon_humidors)
    data object Favorites : NavRoute("camera_screen", Localize.title_favorites, Images.tab_icon_favorites)
}