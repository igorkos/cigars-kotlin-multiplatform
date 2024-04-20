/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/10/24, 10:05 PM
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

package com.akellolcc.cigars.mvvm

import com.akellolcc.cigars.logging.Log
import dev.icerock.moko.resources.desc.StringDesc


class MainScreenViewModel : ActionsViewModel<MainScreenViewModel.MainScreenActions>() {

    var isTabsVisible: Boolean = true
        set(value) {
            field = value
            Log.debug("Setting tabs visible $value")
            sendEvent(MainScreenActions.TabsVisibility(value))
        }

    var isDrawerVisible: Boolean = false
        set(value) {
            field = value
            sendEvent(MainScreenActions.OpenDrawer(value))
        }

    sealed interface MainScreenActions {
        data class OpenDrawer(val isVisible: Boolean = false) : MainScreenActions
        data class TabsVisibility(val isVisible: Boolean) : MainScreenActions
        data class ShowError(val error: StringDesc) : MainScreenActions
    }
}