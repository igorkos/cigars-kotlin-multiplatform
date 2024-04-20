/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/16/24, 6:35 PM
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

package com.akellolcc.cigars.databases.repository.impl.queries

import app.cash.sqldelight.ExecutableQuery
import app.cash.sqldelight.Query
import app.cash.sqldelight.SuspendingTransactionWithReturn
import com.akellolcc.cigars.databases.HumidorsDatabaseQueries
import com.akellolcc.cigars.databases.extensions.Humidor
import com.akellolcc.cigars.databases.repository.DatabaseQueries

class HumidorsTableQueries(override val queries: HumidorsDatabaseQueries) :
    DatabaseQueries<Humidor> {
    override fun get(id: Long, where: Long?): Query<Humidor> {
        return queries.get(id, ::humidorFactory)
    }

    override fun allAsc(sort: String): Query<Humidor> {
        return queries.allAsc(sort, ::humidorFactory)
    }

    override fun allDesc(sort: String): Query<Humidor> {
        return queries.allDesc(sort, ::humidorFactory)
    }

    override fun find(rowid: Long, where: Long?): Query<Humidor> {
        return queries.find(rowid, ::humidorFactory)
    }

    override fun count(): Query<Long> {
        return queries.count()
    }

    override fun contains(rowid: Long, where: Long?): Query<Long> {
        return queries.contains(rowid)
    }

    override fun lastInsertRowId(): ExecutableQuery<Long> {
        return queries.lastInsertRowId()
    }

    override suspend fun remove(rowid: Long, where: Long?) {
        return queries.remove(rowid)
    }

    override suspend fun removeAll() {
        return queries.removeAll()
    }

    override fun empty(): Humidor {
        TODO("Not yet implemented")
    }

    override suspend fun <R> transactionWithResult(
        bodyWithReturn: suspend SuspendingTransactionWithReturn<R>.() -> R
    ): R {
        return queries.transactionWithResult {
            bodyWithReturn()
        }
    }
}