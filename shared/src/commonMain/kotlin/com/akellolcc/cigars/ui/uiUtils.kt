/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/19/24, 12:10 PM
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

package com.akellolcc.cigars.ui

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
import kotlinx.datetime.Instant
import kotlin.math.roundToInt
import kotlin.random.Random

@Composable
expect fun screenWidth(): Dp

@Composable
expect fun screenHeight(): Dp

@Composable
fun Dp.dpToPx() = with(LocalDensity.current) { this@dpToPx.toPx().roundToInt() }


@Composable
fun Int.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }


@Composable
expect fun BackHandler(block: () -> Unit)

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

fun Float.toIntPx(density: Float) = (this * density).roundToInt()
expect fun toImageBitmap(data: ByteArray): ImageBitmap?

val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
fun randomString() = (1..4)
    .map { Random.nextInt(0, charPool.size).let { charPool[it] } }
    .joinToString("")

expect fun Instant.formatDate(pattern: String, defValue: String = ""): String

expect fun String.parseDate(pattern: String, defValue: Long = 0L): Long

const val defaultDateFormat = "MMMM d, yyyy"
fun Long.formatDate(pattern: String? = null, defValue: String = ""): String {
    return Instant.fromEpochMilliseconds(this).formatDate(pattern ?: defaultDateFormat, defValue)
}

fun LazyListState.reachedBottom(buffer: Int = 1): Boolean {
    val lastVisibleItem = this.layoutInfo.visibleItemsInfo.lastOrNull()
    return lastVisibleItem?.index != 0 && lastVisibleItem?.index == this.layoutInfo.totalItemsCount - buffer
}