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


open class ActionsViewModel<E>  : ViewModel() {

    private val _events = Channel<E>(Channel.BUFFERED)
    private val events: CFlow<E> get() = _events.receiveAsFlow().cFlow()

    @Composable
    fun observeEvents(onEach: (E) -> Unit) {
        events.observeAsActions(onEach = onEach)
    }

    protected fun sendEvent(event: E) {
        CoroutineScope(Dispatchers.Main).launch {
            _events.send(event)
        }
    }

}