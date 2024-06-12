/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/11/24, 7:28 PM
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
import com.akellolcc.cigars.screens.components.search.data.FiltersList
import com.akellolcc.cigars.theme.Images
import com.akellolcc.cigars.theme.Localize
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow


abstract class BaseListViewModel<T : BaseEntity> : DatabaseViewModel<T>() {
    private var _entities: Job? = null
    protected abstract val repository: Repository<T>
    protected var sortField by mutableStateOf<FilterParameter<Boolean>?>(null)

    var items by mutableStateOf<Flow<PagingData<T>>?>(null)

    lateinit var topBarMenu: FiltersList
    var search by mutableStateOf(false)
    var accenting by mutableStateOf(true)
    var sortingFields by mutableStateOf<FilterCollection?>(null)
    var searchingFields by mutableStateOf<FilterCollection?>(null)
    val sortOrderField = FilterParameter(
        Localize.cigar_details_top_bar_sort_order_desc,
        true,
        Localize.screen_list_sort_accenting,
        Images.icon_menu_sort_alpha_asc,
        false
    )

    abstract fun entitySelected(entity: T)


    var sorting: FilterParameter<Boolean>?
        get() = sortField
        set(value) {
            sortField = value
            paging(true)
        }

    open fun sortingOrder(ascending: Boolean) {
        accenting = ascending
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