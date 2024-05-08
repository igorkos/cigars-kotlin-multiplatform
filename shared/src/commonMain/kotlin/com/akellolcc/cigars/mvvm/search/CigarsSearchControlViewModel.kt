/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/7/24, 3:46 PM
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

package com.akellolcc.cigars.mvvm.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.mvvm.base.ActionsViewModel
import com.akellolcc.cigars.screens.components.search.data.FilterCollection
import com.akellolcc.cigars.utils.ObjectFactory

class CigarsSearchControlViewModel(val fields: FilterCollection) :
    ActionsViewModel<CigarsSearchFieldBaseViewModel.Action>() {
    var expanded by mutableStateOf(false)

    /**
     * Control data
     */
    fun onFieldAction(action: CigarsSearchFieldBaseViewModel.Action) {
        Log.debug("onFieldAction $action")
        when (action) {
            is CigarsSearchFieldBaseViewModel.Action.AddField -> {
                fields.add(action.field)
            }

            is CigarsSearchFieldBaseViewModel.Action.RemoveField -> {
                fields.remove(action.field)
            }

            is CigarsSearchFieldBaseViewModel.Action.FieldSearch -> {
                if (isAllValid) {
                    sendEvent(CigarsSearchFieldBaseViewModel.Action.ExecuteSearch())
                }
            }

            else -> {}
        }
    }

    val isAllValid: Boolean
        get() = fields.controls.all { it.validate() }

    companion object Factory : ObjectFactory<CigarsSearchControlViewModel>() {
        override fun factory(data: Any?): CigarsSearchControlViewModel {
            return CigarsSearchControlViewModel(data as FilterCollection)
        }
    }

}
