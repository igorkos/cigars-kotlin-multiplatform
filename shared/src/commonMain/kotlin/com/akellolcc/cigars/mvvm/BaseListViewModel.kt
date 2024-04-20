package com.akellolcc.cigars.mvvm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.akellolcc.cigars.databases.extensions.BaseEntity
import com.akellolcc.cigars.databases.repository.Repository
import com.badoo.reaktive.disposable.Disposable


abstract class BaseListViewModel<T : BaseEntity, A> : DatabaseViewModel<T, A>() {
    private var sortField by mutableStateOf<String?>(null)
    var accenting by mutableStateOf(true)
    var entities by mutableStateOf(listOf<T>())
    protected var _entities: Disposable? = null

    protected abstract val repository: Repository<T>

    abstract fun entitySelected(entity: T)
    fun sorting(sorting: String) {
        sortField = sorting
        loadEntities(true)
    }

    open fun sortingOrder(ascending: Boolean) {
        accenting = ascending
        loadEntities(true)
    }

    protected open fun loadEntities(reload: Boolean = false) {
        if (_entities == null || reload) {
            _entities = repository.all(sortField, accenting).subscribe {
                entities = it
            }
        }
    }

    open fun loadMore() {
        loadEntities()
    }
}