package com.akellolcc.cigars.mvvm

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.akellolcc.cigars.databases.extensions.BaseEntity
import com.akellolcc.cigars.databases.repository.Repository


abstract class BaseListViewModel<T : BaseEntity, A> : ActionsViewModel<A>() {
    protected abstract val database: Repository<T>
    var loading by mutableStateOf(false)

    @Composable
    abstract fun asState(): State<List<T>>

}