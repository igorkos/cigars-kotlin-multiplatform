package com.akellolcc.cigars.common.ui

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
actual fun screenWidth(): Dp {
    return LocalConfiguration.current.screenWidthDp.dp
}

@Composable
actual fun BackHandler(block: () -> Unit) {
    BackHandler{
        // your action to be called if back handler is enabled
        block()
    }
}
