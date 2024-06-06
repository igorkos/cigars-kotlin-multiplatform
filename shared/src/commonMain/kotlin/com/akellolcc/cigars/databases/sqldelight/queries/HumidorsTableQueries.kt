/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/19/24, 1:25 PM
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

package com.akellolcc.cigars.databases.sqldelight.queries

import app.cash.sqldelight.ExecutableQuery
import app.cash.sqldelight.Query
import app.cash.sqldelight.SuspendingTransactionWithReturn
import app.cash.sqldelight.paging3.QueryPagingSource
import com.akellolcc.cigars.databases.HumidorsDatabaseQueries
import com.akellolcc.cigars.databases.models.Humidor
import com.akellolcc.cigars.databases.models.emptyHumidor
import com.akellolcc.cigars.screens.components.search.data.FilterParameter
import com.akellolcc.cigars.screens.components.search.data.FiltersList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

class HumidorsTableQueries(override val queries: HumidorsDatabaseQueries) :
    DatabaseQueries<Humidor> {
    override fun get(id: Long, where: Long?): Query<Humidor> {
        return queries.get(id, ::humidorFactory)
    }

    override fun all(
        sorting: FilterParameter<Boolean>?,
        filter: FiltersList?,
        vararg args: Pair<String, Any?>
    ): Query<Humidor> {
        val accenting = sorting?.value ?: true
        val sortKey = sorting?.key ?: "name"
        return if (accenting) {
            queries.allAsc(sortKey, ::humidorFactory)
        } else {
            queries.allDesc(sortKey, ::humidorFactory)
        }
    }

    override fun paging(
        sorting: FilterParameter<Boolean>?,
        filter: FiltersList?,
        vararg args: Pair<String, Any?>
    ): app.cash.paging.PagingSource<Int, Humidor> {
        val accenting = sorting?.value ?: true
        val sortKey = sorting?.key ?: "name"

        fun queryProvider(limit: Long, offset: Long): Query<Humidor> {
            return if (accenting) {
                queries.pagedAsc(
                    sortKey,
                    limit,
                    offset,
                    ::humidorFactory
                )
            } else {
                queries.pagedDesc(
                    sortKey,
                    limit,
                    offset,
                    ::humidorFactory
                )
            }
        }
        return QueryPagingSource(
            queries.count(),
            queries,
            Dispatchers.IO,
            ::queryProvider
        )
    }

    override fun find(rowid: Long, where: Long?): Query<Humidor> {
        return queries.find(rowid, ::humidorFactory)
    }

    override fun count(vararg args: Pair<String, Any?>): Query<Long> {
        return queries.count()
    }

    override fun contains(rowid: Long, where: Long?): Query<Long> {
        return queries.contains(rowid)
    }

    override fun lastInsertRowId(): ExecutableQuery<Long> {
        return queries.lastInsertRowId()
    }

    override suspend fun update(entity: Humidor) {
        queries.update(
            entity.name,
            entity.brand,
            entity.holds,
            entity.count,
            entity.temperature,
            entity.humidity,
            entity.notes,
            entity.link,
            entity.autoOpen,
            entity.sorting,
            entity.type,
            entity.rowid
        )
    }

    override suspend fun add(entity: Humidor) {
        queries.add(
            entity.name,
            entity.brand,
            entity.holds,
            entity.count,
            entity.temperature,
            entity.humidity,
            entity.notes,
            entity.link,
            entity.autoOpen,
            entity.sorting,
            entity.type
        )
    }

    override suspend fun remove(rowid: Long, where: Long?) {
        return queries.remove(rowid)
    }

    override suspend fun removeAll() {
        return queries.removeAll()
    }

    override fun empty(): Humidor {
        return emptyHumidor.copy()
    }

    override suspend fun <R> transactionWithResult(
        bodyWithReturn: suspend SuspendingTransactionWithReturn<R>.() -> R
    ): R {
        return queries.transactionWithResult {
            bodyWithReturn()
        }
    }
}