/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/23/24, 11:15 PM
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

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


open class ActionsViewModel<E> : ScreenModel {

    private val _events = Channel<E>(Channel.BUFFERED)
    private val events: Flow<E> get() = _events.receiveAsFlow()

    fun observeEvents(onEach: (E) -> Unit) {
        screenModelScope.launch {
            events.collect { onEach(it) }
        }
    }

    fun sendEvent(event: E) {
        screenModelScope.launch {
            _events.send(event)
        }
    }

}