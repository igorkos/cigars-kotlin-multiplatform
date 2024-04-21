/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/20/24, 5:51 PM
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
import com.akellolcc.cigars.databases.extensions.BaseEntity
import com.akellolcc.cigars.databases.repository.Repository
import com.badoo.reaktive.disposable.Disposable


abstract class BaseListViewModel<T : BaseEntity, A> : DatabaseViewModel<T, A>() {
    protected var sortField by mutableStateOf<String?>(null)
    var accenting by mutableStateOf(true)
    var entities by mutableStateOf(listOf<T>())
    private var _entities: Disposable? = null

    protected abstract val repository: Repository<T>

    abstract fun entitySelected(entity: T)
    open fun sorting(sorting: String) {
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