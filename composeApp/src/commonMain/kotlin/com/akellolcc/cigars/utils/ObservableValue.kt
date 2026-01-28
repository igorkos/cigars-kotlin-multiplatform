/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/8/24, 11:16 AM
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

package com.akellolcc.cigars.utils

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.akellolcc.cigars.logging.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

data class ObservableValue<T>(private val defaultValue: T, private val debounce: Long = 0) {
    private val _inputValue = Channel<T>(capacity = Channel.CONFLATED, onUndeliveredElement = {
        Log.error("Undelivered element: $it")
    })
    private val inputValue: Flow<T> get() = _inputValue.receiveAsFlow()
    private var input by mutableStateOf(defaultValue)
    var value: T
        get() = input
        set(value) {
            input = value
            _inputValue.trySend(value)
        }

    @OptIn(FlowPreview::class)
    fun observe(scope: CoroutineScope, callback: (T) -> Unit): Job {
        return scope.launch {
            if (debounce > 0)
                inputValue.cancellable().debounce(debounce).collectLatest {
                    callback(it)
                }
            else
                inputValue.cancellable().collect {
                    callback(it)
                }
        }
    }

}