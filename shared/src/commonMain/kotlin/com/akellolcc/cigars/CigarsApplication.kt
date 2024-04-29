/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/28/24, 1:52 PM
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

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.navigator.Navigator
import com.akellolcc.cigars.databases.Database
import com.akellolcc.cigars.mvvm.CigarHistoryScreenViewModel
import com.akellolcc.cigars.mvvm.CigarImagesViewScreenViewModel
import com.akellolcc.cigars.mvvm.CigarsDetailsScreenViewModel
import com.akellolcc.cigars.mvvm.CigarsScreenViewModel
import com.akellolcc.cigars.mvvm.FavoritesScreenViewModel
import com.akellolcc.cigars.mvvm.HumidorCigarsScreenViewModel
import com.akellolcc.cigars.mvvm.HumidorDetailsScreenViewModel
import com.akellolcc.cigars.mvvm.HumidorHistoryScreenViewModel
import com.akellolcc.cigars.mvvm.HumidorImagesViewScreenViewModel
import com.akellolcc.cigars.mvvm.HumidorsViewModel
import com.akellolcc.cigars.mvvm.MainScreenViewModel
import com.akellolcc.cigars.mvvm.SearchCigarScreenViewModel
import com.akellolcc.cigars.mvvm.ViewModelRegistry
import com.akellolcc.cigars.screens.Home
import com.akellolcc.cigars.screens.navigation.mainScreenModule

@Composable
fun CigarsApplication() {
    //Register ViewModels
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
    //Init Database
    Database.createInstance(false)


    ScreenRegistry {
        mainScreenModule()
    }
    Navigator(Home())
}