/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/23/24, 3:47 PM
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
import com.akellolcc.cigars.databases.repository.CigarHistoryRepository
import com.akellolcc.cigars.databases.repository.impl.queries.historyFactory
import com.badoo.reaktive.coroutinesinterop.asObservable
import com.badoo.reaktive.observable.ObservableWrapper
import com.badoo.reaktive.observable.wrap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

class SqlDelightCigarHistoryRepository(
    id: Long,
    queries: HistoryDatabaseQueries
) : SqlDelightHistoryRepository(id, queries), CigarHistoryRepository {

    override fun all(sortField: String?, accenting: Boolean): ObservableWrapper<List<History>> {
        return queries.cigarHistory(id, ::historyFactory).asFlow().mapToList(Dispatchers.IO)
            .asObservable().wrap()
    }

    companion object Factory : RepositoryFactory<SqlDelightCigarHistoryRepository>() {
        override fun factory(data: Any?): SqlDelightCigarHistoryRepository {
            val queries = SqlDelightDatabase.instance.database.historyDatabaseQueries
            return SqlDelightCigarHistoryRepository(data as Long, queries)
        }
    }
}
