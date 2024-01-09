package com.akellolcc.cigars.common.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.registry.ScreenProvider
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.registry.screenModule
import com.akellolcc.cigars.common.screens.LoginScreen
import com.akellolcc.cigars.common.screens.MainScreen


sealed class SharedScreen : ScreenProvider {
    object LoginScreen : SharedScreen()
    object MainScreen : SharedScreen()
}

val mainScreenModule = screenModule {
    register<SharedScreen.LoginScreen> {
        LoginScreen()
    }
    register<SharedScreen.MainScreen> {
        MainScreen()
    }

}

fun SetupNavGraph() {
    ScreenRegistry {
        register<SharedScreen.LoginScreen> { provider ->
            LoginScreen()
        }
        register<SharedScreen.MainScreen> { provider ->
            MainScreen()
        }
    }
}

@Composable
fun SetupHomeNavGraph(route: (Route) -> Unit) {

}
