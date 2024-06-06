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
import com.akellolcc.cigars.databases.HumidorsDatabaseQueries
import com.akellolcc.cigars.databases.createRepository
import com.akellolcc.cigars.databases.models.History
import com.akellolcc.cigars.databases.models.HistoryType
import com.akellolcc.cigars.databases.models.Humidor
import com.akellolcc.cigars.databases.repository.HumidorHistoryRepository
import com.akellolcc.cigars.databases.repository.HumidorsRepository
import com.akellolcc.cigars.databases.sqldelight.queries.HumidorsTableQueries
import com.akellolcc.cigars.screens.components.search.data.FilterParameter
import com.akellolcc.cigars.screens.components.search.data.FiltersList
import com.akellolcc.cigars.utils.ObjectFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import kotlinx.datetime.Clock

class SqlDelightHumidorsRepository(val queries: HumidorsDatabaseQueries) :
    SQLDelightBaseRepository<Humidor>(HumidorsTableQueries(queries)), HumidorsRepository {

    /**
     * Adds a new Humidor to the database.
     * 1. Adds the Humidor to the database.
     * 2. Adds the Humidor's history to the database.
     */
    override fun add(entity: Humidor): Flow<Humidor> {
        return super.add(entity).map {
            val hisDatabase: HumidorHistoryRepository = createRepository(HumidorHistoryRepository::class, entity.rowid)
            hisDatabase.add(
                History(
                    -1,
                    1,
                    Clock.System.now().toEpochMilliseconds(),
                    1,
                    entity.price,
                    HistoryType.Addition,
                    null,
                    it,
                    it
                )
            ).single()
            it
        }
    }

    override fun updateCigarsCount(humidor: Long, count: Long): Flow<Long> {
        return flow {
            queries.updateCigarsCount(count, humidor)
            emit(humidor)
        }
    }

    override fun allSync(sorting: FilterParameter<Boolean>?, filter: FiltersList?): List<Humidor> {
        return super.allSync(sorting, filter, Pair("rowid", "DESC"))
    }

    override fun all(sorting: FilterParameter<Boolean>?, filter: FiltersList?): Flow<List<Humidor>> {
        return super.all(sorting, filter, Pair("rowid", "DESC"))
    }

    override fun paging(sorting: FilterParameter<Boolean>?, filter: FiltersList?): Flow<PagingData<Humidor>> {
        return super.paging(sorting, filter, Pair("rowid", "DESC"))
    }

    override fun count(): Long {
        return super.count(Pair("rowid", "DESC"))
    }

    companion object Factory : ObjectFactory<SqlDelightHumidorsRepository>() {
        override fun factory(data: Any?): SqlDelightHumidorsRepository {
            val queries = SqlDelightDatabase.instance.database.humidorsDatabaseQueries
            return SqlDelightHumidorsRepository(queries)
        }
    }
}
