/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/19/24, 1:30 PM
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
import com.akellolcc.cigars.databases.CigarsDatabaseQueries
import com.akellolcc.cigars.databases.models.Cigar
import com.akellolcc.cigars.databases.models.CigarStrength
import com.akellolcc.cigars.databases.models.emptyCigar
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.screens.components.search.data.FilterParameter
import com.akellolcc.cigars.screens.components.search.data.FiltersList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO


class CigarsTableQueries(override val queries: CigarsDatabaseQueries) : DatabaseQueries<Cigar> {
    override fun get(id: Long, where: Long?): Query<Cigar> {
        return queries.get(id, ::cigarFactory)
    }

    override fun all(sorting: FilterParameter<Boolean>?, filter: FiltersList?, vararg args: Pair<String, Any?>): Query<Cigar> {
        val sortBy = sorting?.key ?: "name"
        return if (sorting?.value != false) {
            return queries.allAsc(
                FiltersList.getSQLWhere<String>(filter, "name"),
                FiltersList.getSQLWhere<String>(filter, "brand"),
                FiltersList.getSQLWhere<String>(filter, "country"),
                FiltersList.getSQLWhere<Long>(filter, "date"),
                FiltersList.getSQLWhere<String>(filter, "cigar"),
                FiltersList.getSQLWhere<Long>(filter, "gauge"),
                FiltersList.getSQLWhere<String>(filter, "length"),
                FiltersList.getSQLWhere<Long>(filter, "strength"),
                FiltersList.getSQLWhere<Boolean>(filter, "favorites"),
                sortBy, ::cigarFactory
            )
        } else {
            queries.allDesc(
                FiltersList.getSQLWhere<String>(filter, "name"),
                FiltersList.getSQLWhere<String>(filter, "brand"),
                FiltersList.getSQLWhere<String>(filter, "country"),
                FiltersList.getSQLWhere<Long>(filter, "date"),
                FiltersList.getSQLWhere<String>(filter, "cigar"),
                FiltersList.getSQLWhere<Long>(filter, "gauge"),
                FiltersList.getSQLWhere<String>(filter, "length"),
                FiltersList.getSQLWhere<Long>(filter, "strength"),
                FiltersList.getSQLWhere<Boolean>(filter, "favorites"),
                sortBy, ::cigarFactory
            )
        }
    }

    override fun paging(
        sorting: FilterParameter<Boolean>?,
        filter: FiltersList?,
        vararg args: Pair<String, Any?>
    ): PagingSource<Int, Cigar> {
        val accenting = sorting?.value ?: true
        val sortKey = sorting?.key ?: "name"
        fun queryProvider(limit: Long, offset: Long): Query<Cigar> {
            Log.debug("Querying sort acs: $accenting filter:$filter sort:$sortKey offset->  from:$offset count:$limit")
            return if (accenting) {
                queries.pagedAsc(
                    FiltersList.getSQLWhere<String>(filter, "name"),
                    FiltersList.getSQLWhere<String>(filter, "brand"),
                    FiltersList.getSQLWhere<String>(filter, "country"),
                    FiltersList.getSQLWhere<Long>(filter, "date"),
                    FiltersList.getSQLWhere<String>(filter, "cigar"),
                    FiltersList.getSQLWhere<Long>(filter, "gauge"),
                    FiltersList.getSQLWhere<String>(filter, "length"),
                    FiltersList.getSQLWhere<Long>(filter, "strength"),
                    FiltersList.getSQLWhere<Boolean>(filter, "favorites"),
                    sortKey,
                    limit,
                    offset,
                    ::cigarFactory
                )
            } else {
                queries.pagedDesc(
                    FiltersList.getSQLWhere<String>(filter, "name"),
                    FiltersList.getSQLWhere<String>(filter, "brand"),
                    FiltersList.getSQLWhere<String>(filter, "country"),
                    FiltersList.getSQLWhere<Long>(filter, "date"),
                    FiltersList.getSQLWhere<String>(filter, "cigar"),
                    FiltersList.getSQLWhere<Long>(filter, "gauge"),
                    FiltersList.getSQLWhere<String>(filter, "length"),
                    FiltersList.getSQLWhere<Long>(filter, "strength"),
                    FiltersList.getSQLWhere<Boolean>(filter, "favorites"),
                    sortKey,
                    limit,
                    offset,
                    ::cigarFactory
                )
            }
        }
        return QueryPagingSource(
            count(*args),
            queries,
            Dispatchers.IO,
            ::queryProvider
        )
    }

    override fun find(rowid: Long, where: Long?): Query<Cigar> {
        return queries.find(rowid, ::cigarFactory)
    }

    override fun count(vararg args: Pair<String, Any?>): Query<Long> {
        val favorites = args.find { it.first == "favorites" }?.second as? Boolean ?: false
        return queries.count(favorites)
    }

    override fun contains(rowid: Long, where: Long?): Query<Long> {
        return queries.contains(rowid)
    }

    override fun lastInsertRowId(): ExecutableQuery<Long> {
        return queries.lastInsertRowId()
    }

    override suspend fun update(entity: Cigar) {
        queries.update(
            entity.name,
            entity.brand,
            entity.country,
            entity.date,
            entity.cigar,
            entity.wrapper,
            entity.binder,
            entity.gauge,
            entity.length,
            CigarStrength.toLong(entity.strength),
            entity.rating,
            entity.myrating,
            entity.notes,
            entity.filler,
            entity.link,
            entity.count,
            entity.shopping,
            entity.favorites,
            entity.price,
            entity.other,
            entity.relationsJson,
            entity.rowid
        )
    }

    override suspend fun add(entity: Cigar) {
        queries.add(
            entity.name,
            entity.brand,
            entity.country,
            entity.date,
            entity.cigar,
            entity.wrapper,
            entity.binder,
            entity.gauge,
            entity.length,
            CigarStrength.toLong(entity.strength),
            entity.rating,
            entity.myrating,
            entity.notes,
            entity.filler,
            entity.link,
            entity.count,
            entity.shopping,
            entity.favorites,
            entity.price,
            entity.other,
            entity.relationsJson
        )
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