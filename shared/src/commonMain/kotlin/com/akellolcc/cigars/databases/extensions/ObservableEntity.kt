package com.akellolcc.cigars.databases.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import dev.icerock.moko.mvvm.flow.CMutableStateFlow
import dev.icerock.moko.mvvm.flow.cMutableStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ObservableEntity<D>(private val dbEntity: Flow<D>) {
    private val listeners = mutableListOf<ObservableEntityListener<D, *>>()
    private val entity: CMutableStateFlow<D?> = MutableStateFlow<D?>(null).cMutableStateFlow()
    val value: D?
        get() = entity.value
    init {
        CoroutineScope(Dispatchers.Default).launch {
            dbEntity.collect{ ent ->
                entity.value = ent
                reload()
            }
        }
    }

    fun reload() {
        listeners.forEach {
            it.invoke(entity.value)
        }
    }

    fun <T>map(transform: (D?) -> T?) : MutableState<T?> {
        val listener = ObservableEntityListener<D, T>(transform(entity.value),transform)
        listeners.add(listener)
        return listener.value
    }
    @Composable
    fun asState() : State<D?> {
        return entity.collectAsState()
    }
}

private class ObservableEntityListener<D, T>(initialValue: T?, private val transform: (D?) -> Any?){
    val value = mutableStateOf(initialValue)
    fun invoke(value: D?) {
        // Log.debug("Invoke: $value")
        this.value.value = transform(value) as T?
    }
}
