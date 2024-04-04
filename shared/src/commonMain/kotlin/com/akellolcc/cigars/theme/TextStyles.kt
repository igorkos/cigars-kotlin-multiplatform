package com.akellolcc.cigars.theme

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

enum class TextStyles {
    ScreenTitle,
    BarItemTitle,
    Headline,
    Subhead,
    Description
}

@Composable
fun textStyle(style: TextStyles, color: Color = Color(LocalContentColor.current.value)): TextStyle {
    return when(style) {
        TextStyles.Headline -> MaterialTheme.typography.titleLarge.copy(color = color)
        TextStyles.Subhead ->  MaterialTheme.typography.titleMedium.copy(color = color)
        TextStyles.Description -> MaterialTheme.typography.titleSmall.copy(color = color)
        TextStyles.ScreenTitle -> MaterialTheme.typography.titleLarge.copy(color = color)
        TextStyles.BarItemTitle -> MaterialTheme.typography.labelMedium.copy(color = color)
    }
}
