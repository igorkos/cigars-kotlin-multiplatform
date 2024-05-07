/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/6/24, 1:41 PM
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
import androidx.compose.ui.text.input.KeyboardType
import cafe.adriel.voyager.core.model.screenModelScope
import com.akellolcc.cigars.screens.search.data.FilterParameter
import com.akellolcc.cigars.utils.ObservableValue

abstract class CigarsSearchFieldBaseViewModel(protected val parameter: FilterParameter<*>) :
    ActionsViewModel<CigarsSearchFieldBaseViewModel.Action>() {
    private var lastFocus by mutableStateOf(false)

    //Input flow
    private val valueFlow: ObservableValue<String> = ObservableValue("", 500)
    var value: String
        get() = valueFlow.value
        set(value) {
            updateInput(value)
        }

    val keyboardType: KeyboardType = parameter.keyboardType

    open var isError by mutableStateOf(false)
    var loading by mutableStateOf(false)

    open val annotation: String? = null

    init {
        disposables.value += valueFlow.observe(screenModelScope) {
            processInput(it)
        }
    }


    protected open fun processInput(value: String) {
        parameter.set(value)
        sendEvent(Action.Selected())
    }

    protected open fun updateInput(value: String) {
        valueFlow.value = value
    }

    abstract fun validate(): Boolean

    /**
     * Control data
     */

    open fun onFocusChange(focused: Boolean) {
        if (lastFocus == focused) return
        lastFocus = focused
        if (!focused) {
            parameter.set(value)
            sendEvent(Action.Selected())
        }
    }

    fun onCompleted() {
        parameter.set(value.trim())
        validate()
        sendEvent(Action.Selected())
    }


    sealed interface Action {
        data class Idle(val value: String = "") : Action
        data class Input(val value: String = "") : Action
        data class DropDown(val value: String = "") : Action
        data class Loading(val value: String = "") : Action
        data class Loaded(val value: String = "") : Action
        data class Selected(val value: String = "") : Action
        data class Error(val value: String = "") : Action
    }
}
