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

import app.cash.sqldelight.ExecutableQuery
import app.cash.sqldelight.Query
import app.cash.sqldelight.SuspendingTransactionWithReturn
import app.cash.sqldelight.paging3.QueryPagingSource
import com.akellolcc.cigars.databases.HumidorCigarsDatabaseQueries
import com.akellolcc.cigars.databases.models.HumidorCigar
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.screens.components.search.data.FilterParameter
import com.akellolcc.cigars.screens.components.search.data.FiltersList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

class CigarHumidorTableQueries(override val queries: HumidorCigarsDatabaseQueries) :
    DatabaseQueries<HumidorCigar> {
    override fun get(id: Long, where: Long?): Query<HumidorCigar> {
        return queries.get(id, where!!, ::humidorCigarFactory)
    }

    override fun all(
        sorting: FilterParameter<Boolean>?,
        filter: FiltersList?,
        vararg args: Pair<String, Any?>
    ): Query<HumidorCigar> {
        args.find { it.first == CIGAR_ID }?.let {
            val rowId = it.second as Long
            return queries.cigarHumidors(rowId, ::humidorCigarFactory)
        }
        args.find { it.first == HUMIDOR_ID }?.let {
            val rowId = it.second as Long
            return queries.humidorCigars(rowId, ::humidorCigarFactory)
        }
        throw IllegalArgumentException("No cigar or humidor specified")
    }

    override fun paging(
        sorting: FilterParameter<Boolean>?,
        filter: FiltersList?,
        vararg args: Pair<String, Any?>
    ): app.cash.paging.PagingSource<Int, HumidorCigar> {
        fun queryProvider(limit: Long, offset: Long): Query<HumidorCigar> {
            args.find { it.first == CIGAR_ID }?.let {
                val rowId = it.second as Long
                return queries.pagedCigarHumidors(rowId, limit, offset, ::humidorCigarFactory)
            }
            args.find { it.first == HUMIDOR_ID }?.let {
                val rowId = it.second as Long
                return queries.pagedHumidorCigars(rowId, limit, offset, ::humidorCigarFactory)
            }
            Log.warn("No cigar or humidor specified return all")
            return queries.all(::humidorCigarFactory)
        }

        return QueryPagingSource(
            count(*args),
            queries,
            Dispatchers.IO,
            ::queryProvider
        )
    }

    override fun find(rowid: Long, where: Long?): Query<HumidorCigar> {
        return queries.find(rowid, where!!, ::humidorCigarFactory)
    }

    override fun count(vararg args: Pair<String, Any?>): Query<Long> {
        args.find { it.first == CIGAR_ID }?.let {
            return queries.cigarsCount(it.second as Long)
        }
        args.find { it.first == HUMIDOR_ID }?.let {
            return queries.humidorsCount(it.second as Long)
        }
        throw IllegalArgumentException("No cigar or humidor specified")
    }


    override fun contains(rowid: Long, where: Long?): Query<Long> {
        return queries.contains(rowid, where!!)
    }

    override fun lastInsertRowId(): ExecutableQuery<Long> {
        return queries.lastInsertRowId()
    }

    override suspend fun update(entity: HumidorCigar) {
        queries.update(
            entity.count,
            entity.humidor.rowid,
            entity.cigar.rowid
        )
    }

    override suspend fun add(entity: HumidorCigar) {
        queries.add(
            entity.count,
            entity.humidor.rowid,
            entity.cigar.rowid
        )
    }

    override suspend fun remove(rowid: Long, where: Long?) {
        return queries.remove(rowid, where!!)
    }

    override suspend fun removeAll() {
        return queries.removeAll()
    }

    override fun empty(): HumidorCigar {
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