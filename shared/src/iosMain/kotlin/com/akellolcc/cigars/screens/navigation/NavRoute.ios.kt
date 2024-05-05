/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/30/24, 12:20 PM
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

import dev.icerock.moko.resources.ImageResource

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class NavRoute {
    actual constructor(
        route: String,
        title: String,
        icon: ImageResource,
        data: Any?,
        isTabsVisible: Boolean,
        isDrawerVisible: Boolean,
        isLoadingCover: Boolean,
        isSearchEnabled: Boolean
    ) {
        this.route = route
        this.title = title
        this.icon = icon
        this.data = data
        this.isTabsVisible = isTabsVisible
        this.isDrawerVisible = isDrawerVisible
        this.isLoadingCover = isLoadingCover
        this.isSearchEnabled = isSearchEnabled
    }

    actual val route: String
    actual val title: String
    actual val icon: ImageResource
    actual var data: Any?
    actual val isTabsVisible: Boolean
    actual val isDrawerVisible: Boolean
    actual val isLoadingCover: Boolean
    actual val isSearchEnabled: Boolean

}