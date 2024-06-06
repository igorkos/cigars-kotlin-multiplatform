/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/31/24, 12:06 PM
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
import dev.icerock.moko.resources.desc.StringDesc
import kotlinx.coroutines.Job


private data class Event<E>(val value: E, private val id: String = randomString()) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other !is Event<*>) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "Event(id='$id', value=${value?.let { it::class.simpleName } ?: "null"})"
    }
}

open class ActionsViewModel : StateScreenModel<Any?>(null) {

    private val events = ObservableValue<Event<Any>?>(null)
    private var eventListeners: MutableMap<String, ((Any) -> Unit)> by mutableStateOf(mutableMapOf())
    private var disposables = mutableStateOf<List<Job>>(mutableListOf())

    init {
        disposables.value += (events.observe(screenModelScope) {
            eventListeners.forEach { entry ->
                Log.debug("${this::class.simpleName} Propagate event: $it")
                it?.let {
                    entry.value(it.value)
                }
            }
        })
    }

    fun observeEvents(tag: String, onEach: (Any) -> Unit) {
        if (!eventListeners.contains(tag)) {
            Log.debug("${this::class.simpleName} observeEvents $tag")
            eventListeners[tag] = onEach
        }
    }

    fun removeObserver(tag: String) {
        Log.debug("${this::class.simpleName} removeObserver $tag")
        eventListeners.remove(tag)
    }

    fun runOnDispose(block: () -> Job) {
        Log.debug("${this::class.simpleName} runOnDispose")
        disposables.value += block()
    }

    fun sendEvent(event: Any) {
        val ev = Event(event)
        Log.debug("${this::class.simpleName} Send event: $ev")
        events.value = ev
    }

    open fun setState(value: Any?) {
        Log.debug("${this::class.simpleName} setState $value")
        mutableState.value = value
    }

    fun onBackPress() {
        sendEvent(CommonAction.OnBackPressed())
    }

    override fun onDispose() {
        Log.debug("${this::class.simpleName} Dispose")
        super.onDispose()
        disposables.value.forEach {
            it.cancel()
        }
    }

    interface CommonAction {
        data class OnBackPressed(val dummy: Boolean = false) : CommonAction
        data class ShowError(val error: StringDesc) : CommonAction
    }
}

