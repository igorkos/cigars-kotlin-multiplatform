/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 3/30/24, 12:05 AM
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.akellolcc.cigars.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.registry.ScreenProvider
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.registry.screenModule
import com.akellolcc.cigars.screens.LoginScreen
import com.akellolcc.cigars.screens.MainScreen


sealed class SharedScreen : ScreenProvider {
    data object LoginScreen : SharedScreen()
    data object MainScreen : SharedScreen()
}

val mainScreenModule = screenModule {
    register<SharedScreen.LoginScreen> {
        LoginScreen()
    }
    register<SharedScreen.MainScreen> {
        MainScreen()
    }

}

fun setupNavGraph() {
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
