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
fun textStyle(style: TextStyles): TextStyle {
    return when(style) {
        TextStyles.Headline -> MaterialTheme.typography.titleLarge.copy(color = Color(LocalContentColor.current.value))
        TextStyles.Subhead ->  MaterialTheme.typography.titleMedium.copy(color = Color(LocalContentColor.current.value))
        TextStyles.Description -> MaterialTheme.typography.titleSmall.copy(color = Color(LocalContentColor.current.value))
        TextStyles.ScreenTitle -> MaterialTheme.typography.titleLarge.copy(color = Color(LocalContentColor.current.value))
        TextStyles.BarItemTitle -> MaterialTheme.typography.labelMedium.copy(color = Color(LocalContentColor.current.value))
    }
}
