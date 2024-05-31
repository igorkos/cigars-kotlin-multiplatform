/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/31/24, 10:41 AM
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
import androidx.paging.PagingData
import com.akellolcc.cigars.databases.models.BaseEntity
import com.akellolcc.cigars.databases.repository.Repository
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.screens.components.search.data.FilterCollection
import com.akellolcc.cigars.screens.components.search.data.FilterParameter
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow


abstract class BaseListViewModel<T : BaseEntity> : DatabaseViewModel<T>() {
    private var _entities: Job? = null
    protected abstract val repository: Repository<T>
    protected var sortField by mutableStateOf<FilterParameter<Boolean>?>(null)

    var items by mutableStateOf<Flow<PagingData<T>>?>(null)


    var search by mutableStateOf(false)

    var sortingFields by mutableStateOf<FilterCollection?>(null)
    var searchingFields by mutableStateOf<FilterCollection?>(null)

    abstract fun entitySelected(entity: T)

    val accenting: Boolean
        get() = sortField?.value == true

    var sorting: FilterParameter<Boolean>?
        get() = sortField
        set(value) {
            sortField = value
            paging(true)
        }

    open fun sortingOrder(ascending: Boolean) {
        sortField?.value = ascending
        paging(true)
    }

    open fun updateSearch(value: Boolean) {
        searchingFields = null
        search = value
    }

    fun paging(reset: Boolean = false) {
        if (reset || items == null) {
            Log.debug("${this::class.simpleName} reload paging")
            items = repository.paging(sortField, searchingFields?.selected)
        }
    }


    override fun onDispose() {
        super.onDispose()
        _entities?.cancel()
        _entities = null
    }

}