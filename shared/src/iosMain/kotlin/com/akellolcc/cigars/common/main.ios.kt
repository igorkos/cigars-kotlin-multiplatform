package com.akellolcc.cigars.common

import androidx.compose.ui.window.ComposeUIViewController
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.navigator.Navigator
import com.akellolcc.cigars.common.navigation.mainScreenModule
import com.akellolcc.cigars.common.screens.MainScreen


fun MainViewController() = ComposeUIViewController {
    ScreenRegistry {
        mainScreenModule()
    }

    Navigator(MainScreen())
}