package com.akellolcc.cigars.common

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.navigator.Navigator
import com.akellolcc.cigars.common.navigation.mainScreenModule
import com.akellolcc.cigars.common.screens.Home
import com.akellolcc.cigars.common.screens.LoginScreen
import com.akellolcc.cigars.common.screens.MainScreen

@Composable fun MainView() {
    ScreenRegistry {
        mainScreenModule()
    }

    Navigator(Home())
}
