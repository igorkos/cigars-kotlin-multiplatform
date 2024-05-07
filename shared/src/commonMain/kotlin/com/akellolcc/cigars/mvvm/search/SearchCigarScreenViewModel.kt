/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/7/24, 12:03 PM
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

package com.akellolcc.cigars.mvvm.search

import com.akellolcc.cigars.databases.createRepository
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.CigarSortingFields
import com.akellolcc.cigars.databases.extensions.emptyCigar
import com.akellolcc.cigars.databases.rapid.rest.GetCigarsRequest
import com.akellolcc.cigars.databases.repository.CigarsRepository
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.mvvm.base.BaseListViewModel
import com.akellolcc.cigars.screens.search.data.CigarSortingParameters
import com.akellolcc.cigars.screens.search.data.FilterParameter
import com.akellolcc.cigars.utils.ObjectFactory
import dev.icerock.moko.resources.desc.StringDesc

class SearchCigarScreenViewModel :
    BaseListViewModel<Cigar, SearchCigarScreenViewModel.Actions>() {
    override val repository: CigarsRepository = createRepository(CigarsRepository::class)

    private var request: GetCigarsRequest? = null

    init {
        sortField = FilterParameter(CigarSortingFields.Name.value, false)
        sortingFields = CigarSortingParameters()
    }

    override fun entitySelected(entity: Cigar) {
    }

    override fun sortingOrder(ascending: Boolean) {
        sortField?.value = ascending
        sortEntities(entities)
    }

    private fun sortEntities(list: List<Cigar>) {
        val field = when (sortField?.key) {
            CigarSortingFields.Name.value -> CigarSortingFields.Name
            CigarSortingFields.Brand.value -> CigarSortingFields.Brand
            CigarSortingFields.Country.value -> CigarSortingFields.Country
            else -> CigarSortingFields.Name
        }

        entities = list.sortedWith(compareBy {
            when (field) {
                CigarSortingFields.Name -> it.name
                CigarSortingFields.Brand -> it.brand
                CigarSortingFields.Country -> it.country
                else -> CigarSortingFields.Name
            }
        }).also {
            if (sortField?.value == false) {
                it.reversed()
            }
        }
    }

    fun searchFieldsChanged(fields: List<FilterParameter<*>>?) {
        searchingFields = fields
        entities = listOf()
    }

    override fun loadEntities(reload: Boolean) {
        searchingFields?.let {
            if (request == null || request!!.fields != it) {
                request = GetCigarsRequest(it)
            }
            execute(request!!.next()) { cigars ->
                Log.debug("Cigars: ${cigars.size}")
                if (cigars.isNotEmpty()) {
                    val list = entities + cigars
                    sortEntities(list)
                } else {
                    if (entities.isEmpty()) {
                        entities = listOf(emptyCigar.copy(name = "No results"))
                    }
                }
            }
        }
    }

    override fun loadMore() {
        Log.debug("loadMore")
        loadEntities(false)
    }

    companion object Factory : ObjectFactory<SearchCigarScreenViewModel>() {
        override fun factory(data: Any?): SearchCigarScreenViewModel {
            return SearchCigarScreenViewModel()
        }
    }

    sealed interface Actions {
        data class ShowError(val error: StringDesc) : Actions
    }
}