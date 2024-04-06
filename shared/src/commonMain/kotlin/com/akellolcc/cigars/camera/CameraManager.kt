package com.akellolcc.cigars.camera

import androidx.compose.runtime.Composable
import com.preat.peekaboo.image.picker.FilterOptions
import com.preat.peekaboo.image.picker.ResizeOptions

@Composable
expect fun rememberCameraManager(
    resizeOptions: ResizeOptions = ResizeOptions(),
    filterOptions: FilterOptions = FilterOptions.Default,
    onResult: (SharedImage?) -> Unit
): CameraManager


@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class CameraManager(
    onLaunch: () -> Unit
) {
    fun launch()
}