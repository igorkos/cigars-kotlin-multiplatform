package com.akellolcc.cigars.components

import TextStyled
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.ButtonDefaults.buttonElevation
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.akellolcc.cigars.theme.MaterialColors
import com.akellolcc.cigars.theme.TextStyles
import com.akellolcc.cigars.theme.materialColor


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
fun DefaultButton(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val style = defaultButtonStyle()
    ElevatedButton(
        enabled = enabled,
        modifier = modifier.width(150.dp),
        border = BorderStroke(1.dp, materialColor(MaterialColors.color_onPrimaryContainer)),
        onClick = onClick
    ) {
        TextStyled(
            title,
            TextStyles.Headline,
        )
    }
}


@Composable
fun DialogButton(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {

    TextButton(
        enabled = enabled,
        modifier = modifier,
        onClick = onClick
    ) {
        TextStyled(
            title,
            TextStyles.Headline,
        )
    }
}
