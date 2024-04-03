package com.akellolcc.cigars.navigation

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import dev.icerock.moko.resources.ImageResource
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
@Serializable
actual class NavRoute {
    actual constructor(
        route: String,
        title: String,
        icon: ImageResource?,
        data: Any?
    ) {
        this.route = route
        this.title = title
        this.icon = icon
        this.data = data
    }

    actual val route: String
    actual val title: String
    @Transient
    actual var icon: ImageResource? = null
    @Transient
    actual var data: Any? = null
    @Transient
    actual var updateTabState: ((Boolean) -> Unit)? = null
    @Transient
    actual var sharedViewModel: ViewModel? = null

}