/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/22/24, 9:33 PM
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

package com.akellolcc.cigars.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class ColorResource ( val light: String, val dark: String)

fun String.parseHexColor(): Int {
    if (!startsWith("#")) {
        throw IllegalArgumentException("Color string must start with #")
    }
    val hex = removePrefix("#")
    if (hex.length !in listOf(6, 8)) {
        throw IllegalArgumentException("Color string must be in #RRGGBB or #AARRGGBB format")
    }

    return try {
        // Parse the hex string to a Long, then cast to Int
        // Shift bits for AARRGGBB format if needed, the below assumes RRGGBB as default
        val colorLong = hex.toLong(16)
        if (hex.length == 6) {
            // If only RRGGBB provided, add full Alpha (FF)
            (0xFF shl 24) or colorLong.toInt()
        } else {
            colorLong.toInt()
        }
    } catch (e: NumberFormatException) {
        throw IllegalArgumentException("Invalid hex color format", e)
    }
}
val LocalCurrentColorTheme = staticCompositionLocalOf { lightColorScheme() }

@Composable
fun createColorsTheme(): ColorScheme {
    val theme = lightColorScheme(
        primary = materialColor(color = MaterialColors.color_primary),
        onPrimary = materialColor(color = MaterialColors.color_onPrimary),
        primaryContainer = materialColor(color = MaterialColors.color_primaryContainer),
        onPrimaryContainer = materialColor(color = MaterialColors.color_onPrimaryContainer),
        inversePrimary = materialColor(color = MaterialColors.color_inversePrimary),
        secondary = materialColor(color = MaterialColors.color_secondary),
        onSecondary = materialColor(color = MaterialColors.color_onSecondary),
        secondaryContainer = materialColor(color = MaterialColors.color_secondaryContainer),
        onSecondaryContainer = materialColor(color = MaterialColors.color_onSecondaryContainer),
        tertiary = materialColor(color = MaterialColors.color_tertiary),
        onTertiary = materialColor(color = MaterialColors.color_onTertiary),
        tertiaryContainer = materialColor(color = MaterialColors.color_tertiaryContainer),
        onTertiaryContainer = materialColor(color = MaterialColors.color_onTertiaryContainer),
        background = materialColor(color = MaterialColors.color_background),
        onBackground = materialColor(color = MaterialColors.color_onBackground),
        surface = materialColor(color = MaterialColors.color_surface),
        onSurface = materialColor(color = MaterialColors.color_onSurface),
        surfaceVariant = materialColor(color = MaterialColors.color_surfaceVariant),
        onSurfaceVariant = materialColor(color = MaterialColors.color_onSurfaceVariant),
        surfaceTint = materialColor(color = MaterialColors.color_surfaceTint),
        inverseSurface = materialColor(color = MaterialColors.color_inverseSurface),
        inverseOnSurface = materialColor(color = MaterialColors.color_inverseOnSurface),
        error = materialColor(color = MaterialColors.color_error),
        onError = materialColor(color = MaterialColors.color_onError),
        errorContainer = materialColor(color = MaterialColors.color_errorContainer),
        onErrorContainer = materialColor(color = MaterialColors.color_onErrorContainer),
        outline = materialColor(color = MaterialColors.color_outline),
        outlineVariant = materialColor(color = MaterialColors.color_outlineVariant),
        scrim = materialColor(color = MaterialColors.color_scrim),
    )
    LocalCurrentColorTheme provides theme
    return theme
}

@Composable
fun materialColor(color: ColorResource, alpha: Float = 1.0f, light: Boolean = true): Color {
    val theme = if (light) color.light else color.dark
    val value = theme.parseHexColor()
    val resColor = Color(value)
    return if (alpha != 1.0f) Color(
        resColor.red,
        resColor.green,
        resColor.blue,
        alpha
    ) else resColor
}

class MaterialColors {
    companion object {
        val color_primary = ColorResource("#775A0B", "#E9C16C")

        val color_onPrimary = ColorResource("#FFFFFF", "#3F2E00")

        val color_primaryContainer = ColorResource("#FFDF9C", "#5B4300")

        val color_onPrimaryContainer = ColorResource("#251A00", "#FFDF9E")

        val color_secondary = ColorResource("#6B5D3F", "#D8C4A0")

        val color_onSecondary = ColorResource("#FFFFFF", "#3A2F15")

        val color_secondaryContainer = ColorResource("#F5E0BB", "#52452A")

        val color_onSecondaryContainer = ColorResource("#241A04", "#F5E0BB")

        val color_tertiary = ColorResource("#4A6547", "#B0CFAA")

        val color_onTertiary = ColorResource("#FFFFFF", "#1D361C")

        val color_tertiaryContainer = ColorResource("#CCEBC5", "#334D31")

        val color_onTertiaryContainer = ColorResource("#072109", "#CCEBC4")

        val color_error = ColorResource("#BA1A1A", "#FFB4AB")

        val color_errorContainer = ColorResource("#FFDAD6", "#93000A")

        val color_onError = ColorResource("#FFFFFF", "#690005")

        val color_onErrorContainer = ColorResource("#410002", "#FFDAD6")

        val color_background = ColorResource("#FFF8F2", "#17130B")

        val color_onBackground = ColorResource("#1F1B13", "#EBE1D4")

        val color_outline = ColorResource("#7F7667", "#998F80")

        val color_inverseOnSurface = ColorResource("#F9EFE2", "#353027")

        val color_inverseSurface = ColorResource("#353027", "#EBE1D4")

        val color_inversePrimary = ColorResource("#E9C16C", "#775A0B")

        val color_shadow = ColorResource("#000000", "#000000")

        val color_surfaceTint = ColorResource("#775A0B", "#E9C16C")

        val color_outlineVariant = ColorResource("#D0C5B4", "#4D4639")

        val color_scrim = ColorResource("#000000", "#000000")

        val color_surface = ColorResource("#1e1b16", "#1e1b16")

        val color_onSurface = ColorResource("#1e1b16", "#e9e1d8")

        val color_surfaceVariant = ColorResource("#ede1cf", "#4d4639")

        val color_onSurfaceVariant = ColorResource("#4D4639", "#D0C5B4")

        val color_transparent = ColorResource("#00000000", "#00000000")
    }
}


