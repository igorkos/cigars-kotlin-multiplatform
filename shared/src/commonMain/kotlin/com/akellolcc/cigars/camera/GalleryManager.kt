package com.akellolcc.cigars.camera

import androidx.compose.runtime.Composable

@Composable
expect fun rememberGalleryManager(onResult: (SharedImage?) -> Unit): GalleryManager


@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class GalleryManager(
    onLaunch: () -> Unit
) {
    fun launch()
}