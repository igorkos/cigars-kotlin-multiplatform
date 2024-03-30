package com.akellolcc.cigars.ui

import android.graphics.BitmapFactory
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
actual fun screenWidth(): Dp {
    return LocalConfiguration.current.screenWidthDp.dp
}

@Composable
actual fun BackHandler(block: () -> Unit) {
    androidx.activity.compose.BackHandler {
        block()
    }
}

actual fun toImageBitmap(data: ByteArray): ImageBitmap? {
    return  BitmapFactory.decodeByteArray(data, 0, data.size).asImageBitmap()
}