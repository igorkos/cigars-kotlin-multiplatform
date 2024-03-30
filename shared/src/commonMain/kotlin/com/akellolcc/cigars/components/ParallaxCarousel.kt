package com.akellolcc.cigars.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.akellolcc.cigars.databases.extensions.CigarImage
import com.akellolcc.cigars.theme.Images
import com.akellolcc.cigars.theme.MaterialColors
import com.akellolcc.cigars.theme.materialColor
import com.akellolcc.cigars.ui.toImageBitmap
import com.akellolcc.cigars.ui.toIntPx
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.compose.painterResource

// Shadow and shape values for the card
private val shadowElevation = 15.dp
private val borderRadius = 15.dp
private val shape = RoundedCornerShape(topStart = borderRadius, topEnd = borderRadius)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagedCarousel(images: List<CigarImage>?, modifier: Modifier = Modifier) {
    // Create a pager state
    val pagerState = rememberPagerState {
        if(!images.isNullOrEmpty()) images.size else 1
    }

    Box {
        // HorizontalPager composable: Swiping through images
        HorizontalPager(
            state = pagerState,
            modifier = modifier.fillMaxWidth()
        ) { page ->
            if(!images.isNullOrEmpty()) {
                CarouselItem(images[page].bytes)
            } else {
                CarouselItem(null, Images.default_cigar_image)
            }
        }
        // Bottom row of indicators
        if(!images.isNullOrEmpty()) {
            Row(
                Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 4.dp), //.background(materialColor(MaterialColors.color_error))
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(pagerState.pageCount) { iteration ->
                    val color =
                        if (pagerState.currentPage == iteration) materialColor(MaterialColors.color_onPrimaryContainer) else materialColor(
                            MaterialColors.color_onPrimaryContainer
                        ).copy(
                            alpha = 0.5f
                        )
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(12.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CarouselItem(
    image: ByteArray?,
    default: ImageResource? = null
) {
    var drawSize by remember { mutableStateOf(IntSize(0, 0)) }
    // Load the image bitmap
    val imageBitmap = image?.let {
        toImageBitmap(it)
    }

    if(imageBitmap == null && default == null) return
    // Card composable for the item
    Card(
        modifier = Modifier
            .fillMaxSize()
            .shadow(elevation = shadowElevation, shape = shape)
            .onSizeChanged {
                drawSize = it
            }
    ) {
        if(imageBitmap != null) {
            // Canvas for drawing the image with parallax effect
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape)
            ) {
                // Draw the image
                drawImage(
                    image = imageBitmap,
                    srcSize = IntSize(imageBitmap.width, imageBitmap.height),
                    dstOffset = IntOffset(0, 0),
                    dstSize = drawSize,
                )
            }
        } else {
            val painter: Painter = painterResource(default!!)
            Image(painter = painter, contentDescription = "", contentScale = ContentScale.FillBounds)
        }
    }
}

// Function to calculate draw size for the image
private fun ImageBitmap.calculateDrawSize(density: Float, screenWidth: Dp, pagerHeight: Dp): IntSize {
    val originalImageWidth = width / density
    val originalImageHeight = height / density

    val frameAspectRatio = screenWidth / pagerHeight
    val imageAspectRatio = originalImageWidth / originalImageHeight

    val drawWidth = if (frameAspectRatio > imageAspectRatio) {
        screenWidth.value
    } else {
        pagerHeight.value * imageAspectRatio
    }

    val drawHeight = if (frameAspectRatio > imageAspectRatio) {
        screenWidth.value / imageAspectRatio
    } else {
        pagerHeight.value
    }

    return IntSize(drawWidth.toIntPx(density), drawHeight.toIntPx(density))
}
