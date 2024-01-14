package com.akellolcc.cigars.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.ButtonDefaults.buttonElevation
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle


@Immutable
data class ButtonStyle(
    val colors: ButtonColors,
    val shape: Shape,
    val elevation: ButtonElevation,
    val textStyle: TextStyle? = null,
    val rippleColor: Color? = null
)

@Composable
fun defaultButtonStyle(): ButtonStyle {
    val themeColors = MaterialTheme.colorScheme
    return ButtonStyle(
        colors = buttonColors(
            containerColor = themeColors.onErrorContainer,
            contentColor = themeColors.onPrimary,
            disabledContainerColor = themeColors.inversePrimary,
            disabledContentColor = themeColors.onPrimary
        ),
        shape = ButtonDefaults.shape,
        elevation = buttonElevation(),
    )
}
@Composable
fun DefaultButton(onClick: () -> Unit,
                  modifier: Modifier = Modifier,
                  enabled: Boolean = true,
                  content: @Composable RowScope.() -> Unit) {
    val style = defaultButtonStyle()
    ElevatedButton(
        enabled = enabled,
        shape = style.shape,
        colors = style.colors,
        elevation = style.elevation,
        modifier = modifier,
        onClick = onClick,
        content = content)
}
