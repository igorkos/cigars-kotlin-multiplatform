package com.akellolcc.cigars.mvvm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.akellolcc.cigars.databases.extensions.BaseEntity
import com.akellolcc.cigars.databases.repository.Repository
import com.akellolcc.cigars.logging.Log
import com.badoo.reaktive.disposable.Disposable
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.observable.subscribe


abstract class BaseListViewModel<T : BaseEntity, A> : DatabaseViewModel<T, A>() {
    private var sortField by mutableStateOf<String?>(null)
    var accenting by mutableStateOf(true)
    var entities by mutableStateOf(listOf<T>())
    private var _entities: Disposable? = null

    protected abstract val repository: Repository<T>

    abstract fun entitySelected(entity: T)
    fun sorting(sorting: String) {
        sortField = sorting
        loadEntities(true)
    }

    fun sortingOrder(ascending: Boolean) {
        accenting = ascending
        loadEntities(true)
    }

    fun loadEntities(reload: Boolean = false) {
        if (_entities == null || reload) {
            _entities = repository.all(sortField, accenting).subscribe{
                Log.debug("Entities updated $it")
                entities = it
            }
        }
    }
}