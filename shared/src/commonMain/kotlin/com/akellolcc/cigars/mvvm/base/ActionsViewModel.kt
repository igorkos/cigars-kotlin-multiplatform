/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/17/24, 11:46 PM
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

package com.akellolcc.cigars.mvvm.base

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.utils.ObservableValue
import com.akellolcc.cigars.utils.randomString
import kotlinx.coroutines.Job


private data class Event<E>(val id: String, val value: E) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other !is Event<*>) return false
        return id == (other as Event<E>).id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

open class ActionsViewModel<E> : StateScreenModel<Any?>(null) {

    private val events = ObservableValue<Event<E>?>(null)
    private var eventListeners: List<((E) -> Unit)> by mutableStateOf(listOf())
    private var disposables = mutableStateOf<List<Job>>(mutableListOf())

    init {
        disposables.value += (events.observe(screenModelScope) {
            eventListeners.forEach { callback ->
                Log.debug("${this::class.simpleName} Propagate event: ${it?.id} -> ${if (it?.value != null) it.value!!::class.simpleName else "null"}  ")
                it?.let {
                    callback(it.value)
                }
            }
        })
    }

    fun observeEvents(onEach: (E) -> Unit) {
        if (!eventListeners.contains(onEach)) {
            eventListeners = eventListeners + onEach
        }
    }

    fun removeObserver(onEach: (E) -> Unit) {
        Log.debug("${this::class.simpleName} removeObserver $onEach")
        eventListeners = eventListeners - onEach
    }

    fun runOnDispose(block: () -> Job) {
        Log.debug("${this::class.simpleName} runOnDispose")
        disposables.value += block()
    }

    fun sendEvent(event: E) {
        val ev = Event(id = randomString(), value = event)
        Log.debug("${this::class.simpleName} Send event: ${ev.id} -> ${if (event != null) event!!::class.simpleName else "null"} ")
        events.value = ev
    }

    open fun setState(value: Any?) {
        Log.debug("${this::class.simpleName} setState $value")
        mutableState.value = value
    }

    override fun onDispose() {
        Log.debug("${this::class.simpleName} Dispose")
        super.onDispose()
        disposables.value.forEach {
            it.cancel()
        }
    }
}