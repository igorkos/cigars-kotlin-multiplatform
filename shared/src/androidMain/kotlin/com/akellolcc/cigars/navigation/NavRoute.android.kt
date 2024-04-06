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
        isTabsVisible: Boolean
    ) {
        this.route = route
        this.title = title
        this.icon = icon
        this.data = data
        this.isTabsVisible = isTabsVisible
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

    @kotlin.jvm.Transient
    @Transient
    actual var sharedViewModel: MainScreenViewModel? = null
}