/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/12/24, 8:18 PM
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
 ******************************************************************************************************************************************/

package com.akellolcc.cigars.utils.ui

import android.content.Context
import android.content.ContextWrapper
import android.graphics.BitmapFactory
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
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

private fun Context.getActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}

@Composable
actual fun BackHandler(block: @Composable () -> Boolean) {
    val pressed = remember { mutableStateOf(false) }
    androidx.activity.compose.BackHandler {
        pressed.value = true
    }

    if (pressed.value) {
        if (!block()) {
            LocalContext.current.getActivity()?.finish()
        }
        pressed.value = false
    }
}

actual fun toImageBitmap(data: ByteArray): ImageBitmap? {
    return BitmapFactory.decodeByteArray(data, 0, data.size).asImageBitmap()
}

