/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/4/24, 11:35 AM
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
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.screens.search.SearchParameterAction
import com.akellolcc.cigars.screens.search.data.FilterCollection
import com.akellolcc.cigars.screens.search.data.FilterParameter
import com.akellolcc.cigars.utils.ObjectFactory


class CigarsSearchControlViewModel(val fields: FilterCollection) :
    DatabaseViewModel<Cigar, CigarsSearchControlViewModel.Action>() {
    var expanded by mutableStateOf(false)

    /**
     * Control data
     */
    fun onFieldAction(action: SearchParameterAction, field: FilterParameter<*>) {
        when (action) {
            SearchParameterAction.Add -> {
                fields.add(field)
                sendEvent(Action.FieldsUpdate())
            }

            SearchParameterAction.Remove -> {
                fields.remove(field)
                sendEvent(Action.FieldsUpdate())
            }

            SearchParameterAction.Completed -> {
                if (isAllValid) {
                    sendEvent(Action.Completed())
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

    sealed interface Action {
        data class Completed(val value: String = "") : Action
        data class FieldsUpdate(val value: String = "") : Action
        data class Error(val value: String = "") : Action
    }
}
