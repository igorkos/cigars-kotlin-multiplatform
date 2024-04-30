/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/29/24, 8:41 PM
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

package com.akellolcc.cigars.databases.repository.impl

import com.akellolcc.cigars.databases.HistoryDatabaseQueries
import com.akellolcc.cigars.databases.extensions.History
import com.akellolcc.cigars.databases.repository.HistoryRepository
import com.akellolcc.cigars.databases.repository.impl.queries.HistoryTableQueries
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

abstract class SqlDelightHistoryRepository(
    protected open var id: Long,
    protected val queries: HistoryDatabaseQueries
) : SQLDelightBaseRepository<History>(HistoryTableQueries(queries)), HistoryRepository {

    override fun doUpsert(entity: History, add: Boolean): Flow<History> {
        return flow {
            if (add) {
                queries.add(
                    entity.count,
                    entity.date,
                    entity.left,
                    entity.price,
                    entity.type.type,
                    entity.cigar?.rowid,
                    entity.humidorFrom.rowid,
                    entity.humidorTo.rowid
                )
            } else {
                queries.update(
                    entity.count,
                    entity.date,
                    entity.left,
                    entity.price,
                    entity.type.type,
                    entity.cigar?.rowid,
                    entity.humidorFrom.rowid,
                    entity.humidorTo.rowid,
                    entity.rowid
                )
            }
            emit(entity)
        }
    }

    override fun humidorName(id: Long): String = queries.humidorName(id).executeAsOne()

    override fun cigarName(id: Long): String = queries.cigarName(id).executeAsOne()

}
