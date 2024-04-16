package com.akellolcc.cigars.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.akellolcc.cigars.databases.Database
import com.akellolcc.cigars.navigation.SharedScreen
import com.akellolcc.cigars.navigation.setupNavGraph
import com.akellolcc.cigars.utils.Pref

class Home : Screen {
    val database = Database.instance

    init {
        setupNavGraph()
    }

    @Composable
    override fun Content() {
        val initialized = remember { mutableStateOf(false) }
        val navigator = LocalNavigator.currentOrThrow

        if (!initialized.value) {
            if (Pref.isFirstStart) {
                database.createDemoSet().subscribe {
                    Pref.isFirstStart = false
                    initialized.value = true
                }
                return
            }
        }
        val postLoginScreen = rememberScreen(SharedScreen.LoginScreen)
        val postMainScreen = rememberScreen(SharedScreen.MainScreen)
        navigator.push(postMainScreen)
    }
}
