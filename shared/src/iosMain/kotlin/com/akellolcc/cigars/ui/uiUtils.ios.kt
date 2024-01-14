package com.akellolcc.cigars.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun screenWidth(): Dp {
    return Dp(LocalWindowInfo.current.containerSize.width.toFloat())
}

@Composable
actual fun BackHandler(block: () -> Unit) {
}