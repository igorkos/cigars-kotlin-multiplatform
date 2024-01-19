package com.akellolcc.cigars.screens

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.akellolcc.cigars.navigation.SetupNavGraph
import com.akellolcc.cigars.navigation.SharedScreen

class Home : Screen {
    init {
        SetupNavGraph()
    }

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val postLoginScreen= rememberScreen(SharedScreen.LoginScreen)
        val postMainScreen= rememberScreen(SharedScreen.MainScreen)

        navigator.push(postMainScreen)
    }
}
