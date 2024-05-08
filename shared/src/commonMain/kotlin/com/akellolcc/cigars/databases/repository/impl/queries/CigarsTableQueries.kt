/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/7/24, 12:20 PM
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

package com.akellolcc.cigars.databases.repository.impl.queries

import app.cash.sqldelight.ExecutableQuery
import app.cash.sqldelight.Query
import app.cash.sqldelight.SuspendingTransactionWithReturn
import com.akellolcc.cigars.databases.CigarsDatabaseQueries
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.emptyCigar
import com.akellolcc.cigars.screens.components.search.data.FilterParameter

class CigarsTableQueries(override val queries: CigarsDatabaseQueries) : DatabaseQueries<Cigar> {
    override fun get(id: Long, where: Long?): Query<Cigar> {
        return queries.get(id, ::cigarFactory)
    }

    override fun allAsc(sortBy: String, filter: List<FilterParameter<*>>?): Query<Cigar> {
        val search = mutableMapOf<String, Any>()
        filter?.forEach {
            search[it.key] = "%${it.value}%"
        }
        return queries.allAsc(
            search["name"] as? String ?: "%%",
            search["brand"] as? String ?: "%%",
            search["country"] as? String ?: "%%",
            search["date"] as? Long ?: 0L,
            search["cigar"] as? String ?: "%%",
            search["gauge"] as? Long ?: 0L,
            search["length"] as? String ?: "%%",
            search["strength"] as? Long ?: 0L,
            sortBy, ::cigarFactory
        )
    }

    override fun allDesc(sortBy: String, filter: List<FilterParameter<*>>?): Query<Cigar> {
        val search = mutableMapOf<String, Any>()
        filter?.forEach {
            search[it.key] = "%${it.value}%"
        }
        return queries.allDesc(
            search["name"] as? String ?: "%%",
            search["brand"] as? String ?: "%%",
            search["country"] as? String ?: "%%",
            search["date"] as? Long ?: 0L,
            search["cigar"] as? String ?: "%%",
            search["gauge"] as? Long ?: 0L,
            search["length"] as? String ?: "%%",
            search["strength"] as? Long ?: 0L,
            sortBy, ::cigarFactory
        )
    }

    override fun find(rowid: Long, where: Long?): Query<Cigar> {
        return queries.find(rowid, ::cigarFactory)
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

    override fun empty(): Cigar {
        return emptyCigar.copy()
    }

    override suspend fun <R> transactionWithResult(
        bodyWithReturn: suspend SuspendingTransactionWithReturn<R>.() -> R
    ): R {
        return queries.transactionWithResult {
            bodyWithReturn()
        }
    }
}