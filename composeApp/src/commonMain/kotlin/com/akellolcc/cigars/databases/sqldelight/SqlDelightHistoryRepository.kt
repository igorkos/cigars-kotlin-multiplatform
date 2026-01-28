/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/19/24, 1:08 PM
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

package com.akellolcc.cigars.databases.sqldelight

import androidx.paging.PagingData
import com.akellolcc.cigars.databases.HistoryDatabaseQueries
import com.akellolcc.cigars.databases.models.History
import com.akellolcc.cigars.databases.repository.HistoryRepository
import com.akellolcc.cigars.databases.sqldelight.queries.HistoryTableQueries
import com.akellolcc.cigars.screens.components.search.data.FilterParameter
import com.akellolcc.cigars.screens.components.search.data.FiltersList
import kotlinx.coroutines.flow.Flow

abstract class SqlDelightHistoryRepository(
    protected val queries: HistoryDatabaseQueries,
    protected val id: Pair<String, Long>
) : SQLDelightBaseRepository<History>(HistoryTableQueries(queries)), HistoryRepository {
    override fun allSync(sorting: FilterParameter<Boolean>?, filter: FiltersList?): List<History> {
        return allSync(sorting, filter, id)
    }

    override fun all(sorting: FilterParameter<Boolean>?, filter: FiltersList?): Flow<List<History>> {
        return all(sorting, filter, id)
    }

    override fun paging(sorting: FilterParameter<Boolean>?, filter: FiltersList?): Flow<PagingData<History>> {
        return paging(sorting, filter, id)
    }

    override fun count(): Long {
        return count(id)
    }

    override fun humidorName(id: Long): String = queries.humidorName(id).executeAsOne()

    override fun cigarName(id: Long): String = queries.cigarName(id).executeAsOne()

}
