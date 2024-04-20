/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/19/24, 6:00 PM
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

package com.akellolcc.cigars.navigation

import com.akellolcc.cigars.mvvm.MainScreenViewModel
import com.akellolcc.cigars.theme.Images
import dev.icerock.moko.resources.ImageResource
import kotlinx.serialization.Transient
import java.io.Serializable

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

@kotlinx.serialization.Serializable
actual class NavRoute : Serializable {
    actual constructor(
        route: String,
        title: String,
        icon: ImageResource,
        data: Any?,
        isTabsVisible: Boolean,
        isDrawerVisible: Boolean,
        isLoadingCover: Boolean
    ) {
        this.route = route
        this.title = title
        this.icon = icon
        this.data = data
        this.isTabsVisible = isTabsVisible
        this.isDrawerVisible = isDrawerVisible
        this.isLoadingCover = isLoadingCover
    }

    actual val route: String
    actual val title: String

    @kotlin.jvm.Transient
    @Transient
    actual var icon: ImageResource = Images.tab_icon_cigars

    @kotlin.jvm.Transient
    @Transient
    actual var data: Any? = null
    actual var isTabsVisible: Boolean = true
    actual var isDrawerVisible: Boolean = false

    @kotlin.jvm.Transient
    @Transient
    actual var sharedViewModel: MainScreenViewModel? = null
    actual var isLoadingCover: Boolean = true
}