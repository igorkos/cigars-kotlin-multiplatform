/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/4/24, 11:42 AM
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
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.screens.search.data.FilterParameter
import com.akellolcc.cigars.utils.ObjectFactory
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class CigarsSearchFieldViewModel(private val parameter: FilterParameter<*>) :
    DatabaseViewModel<Cigar, CigarsSearchFieldViewModel.Action>() {

    //Input flow
    private val _inputValue = Channel<String>(Channel.BUFFERED)
    private val inputValue: Flow<String> get() = _inputValue.receiveAsFlow()
    private var input by mutableStateOf("")

    var isError by mutableStateOf(false)

    init {
        disposables.value += screenModelScope.launch {
            inputValue.debounce(500).collectLatest { value ->
                parameter.set(value)
            }
        }
    }

    fun validate(): Boolean {
        val valid = input.isNotEmpty() && input.length >= 3
        isError = !valid
        return valid
    }

    val annotation: String?
        get() {
            return if (validate()) {
                null
            } else {
                "Please enter at least 3 characters to narrow down your search"
            }
        }

    /**
     * Control data
     */
    private var lastFocus = false
    fun onFocusChange(focused: Boolean) {
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

    /**
     * Input value change
     */
    var value: String
        get() = input
        set(value) {
            input = value
            _inputValue.trySend(value.trim())
        }

    val keyboardType: KeyboardType = parameter.keyboardType

    @Suppress("UNCHECKED_CAST")
    companion object Factory : ObjectFactory<CigarsSearchFieldViewModel>() {
        override fun factory(data: Any?): CigarsSearchFieldViewModel {
            return CigarsSearchFieldViewModel(data as FilterParameter<String>)
        }
    }

    sealed interface Action {
        data class Selected(val value: String = "") : Action
        data class Error(val value: String = "") : Action
    }
}
