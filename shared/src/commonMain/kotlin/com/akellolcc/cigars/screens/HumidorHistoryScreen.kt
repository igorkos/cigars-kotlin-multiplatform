/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/7/24, 12:10 PM
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

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.model.rememberScreenModel
import com.akellolcc.cigars.mvvm.base.createViewModel
import com.akellolcc.cigars.mvvm.humidor.HumidorHistoryScreenViewModel
import com.akellolcc.cigars.screens.base.HistoryScreen
import com.akellolcc.cigars.screens.navigation.NavRoute

class HumidorHistoryScreen(route: NavRoute) : HistoryScreen<HumidorHistoryScreenViewModel>(route) {

    @Composable
    override fun Content() {
        viewModel =
            rememberScreenModel {
                createViewModel(
                    HumidorHistoryScreenViewModel::class,
                    route.data
                )
            }
        super.Content()
    }
}
