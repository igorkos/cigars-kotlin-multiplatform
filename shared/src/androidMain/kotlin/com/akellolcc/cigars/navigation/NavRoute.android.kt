package com.akellolcc.cigars.navigation

import com.akellolcc.cigars.mvvm.MainScreenViewModel
import dev.icerock.moko.resources.ImageResource
import kotlinx.serialization.Transient
import java.io.Serializable

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

@kotlinx.serialization.Serializable
actual class NavRoute : Serializable {
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
    @kotlin.jvm.Transient
    @Transient
    actual var icon: ImageResource? = null
    @kotlin.jvm.Transient
    @Transient
    actual var data: Any? = null
    @kotlin.jvm.Transient
    @Transient
    actual var updateTabState: ((Boolean) -> Unit)? = null
    @kotlin.jvm.Transient
    @Transient
    actual var sharedViewModel: MainScreenViewModel? = null
}