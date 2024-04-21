/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/19/24, 11:45 PM
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

package com.akellolcc.cigars.screens

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.akellolcc.cigars.mvvm.FavoritesScreenViewModel
import com.akellolcc.cigars.screens.navigation.ITabItem
import com.akellolcc.cigars.screens.navigation.NavRoute
import kotlin.jvm.Transient

class FavoritesScreen(override val route: NavRoute) : ITabItem {
    private val screen = FavoritesListScreen(route)

    @Composable
    override fun Content() {
        Navigator(screen)
    }
}

class FavoritesListScreen(route: NavRoute) : CigarsListScreen(route) {

    @Transient
    override val viewModel = FavoritesScreenViewModel()

}
