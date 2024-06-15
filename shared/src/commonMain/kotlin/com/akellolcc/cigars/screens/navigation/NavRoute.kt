/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/14/24, 12:24 PM
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
 ******************************************************************************************************************************************/

package com.akellolcc.cigars.screens.navigation

import com.akellolcc.cigars.theme.Images
import com.akellolcc.cigars.theme.Localize
import dev.icerock.moko.resources.ImageResource

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class NavRoute(
    route: String,
    title: String,
    semantics: String,
    icon: ImageResource,
    data: Any? = null,
    isTabsVisible: Boolean = false,
    isDrawerVisible: Boolean = false,
    isLoadingCover: Boolean = false,
    isSearchEnabled: Boolean = false,
) {
    val route: String
    val title: String
    val semantics: String
    val icon: ImageResource
    var data: Any?
    val isTabsVisible: Boolean
    val isDrawerVisible: Boolean
    val isLoadingCover: Boolean
    val isSearchEnabled: Boolean

    fun applyData(data: Any?): NavRoute
}

val MainRoute = NavRoute(
    "MainScreen",
    "",
    semantics = "MainScreen",
    Images.tab_icon_cigars,
)
val CigarsRoute = NavRoute(
    "CigarsScreen",
    Localize.title_cigars,
    Localize.title_cigars_desc,
    Images.tab_icon_cigars,
    isTabsVisible = true,
    isSearchEnabled = true
)
val HumidorsRoute = NavRoute(
    "HumidorsScreen",
    Localize.title_humidors,
    Localize.title_humidors_desc,
    Images.tab_icon_humidors,
    isTabsVisible = true,
)
val FavoritesRoute = NavRoute(
    "FavoritesScreen",
    Localize.title_favorites,
    Localize.title_favorites_desc,
    Images.tab_icon_favorites,
    isTabsVisible = true,
    isSearchEnabled = true
)
val SearchCigarRoute = NavRoute(
    "SearchCigarsScreen",
    Localize.title_search,
    Localize.title_search_desc,
    Images.tab_icon_search,
    isTabsVisible = true,
)
val CigarSearchDetailsRoute = NavRoute(
    "CigarSearchDetailsScreen",
    Localize.title_cigars_search_details,
    Localize.title_cigars_search_details_desc,
    Images.tab_icon_cigars,
)

val CigarsDetailsRoute = NavRoute(
    "CigarDetailsScreen",
    Localize.title_cigars_details,
    Localize.title_cigars_details_desc,
    Images.tab_icon_cigars,
)
val CigarHistoryRoute = NavRoute(
    "CigarHistoryScreen",
    Localize.title_cigar_history,
    Localize.title_cigar_history_desc,
    Images.tab_icon_cigars,
)
val HumidorDetailsRoute = NavRoute(
    "HumidorDetailsScreen",
    Localize.title_humidor_details,
    Localize.title_humidor_details_desc,
    Images.tab_icon_humidors,
)
val HumidorHistoryRoute = NavRoute(
    "HumidorHistoryScreen",
    Localize.title_humidor_history,
    Localize.title_humidor_history_desc,
    Images.tab_icon_humidors,
)
val CigarImagesViewRoute = NavRoute(
    "PhotosViewScreen",
    Localize.title_cigar_images,
    Localize.title_cigar_images_desc,
    Images.tab_icon_cigars,
)
val HumidorImagesViewRoute = NavRoute(
    "PhotosViewScreen",
    Localize.title_humidor_images,
    Localize.title_humidor_images_desc,
    Images.tab_icon_cigars,
)
val HumidorCigarsRoute = NavRoute(
    "HumidorCigarScreen",
    Localize.title_humidor_cigars,
    Localize.title_humidor_cigars_desc,
    Images.tab_icon_cigars,
    isSearchEnabled = true
)