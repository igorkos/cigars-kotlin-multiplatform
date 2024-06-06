/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/21/24, 1:08 PM
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
import org.jetbrains.skia.Image

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

