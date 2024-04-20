/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/10/24, 10:04 PM
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

import androidx.compose.runtime.Composable
import dev.icerock.moko.mvvm.flow.CFlow
import dev.icerock.moko.mvvm.flow.cFlow
import dev.icerock.moko.mvvm.flow.compose.observeAsActions
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


open class ActionsViewModel<E> : ViewModel() {

    private val _events = Channel<E>(Channel.BUFFERED)
    private val events: CFlow<E> get() = _events.receiveAsFlow().cFlow()

    @Composable
    fun observeEvents(onEach: (E) -> Unit) {
        events.observeAsActions(onEach = onEach)
    }

    fun sendEvent(event: E) {
        CoroutineScope(Dispatchers.Main).launch {
            _events.send(event)
        }
    }

}