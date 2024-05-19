/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/17/24, 6:49 PM
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
 ******************************************************************************************************************************************/

package com.akellolcc.cigars.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.akellolcc.cigars.databases.Database
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.screens.navigation.SharedScreen
import com.akellolcc.cigars.screens.navigation.setupNavGraph
import com.akellolcc.cigars.theme.MaterialColors
import com.akellolcc.cigars.theme.materialColor
import com.akellolcc.cigars.utils.Pref
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlin.jvm.Transient

class Home : Screen {
    @kotlinx.serialization.Transient
    @Transient
    val database = Database.instance

    init {
        setupNavGraph()
    }

    @Composable
    override fun Content() {
        val initialized = remember { mutableStateOf(!Pref.isFirstStart) }
        val navigator = LocalNavigator.currentOrThrow

        LaunchedEffect(Unit) {
            if (Pref.isFirstStart) {
                database.reset()
                CoroutineScope(Dispatchers.IO).launch {
                    database.createDemoSet().collect {
                        Log.debug("Created demo set")
                        Pref.isFirstStart = false
                        initialized.value = true
                    }
                }
            }
        }

        if (!initialized.value) {
            Box(
                modifier = Modifier.fillMaxSize().background(
                    materialColor(
                        MaterialColors.color_transparent
                    )
                ), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.width(64.dp),
                )
            }
        } else {
            val postMainScreen = rememberScreen(SharedScreen.MainScreen)
            navigator.push(postMainScreen)
        }
    }
}
