/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/15/24, 2:29 PM
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

import cafe.adriel.voyager.core.model.screenModelScope
import com.akellolcc.cigars.databases.createRepository
import com.akellolcc.cigars.databases.models.Cigar
import com.akellolcc.cigars.databases.models.CigarSortingFields
import com.akellolcc.cigars.databases.repository.CigarsSearchRepository
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.mvvm.base.BaseListViewModel
import com.akellolcc.cigars.screens.components.search.data.CigarSearchParameters
import com.akellolcc.cigars.screens.components.search.data.CigarSortingParameters
import com.akellolcc.cigars.screens.components.search.data.FilterParameter
import com.akellolcc.cigars.utils.ObjectFactory
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.launch

class SearchCigarScreenViewModel :
    BaseListViewModel<Cigar, SearchCigarScreenViewModel.Actions>() {
    override val repository: CigarsSearchRepository = createRepository(CigarsSearchRepository::class)

    init {
        sortField = FilterParameter(CigarSortingFields.Name.value, false)
        sortingFields = CigarSortingParameters()
        searchingFields = CigarSearchParameters()
    }

    override fun entitySelected(entity: Cigar) {
        sendEvent(Actions.RouteToCigar(entity))
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

    override fun loadEntities(reload: Boolean) {
        searchingFields?.let { fields ->
            if (fields.validate()) {
                Log.debug("Load entities searching fields: $fields")
                loading = true
                onDispose()
                runOnDispose {
                    screenModelScope.launch {
                        repository.all(null, fields.selected).cancellable().collect {
                            val list = entities + it
                            sortEntities(list)
                            loading = false
                        }
                    }
                }
            }
        }
    }

    override fun loadMore() {
        loadEntities(false)
    }

    companion object Factory : ObjectFactory<SearchCigarScreenViewModel>() {
        override fun factory(data: Any?): SearchCigarScreenViewModel {
            return SearchCigarScreenViewModel()
        }
    }

    sealed interface Actions {
        data class RouteToCigar(val cigar: Cigar) : Actions
    }
}