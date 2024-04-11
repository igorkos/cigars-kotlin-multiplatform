package com.akellolcc.cigars.mvvm

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.akellolcc.cigars.databases.extensions.BaseEntity
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.CigarImage
import com.akellolcc.cigars.databases.extensions.ObservableEntity
import com.akellolcc.cigars.databases.repository.Repository
import com.akellolcc.cigars.logging.Log


abstract class BaseListViewModel<T : BaseEntity, A> : DatabaseViewModel<T, A>() {
    private var sortField by mutableStateOf<String?>(null)
    var accenting by mutableStateOf(true)
    var entities by mutableStateOf(listOf<T>())
    private var _entities: ObservableEntity<List<T>>? = null

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
            _entities = ObservableEntity(repository.observeAll(sortField, accenting))
            _entities?.map { entities = it ?:listOf() }
        }
    }
    @Composable
    fun asState(): State<List<T>> {

        return repository.observeAll(sortField, accenting).collectAsState(listOf())
    }
}