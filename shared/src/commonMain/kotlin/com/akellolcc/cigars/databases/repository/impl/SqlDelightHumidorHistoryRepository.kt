/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/26/24, 5:20 PM
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

package com.akellolcc.cigars.databases.repository.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.akellolcc.cigars.databases.HistoryDatabaseQueries
import com.akellolcc.cigars.databases.RepositoryFactory
import com.akellolcc.cigars.databases.extensions.History
import com.akellolcc.cigars.databases.repository.HumidorHistoryRepository
import com.akellolcc.cigars.databases.repository.impl.queries.historyFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow

class SqlDelightHumidorHistoryRepository(
    id: Long,
    queries: HistoryDatabaseQueries
) : SqlDelightHistoryRepository(id, queries), HumidorHistoryRepository {

    override fun all(sortField: String?, accenting: Boolean): Flow<List<History>> {
        return queries.humidorHistory(id, ::historyFactory).asFlow().mapToList(Dispatchers.IO)
    }

    override fun count(): Long {
        return queries.humidorHistoryCount(id).executeAsOne()
    }

    companion object Factory : RepositoryFactory<SqlDelightHumidorHistoryRepository>() {
        override fun factory(data: Any?): SqlDelightHumidorHistoryRepository {
            val queries = SqlDelightDatabase.instance.database.historyDatabaseQueries
            return SqlDelightHumidorHistoryRepository(
                data as Long,
                queries
            )
        }
    }
}
