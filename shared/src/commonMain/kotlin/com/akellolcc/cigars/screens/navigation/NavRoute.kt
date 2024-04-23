/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/22/24, 12:15 PM
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.akellolcc.cigars.screens.navigation

import com.akellolcc.cigars.mvvm.MainScreenViewModel
import com.akellolcc.cigars.theme.Images
import com.akellolcc.cigars.theme.Localize
import dev.icerock.moko.resources.ImageResource

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class NavRoute(
    route: String,
    title: String,
    icon: ImageResource,
    data: Any? = null,
    isTabsVisible: Boolean = true,
    isDrawerVisible: Boolean = false,
    isLoadingCover: Boolean = false,
) {
    val route: String
    val title: String
    var icon: ImageResource
    var isTabsVisible: Boolean
    var isDrawerVisible: Boolean
    var sharedViewModel: MainScreenViewModel?
    var data: Any?
    var isLoadingCover: Boolean
}

val CigarsRoute = NavRoute("CigarsScreen", Localize.title_cigars, Images.tab_icon_cigars)
val CigarsDetailsRoute = NavRoute(
    "CigarDetailsScreen",
    Localize.title_cigars,
    Images.tab_icon_cigars,
    isTabsVisible = false
)

val CigarHistoryRoute = NavRoute(
    "CigarHistoryScreen",
    Localize.title_cigars,
    Images.tab_icon_cigars,
    isTabsVisible = false
)
val HumidorDetailsRoute = NavRoute(
    "HumidorDetailsScreen",
    Localize.title_humidors,
    Images.tab_icon_humidors,
    isTabsVisible = false
)
val HumidorHistoryRoute = NavRoute(
    "HumidorHistoryScreen",
    Localize.title_humidors,
    Images.tab_icon_humidors,
    isTabsVisible = false
)
val ImagesViewRoute =
    NavRoute(
        "PhotosViewScreen",
        Localize.title_cigars,
        Images.tab_icon_cigars,
        isTabsVisible = false
    )
val HumidorsRoute = NavRoute("HumidorsScreen", Localize.title_humidors, Images.tab_icon_humidors)
val FavoritesRoute =
    NavRoute("FavoritesScreen", Localize.title_favorites, Images.tab_icon_favorites)
val HumidorCigarsRoute = NavRoute(
    "HumidorCigarScreen",
    Localize.title_cigars,
    Images.tab_icon_cigars,
    isTabsVisible = false
)
val SearchCigarRoute =
    NavRoute("SearchCigarsScreen", Localize.title_search, Images.tab_icon_search)