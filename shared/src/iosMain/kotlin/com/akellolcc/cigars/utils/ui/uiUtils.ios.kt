/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/19/24, 11:51 PM
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

package com.akellolcc.cigars.utils.ui

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

