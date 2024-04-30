/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/29/24, 9:02 PM
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

package com.akellolcc.cigars.mvvm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.akellolcc.cigars.utils.ObjectFactory
import dev.icerock.moko.resources.desc.StringDesc

class MainScreenViewModel : ActionsViewModel<MainScreenViewModel.MainScreenActions>() {
    var isTabsVisible by mutableStateOf(true)
    var isDrawerVisible by mutableStateOf(false)

    companion object Factory : ObjectFactory<MainScreenViewModel>() {
        override fun factory(data: Any?): MainScreenViewModel {
            return MainScreenViewModel()
        }
    }

    sealed interface MainScreenActions {
        data class ShowError(val error: StringDesc) : MainScreenActions
    }
}