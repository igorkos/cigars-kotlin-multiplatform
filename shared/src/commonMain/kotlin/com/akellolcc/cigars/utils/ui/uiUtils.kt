/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/12/24, 8:04 PM
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

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.akellolcc.cigars.theme.imagePainter
import dev.icerock.moko.resources.ImageResource
import kotlin.math.roundToInt

@Composable
expect fun screenWidth(): Dp

@Composable
expect fun screenHeight(): Dp

@Composable
fun Dp.dpToPx() = with(LocalDensity.current) { this@dpToPx.toPx().roundToInt() }


@Composable
fun Int.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }


@Composable
expect fun BackHandler(block: @Composable () -> Boolean)

@Composable
fun rotateImage(image: ImageResource, size: Size) {
    val infiniteTransition = rememberInfiniteTransition()
    val angle by infiniteTransition.animateFloat(
        initialValue = 0F,
        targetValue = 360F,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing)
        )
    )
    Image(
        painter = imagePainter(image),
        "image",
        Modifier.size(size.width.dp, size.height.dp).rotate(angle),
        contentScale = ContentScale.Fit
    )
}


expect fun toImageBitmap(data: ByteArray): ImageBitmap?

fun LazyListState.reachedBottom(buffer: Int = 2): Boolean {
    val lastVisibleItem = this.layoutInfo.visibleItemsInfo.lastOrNull() ?: return false
    val reachedBottom =
        lastVisibleItem.index != 0 && lastVisibleItem.index >= this.layoutInfo.totalItemsCount - buffer
    return reachedBottom
}