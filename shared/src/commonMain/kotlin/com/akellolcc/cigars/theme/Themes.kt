/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/10/24, 10:04 PM
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

package com.akellolcc.cigars.common.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.akellolcc.cigars.theme.Backgrounds
import com.akellolcc.cigars.theme.DefaultBackground
import com.akellolcc.cigars.theme.LocalBackgroundTheme
import com.akellolcc.cigars.theme.LocalTintTheme
import com.akellolcc.cigars.theme.TintTheme
import com.akellolcc.cigars.theme.backgroundTheme
import com.akellolcc.cigars.theme.createColorsTheme

val typography = Typography(
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        //fontSize = 16.sp
    )
)
val shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(4.dp),
    large = RoundedCornerShape(0.dp)
)

@Composable
fun AnimatedSplashScreenTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = createColorsTheme()

    MaterialTheme(
        colorScheme = colors,
        typography = typography,
        shapes = shapes,
        content = content
    )
}


@Composable
fun DefaultTheme(
    content: @Composable () -> Unit,
) {
    // Color scheme
    val scheme = createColorsTheme()

    // Background theme
    val backgroundTheme = backgroundTheme(Backgrounds.Default)
    val tintTheme = TintTheme(scheme.surfaceTint)
    CompositionLocalProvider(
        LocalBackgroundTheme provides backgroundTheme,
        LocalTintTheme provides tintTheme,
    ) {
        MaterialTheme(
            colorScheme = scheme,
            typography = typography,
            content = {
                DefaultBackground {
                    content()
                }
            },
        )
    }
}
