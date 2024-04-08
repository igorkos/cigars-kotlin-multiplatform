package com.akellolcc.cigars.mvvm

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import com.akellolcc.cigars.databases.extensions.BaseEntity
import com.akellolcc.cigars.databases.repository.Repository


abstract class BaseListViewModel<T : BaseEntity, A> : DatabaseViewModel<T, A>() {
    protected abstract val repository: Repository<T>

    @Composable
    fun asState(): State<List<T>> {
        return repository.observeAll().collectAsState(listOf())
    }
}