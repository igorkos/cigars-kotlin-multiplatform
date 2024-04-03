package com.akellolcc.cigars.navigation

import com.akellolcc.cigars.mvvm.MainScreenViewModel
import com.akellolcc.cigars.theme.Images
import com.akellolcc.cigars.theme.Localize
import dev.icerock.moko.resources.ImageResource

enum class TabBarVisibility{
    TabBarVisible,
    TabBarHidden
}

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class NavRoute(route: String, title: String, icon: ImageResource?, data: Any? = null){
    val route: String
    val title: String
    var icon: ImageResource?
    var updateTabState: ((Boolean) -> Unit)?
    var sharedViewModel: MainScreenViewModel?
    var data: Any?
}

val CigarsRoute =  NavRoute("cigars_screen", Localize.title_cigars, Images.tab_icon_cigars)
val CigarsDetailsRoute =  NavRoute("cigar_details_screen", Localize.title_cigars, Images.tab_icon_cigars)
val ImagesViewRoute =  NavRoute("photos_screen", Localize.title_cigars, Images.tab_icon_cigars)
val HumidorsRoute =  NavRoute("humidors_screen", Localize.title_humidors, Images.tab_icon_humidors)
val FavoritesRoute =  NavRoute("favorites_screen", Localize.title_favorites, Images.tab_icon_favorites)
