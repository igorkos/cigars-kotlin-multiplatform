/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/19/24, 1:24 PM
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

import app.cash.paging.PagingSource
import app.cash.sqldelight.ExecutableQuery
import app.cash.sqldelight.Query
import app.cash.sqldelight.SuspendingTransactionWithReturn
import app.cash.sqldelight.paging3.QueryPagingSource
import com.akellolcc.cigars.databases.HistoryDatabaseQueries
import com.akellolcc.cigars.databases.models.History
import com.akellolcc.cigars.screens.components.search.data.FilterParameter
import com.akellolcc.cigars.screens.components.search.data.FiltersList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

class HistoryTableQueries(override val queries: HistoryDatabaseQueries) : DatabaseQueries<History> {
    override fun get(id: Long, where: Long?): Query<History> {
        return queries.get(id, ::historyFactory)
    }

    override fun all(
        sorting: FilterParameter<Boolean>?,
        filter: FiltersList?,
        vararg args: Pair<String, Any?>
    ): Query<History> {
        val accenting = sorting?.value ?: true
        args.find { it.first == CIGAR_ID }?.let {
            return if (accenting) queries.cigarHistory(it.second as Long, ::historyFactory) else queries.cigarHistoryDesc(
                it.second as Long,
                ::historyFactory
            )
        }
        args.find { it.first == HUMIDOR_ID }?.let {
            return if (accenting) queries.humidorHistory(
                it.second as Long,
                ::historyFactory
            ) else queries.humidorHistoryDesc(it.second as Long, ::historyFactory)
        }
        throw IllegalArgumentException("Unknown argument")
    }

    override fun paging(
        sorting: FilterParameter<Boolean>?,
        filter: FiltersList?,
        vararg args: Pair<String, Any?>
    ): PagingSource<Int, History> {
        val accenting = sorting?.value ?: true
        fun queryProvider(limit: Long, offset: Long): Query<History> {
            args.find { it.first == CIGAR_ID }?.let {
                return if (accenting) queries.pagedCigarHistory(
                    it.second as Long,
                    limit,
                    offset,
                    ::historyFactory
                ) else queries.pagedCigarHistoryDesc(
                    it.second as Long, limit,
                    offset, ::historyFactory
                )
            }
            args.find { it.first == HUMIDOR_ID }?.let {
                return if (accenting) queries.pagedHumidorHistory(
                    it.second as Long,
                    limit,
                    offset,
                    ::historyFactory
                ) else queries.pagedHumidorHistoryDesc(
                    it.second as Long, limit,
                    offset, ::historyFactory
                )
            }
            throw IllegalArgumentException("Unknown argument")
        }

        return QueryPagingSource(
            count(*args),
            queries,
            Dispatchers.IO,
            ::queryProvider
        )
    }

    override fun find(rowid: Long, where: Long?): Query<History> {
        return queries.find(rowid, ::historyFactory)
    }

    override fun count(vararg args: Pair<String, Any?>): Query<Long> {
        args.find { it.first == CIGAR_ID }?.let {
            return queries.cigarHistoryCount(it.second as Long)
        }
        args.find { it.first == HUMIDOR_ID }?.let {
            return queries.humidorHistoryCount(it.second as Long)
        }
        throw IllegalArgumentException("Unknown argument")
    }

    override fun contains(rowid: Long, where: Long?): Query<Long> {
        return queries.contains(rowid)
    }

    override fun lastInsertRowId(): ExecutableQuery<Long> {
        return queries.lastInsertRowId()
    }

    override suspend fun update(entity: History) {
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

    override suspend fun add(entity: History) {
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
    }

    override suspend fun remove(rowid: Long, where: Long?) {
        return queries.remove(rowid)
    }

    override suspend fun removeAll() {
        return queries.removeAll()
    }

    override fun empty(): History {
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