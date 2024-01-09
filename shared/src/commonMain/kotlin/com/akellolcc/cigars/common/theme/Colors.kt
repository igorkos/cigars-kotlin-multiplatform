package com.akellolcc.cigars.common.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import dev.icerock.moko.resources.compose.colorResource
import dev.icerock.moko.resources.ColorResource
import com.akellolcc.cigars.common.MR

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
fun materialColor(color: ColorResource): Color {
    return colorResource(color)
}

class MaterialColors {
    companion object {
        val color_primary = MR.colors.color_primary

        val color_onPrimary = MR.colors.color_onPrimary

        val color_primaryContainer = MR.colors.color_primaryContainer

        val color_onPrimaryContainer = MR.colors.color_onPrimaryContainer

        val color_secondary = MR.colors.color_secondary

        val color_onSecondary = MR.colors.color_onSecondary

        val color_secondaryContainer = MR.colors.color_secondaryContainer

        val color_onSecondaryContainer = MR.colors.color_onSecondaryContainer

        val color_tertiary = MR.colors.color_tertiary

        val color_onTertiary = MR.colors.color_onTertiary

        val color_tertiaryContainer = MR.colors.color_tertiaryContainer

        val color_onTertiaryContainer = MR.colors.color_onTertiaryContainer

        val color_error = MR.colors.color_error

        val color_errorContainer = MR.colors.color_errorContainer

        val color_onError = MR.colors.color_onError

        val color_onErrorContainer = MR.colors.color_onErrorContainer

        val color_background = MR.colors.color_background

        val color_onBackground = MR.colors.color_onBackground

        val color_outline = MR.colors.color_outline

        val color_inverseOnSurface = MR.colors.color_inverseOnSurface

        val color_inverseSurface = MR.colors.color_inverseSurface

        val color_inversePrimary = MR.colors.color_inversePrimary

        val color_shadow = MR.colors.color_shadow

        val color_surfaceTint = MR.colors.color_surfaceTint

        val color_outlineVariant = MR.colors.color_outlineVariant

        val color_scrim = MR.colors.color_scrim

        val color_surface = MR.colors.color_surface

        val color_onSurface = MR.colors.color_onSurface

        val color_surfaceVariant = MR.colors.color_surfaceVariant

        val color_onSurfaceVariant = MR.colors.color_onSurfaceVariant
    }
}


