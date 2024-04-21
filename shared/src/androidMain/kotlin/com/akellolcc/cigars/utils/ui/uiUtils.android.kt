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
actual fun screenHeight(): Dp {
    return LocalConfiguration.current.screenHeightDp.dp
}

@Composable
actual fun BackHandler(block: () -> Unit) {
    androidx.activity.compose.BackHandler {
        block()
    }
}

actual fun toImageBitmap(data: ByteArray): ImageBitmap? {
    return BitmapFactory.decodeByteArray(data, 0, data.size).asImageBitmap()
}

