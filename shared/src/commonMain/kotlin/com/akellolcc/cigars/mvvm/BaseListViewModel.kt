/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/24/24, 1:49 PM
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
 */

package com.akellolcc.cigars.mvvm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.screenModelScope
import com.akellolcc.cigars.databases.extensions.BaseEntity
import com.akellolcc.cigars.databases.repository.Repository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.launch


abstract class BaseListViewModel<T : BaseEntity, A> : DatabaseViewModel<T, A>() {
    private var _entities: Job? = null
    protected abstract val repository: Repository<T>
    protected var sortField by mutableStateOf<String?>(null)

    var accenting by mutableStateOf(true)
    var entities by mutableStateOf(listOf<T>())


    abstract fun entitySelected(entity: T)

    open fun sorting(sorting: String) {
        sortField = sorting
        loadEntities(true)
    }

    open fun sortingOrder(ascending: Boolean) {
        accenting = ascending
        loadEntities(true)
    }

    open fun loadMore() {
        loadEntities()
    }

    protected open fun loadEntities(reload: Boolean = false) {
        if (_entities == null || reload) {
            loading = true
            _entities = screenModelScope.launch {
                repository.all(sortField, accenting).cancellable().collect {
                    entities = it
                    loading = false
                }
            }
        }
    }

    override fun onDispose() {
        super.onDispose()
        _entities?.cancel()
        _entities = null
    }

}