package com.akellolcc.cigars.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.akellolcc.cigars.theme.imagePainter
import dev.icerock.moko.resources.ImageResource
import kotlin.math.roundToInt

@Composable
expect fun screenWidth(): Dp

@Composable
expect fun BackHandler(block:() -> Unit)

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

