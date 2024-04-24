/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/23/24, 11:56 PM
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

package com.akellolcc.cigars.screens.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.akellolcc.cigars.databases.extensions.CigarImage
import com.akellolcc.cigars.theme.Images
import com.akellolcc.cigars.theme.MaterialColors
import com.akellolcc.cigars.theme.materialColor
import com.akellolcc.cigars.utils.ui.toImageBitmap
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.compose.painterResource

// Shadow and shape values for the card
private val shadowElevation = 15.dp
private val borderRadius = 15.dp
private val shape = RoundedCornerShape(topStart = borderRadius, topEnd = borderRadius)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagedCarousel(
    images: List<CigarImage>?,
    modifier: Modifier = Modifier,
    scale: ContentScale = ContentScale.FillHeight,
    loading: Boolean = false,
    select: Int = 0,
    onClick: ((page: Int) -> Unit)? = null
) {
    val pagerState = rememberPagerState(select) {
        if (!images.isNullOrEmpty()) images.size else 1
    }

    LaunchedEffect(loading, select) {
        if (!loading) {
            pagerState.scrollToPage(select)
        }
    }

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        // HorizontalPager composable: Swiping through images
        if (loading) {
            CircularProgressIndicator()
        } else {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
            ) { page ->
                //Log.debug("Images1: ${images?.size}")
                if (!images.isNullOrEmpty()) {
                    CarouselItem(images[page].bytes, scale) {
                        onClick?.invoke(page)
                    }
                } else {
                    CarouselItem(null, scale, Images.default_cigar_image) {
                        onClick?.invoke(page)
                    }
                }
            }
            // Bottom row of indicators
            if (!images.isNullOrEmpty()) {
                Row(
                    Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 4.dp),
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
}

@Composable
fun CarouselItem(
    image: ByteArray?,
    scale: ContentScale,
    default: ImageResource? = null,
    onClick: () -> Unit
) {
    // Load the image bitmap
    val imageBitmap = image?.let {
        toImageBitmap(it)
    }

    if (imageBitmap == null && default == null) return
    Column(
        modifier = Modifier.fillMaxSize().clickable {
            onClick()
        },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val modifier = Modifier.fillMaxSize()
        if (imageBitmap != null) {
            Image(
                modifier = modifier,
                bitmap = imageBitmap,
                contentDescription = "",
                contentScale = scale
            )
        } else {
            val painter: Painter = painterResource(default!!)
            Image(
                modifier = modifier,
                painter = painter,
                contentDescription = "",
                contentScale = scale
            )
        }
    }
}
