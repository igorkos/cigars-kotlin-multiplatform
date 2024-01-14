package com.akellolcc.cigars.navigation

import com.akellolcc.cigars.theme.Images
import dev.icerock.moko.resources.ImageResource
import com.akellolcc.cigars.MR
import dev.icerock.moko.resources.StringResource

interface TabItem {
    val route: NavRoute
}

sealed class NavRoute(val route: String, val title: StringResource, val icon: ImageResource? = null) {
    data object Home : NavRoute("gallery_screen", MR.strings.title_gallery)
    data object Login : NavRoute("login_screen", MR.strings.title_login)

    data object Cigars : NavRoute("photos_screen", MR.strings.title_cigars, Images.tab_icon_cigars)
    data object Humidors : NavRoute("albums_screen", MR.strings.title_humidors, Images.tab_icon_humidors)
    data object Favorites : NavRoute("camera_screen", MR.strings.title_favorites, Images.tab_icon_favorites)
}