/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/19/24, 2:33 PM
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

package com.akellolcc.cigars.mvvm.cigars

import com.akellolcc.cigars.databases.createRepository
import com.akellolcc.cigars.databases.models.Cigar
import com.akellolcc.cigars.databases.models.CigarSortingFields
import com.akellolcc.cigars.databases.repository.CigarsRepository
import com.akellolcc.cigars.mvvm.base.BaseListViewModel
import com.akellolcc.cigars.screens.components.search.data.CigarFilterParameters
import com.akellolcc.cigars.screens.components.search.data.CigarSortingParameters
import com.akellolcc.cigars.screens.components.search.data.FilterParameter
import com.akellolcc.cigars.utils.ObjectFactory
import dev.icerock.moko.resources.desc.StringDesc


open class CigarsScreenViewModel : BaseListViewModel<Cigar, CigarsScreenViewModel.CigarsAction>() {
    override val repository: CigarsRepository = createRepository(CigarsRepository::class)

    init {
        sortField = FilterParameter(CigarSortingFields.Name.value, true)
        sortingFields = CigarSortingParameters()
        searchingFields = CigarFilterParameters()
    }

    override fun entitySelected(entity: Cigar) {
        sendEvent(CigarsAction.RouteToCigar(entity))
    }

    override fun updateSearch(value: Boolean) {
        searchingFields = CigarFilterParameters()
        search = value
    }

    companion object Factory : ObjectFactory<CigarsScreenViewModel>() {
        override fun factory(data: Any?): CigarsScreenViewModel {
            return CigarsScreenViewModel()
        }
    }

    sealed interface CigarsAction {
        data class RouteToCigar(val cigar: Cigar) : CigarsAction
        data class ShowError(val error: StringDesc) : CigarsAction
    }
}