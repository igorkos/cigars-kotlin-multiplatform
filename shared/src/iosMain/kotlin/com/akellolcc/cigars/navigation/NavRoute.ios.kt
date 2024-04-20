package com.akellolcc.cigars.navigation

import com.akellolcc.cigars.mvvm.MainScreenViewModel
import com.akellolcc.cigars.theme.Images
import dev.icerock.moko.resources.ImageResource
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
@Serializable
actual class NavRoute {
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
        this.isLoadingCover = isLoadingCover
    }

    actual val route: String
    actual val title: String

    @Transient
    actual var icon: ImageResource = Images.tab_icon_cigars

    @Transient
    actual var data: Any? = null
    actual var isTabsVisible: Boolean = true

    @Transient
    actual var sharedViewModel: MainScreenViewModel? = null
    actual var isDrawerVisible: Boolean = false
    actual var isLoadingCover: Boolean = true

}