/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/27/24, 2:13 PM
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

package com.akellolcc.cigars.mvvm

import com.akellolcc.cigars.databases.createRepository
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.History
import com.akellolcc.cigars.databases.repository.CigarHistoryRepository


class CigarHistoryScreenViewModel(val cigar: Cigar) : HistoryScreenViewModel() {
    override val repository: CigarHistoryRepository = createRepository(CigarHistoryRepository::class, cigar.rowid)

    override fun entitySelected(entity: History) {
    }

    init {
        name = cigar.name
    }

    companion object Factory : ViewModelsFactory<CigarHistoryScreenViewModel>() {
        override fun factory(data: Any?): CigarHistoryScreenViewModel {
            return CigarHistoryScreenViewModel(data as Cigar)
        }

    }
}