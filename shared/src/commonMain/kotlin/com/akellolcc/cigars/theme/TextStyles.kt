package com.akellolcc.cigars.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

enum class TextStyles {
    ScreenTitle,
    BarItemTitle,
    Headline,
    Subhead,
    Description
}
data class TextStyle(
    val color: Color,
    val fontSize: TextUnit = TextUnit.Unspecified,
    val fontStyle: FontStyle? = null,
    val fontFamily: FontFamily? = null,
    val textAlign: TextAlign? = null)

@Composable
fun textStyle(style: TextStyles): TextStyle  {
    return when(style) {
        TextStyles.Headline -> TextStyle(materialColor(MaterialColors.color_primary), 22.sp, FontStyle.Normal)
        TextStyles.Subhead -> TextStyle(materialColor(MaterialColors.color_primary), 18.sp, FontStyle.Normal)
        TextStyles.Description -> TextStyle(materialColor(MaterialColors.color_primary), 16.sp, FontStyle.Normal)
        TextStyles.ScreenTitle -> TextStyle(materialColor(MaterialColors.color_onSurface), 22.sp, FontStyle.Normal)
        TextStyles.BarItemTitle -> TextStyle(materialColor(MaterialColors.color_primary), 12.sp, FontStyle.Normal)
    }
}
