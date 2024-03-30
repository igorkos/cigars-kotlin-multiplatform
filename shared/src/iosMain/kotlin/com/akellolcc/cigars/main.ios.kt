package com.akellolcc.cigars

import androidx.compose.ui.window.ComposeUIViewController
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.navigator.Navigator
import com.akellolcc.cigars.navigation.mainScreenModule
import com.akellolcc.cigars.screens.Home


fun MainViewController() = ComposeUIViewController {
    ScreenRegistry {
        mainScreenModule()
    }

    Navigator(Home())
}