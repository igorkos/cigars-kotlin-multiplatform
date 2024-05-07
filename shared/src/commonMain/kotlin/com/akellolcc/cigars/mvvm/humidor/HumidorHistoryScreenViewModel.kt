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

package com.akellolcc.cigars.mvvm.humidor

import com.akellolcc.cigars.databases.createRepository
import com.akellolcc.cigars.databases.extensions.History
import com.akellolcc.cigars.databases.extensions.Humidor
import com.akellolcc.cigars.databases.repository.HumidorHistoryRepository
import com.akellolcc.cigars.mvvm.base.HistoryScreenViewModel
import com.akellolcc.cigars.utils.ObjectFactory


class HumidorHistoryScreenViewModel(val humidor: Humidor) : HistoryScreenViewModel() {
    override val repository: HumidorHistoryRepository =
        createRepository(HumidorHistoryRepository::class, humidor.rowid)

    override fun entitySelected(entity: History) {}

    init {
        name = humidor.name
    }

    companion object Factory : ObjectFactory<HumidorHistoryScreenViewModel>() {
        override fun factory(data: Any?): HumidorHistoryScreenViewModel {
            return HumidorHistoryScreenViewModel(data as Humidor)
        }
    }
}