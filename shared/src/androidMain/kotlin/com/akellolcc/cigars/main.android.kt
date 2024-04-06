package com.akellolcc.cigars

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.navigator.Navigator
import com.akellolcc.cigars.navigation.mainScreenModule
import com.akellolcc.cigars.screens.Home


@Composable
fun MainView() {
    ScreenRegistry {
        mainScreenModule()
    }
    Navigator(Home())
}
