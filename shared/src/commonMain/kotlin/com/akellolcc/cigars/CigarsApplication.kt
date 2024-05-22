/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/21/24, 11:18 PM
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

package com.akellolcc.cigars

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.navigator.Navigator
import com.akellolcc.cigars.databases.Database
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.mvvm.MainScreenViewModel
import com.akellolcc.cigars.mvvm.base.CigarImagesViewScreenViewModel
import com.akellolcc.cigars.mvvm.base.HumidorImagesViewScreenViewModel
import com.akellolcc.cigars.mvvm.base.ViewModelRegistry
import com.akellolcc.cigars.mvvm.cigars.CigarHistoryScreenViewModel
import com.akellolcc.cigars.mvvm.cigars.CigarsDetailsScreenViewModel
import com.akellolcc.cigars.mvvm.cigars.CigarsScreenViewModel
import com.akellolcc.cigars.mvvm.cigars.FavoritesScreenViewModel
import com.akellolcc.cigars.mvvm.humidor.HumidorCigarsScreenViewModel
import com.akellolcc.cigars.mvvm.humidor.HumidorDetailsScreenViewModel
import com.akellolcc.cigars.mvvm.humidor.HumidorHistoryScreenViewModel
import com.akellolcc.cigars.mvvm.humidor.HumidorsViewModel
import com.akellolcc.cigars.mvvm.search.CigarsBrandsSearchViewModel
import com.akellolcc.cigars.mvvm.search.CigarsSearchControlViewModel
import com.akellolcc.cigars.mvvm.search.CigarsSearchFieldViewModel
import com.akellolcc.cigars.mvvm.search.SearchCigarScreenViewModel
import com.akellolcc.cigars.screens.navigation.SharedScreen
import com.akellolcc.cigars.screens.navigation.mainScreenModule
import com.akellolcc.cigars.theme.DefaultTheme
import com.akellolcc.cigars.theme.MaterialColors
import com.akellolcc.cigars.theme.materialColor
import com.akellolcc.cigars.utils.Pref
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

internal fun registerViewModels() {
    Log.debug("Register ViewModels")
    ViewModelRegistry.register(
        CigarsDetailsScreenViewModel::class,
        CigarsDetailsScreenViewModel.Factory
    )
    ViewModelRegistry.register(
        CigarHistoryScreenViewModel::class,
        CigarHistoryScreenViewModel.Factory
    )
    ViewModelRegistry.register(
        CigarsScreenViewModel::class,
        CigarsScreenViewModel.Factory
    )
    ViewModelRegistry.register(
        FavoritesScreenViewModel::class,
        FavoritesScreenViewModel.Factory
    )
    ViewModelRegistry.register(
        HumidorCigarsScreenViewModel::class,
        HumidorCigarsScreenViewModel.Factory
    )
    ViewModelRegistry.register(
        HumidorDetailsScreenViewModel::class,
        HumidorDetailsScreenViewModel.Factory
    )
    ViewModelRegistry.register(
        HumidorHistoryScreenViewModel::class,
        HumidorHistoryScreenViewModel.Factory
    )
    ViewModelRegistry.register(
        HumidorsViewModel::class,
        HumidorsViewModel.Factory
    )
    ViewModelRegistry.register(
        CigarImagesViewScreenViewModel::class,
        CigarImagesViewScreenViewModel.Factory
    )
    ViewModelRegistry.register(
        HumidorImagesViewScreenViewModel::class,
        HumidorImagesViewScreenViewModel.Factory
    )
    ViewModelRegistry.register(
        MainScreenViewModel::class,
        MainScreenViewModel.Factory,
        true
    )
    ViewModelRegistry.register(
        SearchCigarScreenViewModel::class,
        SearchCigarScreenViewModel.Factory
    )
    ViewModelRegistry.register(
        CigarsBrandsSearchViewModel::class,
        CigarsBrandsSearchViewModel.Factory
    )
    ViewModelRegistry.register(
        CigarsSearchFieldViewModel::class,
        CigarsSearchFieldViewModel.Factory
    )
    ViewModelRegistry.register(
        CigarsSearchControlViewModel::class,
        CigarsSearchControlViewModel.Factory
    )
}

@Composable
fun CigarsApplication() {
    ScreenRegistry {
        mainScreenModule()
    }

    val initialized = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        registerViewModels()
        Log.debug("Init Database")
        val database = Database.createInstance(false)

        if (Pref.isFirstStart) {
            Log.debug("First start create demo set")
            database.reset()
            CoroutineScope(Dispatchers.IO).launch {
                database.createDemoSet().collect {
                    Log.debug("Created demo set")
                    Pref.isFirstStart = false
                    initialized.value = true
                }
            }
        } else {
            Log.debug("App already initialized")
            initialized.value = true
        }
    }

    DefaultTheme {
        if (!initialized.value) {
            Box(
                modifier = Modifier.fillMaxSize().background(
                    materialColor(
                        MaterialColors.color_onPrimary
                    )
                ), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(64.dp),
                    color = materialColor(MaterialColors.color_primary)
                )
            }
        } else {
            val postMainScreen = rememberScreen(SharedScreen.MainScreen)
            Navigator(postMainScreen)
        }
    }
}