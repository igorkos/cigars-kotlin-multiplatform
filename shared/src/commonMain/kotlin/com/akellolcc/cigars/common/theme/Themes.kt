package com.akellolcc.cigars.common.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.akellolcc.cigars.common.logging.Log

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


@OptIn(ExperimentalStdlibApi::class)
@Composable
fun DefaultTheme(
    content: @Composable () -> Unit,
) {
    // Color scheme
    val scheme = createColorsTheme()

    // Background theme
    val backgroundTheme = backgroundTheme(Backgrounds.Default)
    val tintTheme = TintTheme(scheme.surfaceTint)
    Log.debug("DefaultTheme: " + scheme.surfaceTint.toArgb().toHexString())
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
