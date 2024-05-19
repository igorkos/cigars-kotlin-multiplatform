/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/17/24, 8:27 PM
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
import com.akellolcc.cigars.databases.models.Cigar
import com.akellolcc.cigars.databases.models.CigarSortingFields
import com.akellolcc.cigars.databases.repository.CigarsSearchRepository
import com.akellolcc.cigars.mvvm.base.BaseListViewModel
import com.akellolcc.cigars.screens.components.search.data.CigarSearchParameters
import com.akellolcc.cigars.screens.components.search.data.CigarSortingParameters
import com.akellolcc.cigars.screens.components.search.data.FilterParameter
import com.akellolcc.cigars.utils.ObjectFactory

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