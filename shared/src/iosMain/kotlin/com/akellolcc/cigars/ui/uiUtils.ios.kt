package com.akellolcc.cigars.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import org.jetbrains.skia.Image

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun screenWidth(): Dp {
    return Dp(LocalWindowInfo.current.containerSize.width.toFloat())
}

@Composable
actual fun BackHandler(block: () -> Unit) {
}

actual fun toImageBitmap(data: ByteArray): ImageBitmap? {
    return Image.makeFromEncoded(data).toComposeImageBitmap()
}