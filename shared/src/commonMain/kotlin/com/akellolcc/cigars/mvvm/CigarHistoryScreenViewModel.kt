/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/17/24, 1:17 AM
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

import com.akellolcc.cigars.databases.RepositoryType
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.History
import com.akellolcc.cigars.databases.repository.HistoryRepository


class CigarHistoryScreenViewModel(val cigar: Cigar) : HistoryScreenViewModel() {
    override val repository: HistoryRepository =
        database.getRepository(RepositoryType.CigarHistory, cigar.rowid)

    override fun entitySelected(entity: History) {
    }

    init {
        name = cigar.name
    }

    override fun entityName(id: History): String {
        return repository.humidorName(id.humidorFrom)
    }
}