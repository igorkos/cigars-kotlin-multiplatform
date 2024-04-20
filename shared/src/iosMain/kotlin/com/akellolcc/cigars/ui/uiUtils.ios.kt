package com.akellolcc.cigars.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import kotlinx.datetime.Instant
import kotlinx.datetime.toNSDate
import org.jetbrains.skia.Image
import platform.Foundation.NSDateFormatter
import platform.Foundation.timeIntervalSince1970

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun screenWidth(): Dp {
    return Dp(LocalWindowInfo.current.containerSize.width.toFloat())
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun screenHeight(): Dp {
    return Dp(LocalWindowInfo.current.containerSize.height.toFloat())
}

@Composable
actual fun BackHandler(block: () -> Unit) {
}

actual fun toImageBitmap(data: ByteArray): ImageBitmap? {
    return Image.makeFromEncoded(data).toComposeImageBitmap()
}

actual fun Instant.formatDate(pattern: String, defValue: String): String {
    return try {
        val dateFormatter = NSDateFormatter()
        dateFormatter.dateFormat = pattern
        dateFormatter.stringFromDate(
            toNSDate()
        )
    } catch (e: Exception) {
        defValue
    }

}

actual fun String.parseDate(pattern: String, defValue: Long): Long {
    return try {
        val dateFormatter = NSDateFormatter()
        dateFormatter.dateFormat = pattern
        val result = dateFormatter.dateFromString(this)?.timeIntervalSince1970?.toLong()
        if (result != null) {
            result * 1000
        } else {
            defValue
        }
    } catch (e: Exception) {
        defValue
    }
}

