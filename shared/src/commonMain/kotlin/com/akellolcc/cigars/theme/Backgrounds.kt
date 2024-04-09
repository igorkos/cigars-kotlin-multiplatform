/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.akellolcc.cigars.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp

/**
 * A class to model background color and tonal elevation values for Now in Android.
 */
@Immutable
data class BackgroundTheme(
    val color: Color = Color.Unspecified,
    val tonalElevation: Dp = Dp.Unspecified,
)

enum class Backgrounds {
    Default
}

@Composable
fun createBackgroundTheme(background: Backgrounds): BackgroundTheme {
    return when (background) {
        Backgrounds.Default -> {
            BackgroundTheme(materialColor(MaterialColors.color_primaryContainer))
        }
    }
}

//val DrawerLightBackgroundTheme = BackgroundTheme(color = Color(R.color.md_theme_light_primaryContainer))
//val DrawerDarkBackgroundTheme = BackgroundTheme(color = Color(R.color.md_theme_dark_surface))

//val PreviewLightBackgroundTheme = BackgroundTheme(color = Color(R.color.md_theme_light_primaryContainer))
//val PreviewDarkBackgroundTheme = BackgroundTheme(color = Color(R.color.md_theme_dark_surface))

val LocalBackgroundTheme = staticCompositionLocalOf { BackgroundTheme() }

@Composable
fun backgroundTheme(background: Backgrounds): BackgroundTheme {
    return createBackgroundTheme(background)
}

/**
 * The main background for the app.
 * Uses [LocalBackgroundTheme] to set the color and tonal elevation of a [Surface].
 *
 * @param modifier Modifier to be applied to the background.
 * @param content The background content.
 */
@OptIn(ExperimentalStdlibApi::class)
@Composable
fun DefaultBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = with(Modifier) {
            fillMaxSize()
            //  .paint(
            //      imagePainter(Images.default_background),
            //      contentScale = ContentScale.FillBounds
            //  )

        }
    ) {
        content()
    }
}

/**
 * A gradient background for select screens. Uses [LocalBackgroundTheme] to set the gradient colors
 * of a [Box] within a [Surface].
 *
 * @param modifier Modifier to be applied to the background.
 * @param gradientColors The gradient colors to be rendered.
 * @param content The background content.
 */

@Composable
fun LoginBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = with(Modifier) {
            fillMaxSize()
          //      .paint(
         //           imagePainter(Images.splash_background),
         //           contentScale = ContentScale.FillBounds
          //      )

        }
    ) {
        Box(
            modifier = with(Modifier) {
                fillMaxSize(0.7f)
                 //   .paint(
                //        imagePainter(Images.splash_plate),
                 //       contentScale = ContentScale.FillWidth
                 //   )
                    .align(alignment = Alignment.Center)
            }
        ) {
            Text(text = Localize.app_name, fontSize = 32.sp, modifier = with(Modifier) {
                align(Alignment.Center)
            })
        }
        content()
    }
}

