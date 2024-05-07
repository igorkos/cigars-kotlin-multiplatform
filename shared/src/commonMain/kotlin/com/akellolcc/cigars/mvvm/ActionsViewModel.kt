/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/6/24, 1:39 PM
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
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.akellolcc.cigars.utils.ObservableValue
import kotlinx.coroutines.Job


open class ActionsViewModel<E> : StateScreenModel<Any?>(null) {

    private var eventCallback: ((E) -> Unit)? by mutableStateOf(null)
    private var job = mutableStateOf<Job?>(null)
    protected var disposables = mutableStateOf<List<Job>>(mutableListOf())

    private val events = ObservableValue<E?>(null)

    init {
        job.value = events.observe(screenModelScope) {
            eventCallback?.let { callback ->
                it?.let {
                    callback(it)
                }
            }
        }
    }

    fun observeEvents(onEach: (E) -> Unit) {
        eventCallback = onEach
    }

    fun sendEvent(event: E) {
        events.value = event
    }

    override fun onDispose() {
        super.onDispose()
        job.value?.cancel()
        disposables.value.forEach {
            it.cancel()
        }
    }
}