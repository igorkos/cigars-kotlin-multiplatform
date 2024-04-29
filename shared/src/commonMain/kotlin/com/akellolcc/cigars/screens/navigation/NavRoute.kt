/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/28/24, 2:19 PM
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
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class NavRoute(
    val route: String,
    val title: String,
    @kotlin.jvm.Transient
    @Transient
    var icon: ImageResource? = null,
    var isTabsVisible: Boolean = false,
    var isDrawerVisible: Boolean = false,
    var isLoadingCover: Boolean = false,
    var isSearchEnabled: Boolean = false,
    @kotlin.jvm.Transient
    @Transient
    var data: Any? = null
)

val MainRoute = NavRoute(
    "MainScreen",
    "",
    Images.tab_icon_cigars
)

val CigarsRoute = NavRoute(
    "CigarsScreen",
    Localize.title_cigars,
    Images.tab_icon_cigars,
    isTabsVisible = true,
    isSearchEnabled = true
)
val HumidorsRoute = NavRoute(
    "HumidorsScreen",
    Localize.title_humidors,
    Images.tab_icon_humidors,
    isTabsVisible = true,
)
val FavoritesRoute = NavRoute(
    "FavoritesScreen",
    Localize.title_favorites,
    Images.tab_icon_favorites,
    isTabsVisible = true,
    isSearchEnabled = true
)

val SearchCigarRoute = NavRoute(
    "SearchCigarsScreen",
    Localize.title_search,
    Images.tab_icon_search,
    isTabsVisible = true,
)

val CigarsDetailsRoute = NavRoute(
    "CigarDetailsScreen",
    Localize.title_cigars,
    Images.tab_icon_cigars
)

val CigarHistoryRoute = NavRoute(
    "CigarHistoryScreen",
    Localize.title_cigars,
    Images.tab_icon_cigars,
)
val HumidorDetailsRoute = NavRoute(
    "HumidorDetailsScreen",
    Localize.title_humidors,
    Images.tab_icon_humidors,
)
val HumidorHistoryRoute = NavRoute(
    "HumidorHistoryScreen",
    Localize.title_humidors,
    Images.tab_icon_humidors,
)
val ImagesViewRoute = NavRoute(
    "PhotosViewScreen",
    Localize.title_cigars,
    Images.tab_icon_cigars
)
val HumidorCigarsRoute = NavRoute(
    "HumidorCigarScreen",
    Localize.title_cigars,
    Images.tab_icon_cigars,
    isSearchEnabled = true
)