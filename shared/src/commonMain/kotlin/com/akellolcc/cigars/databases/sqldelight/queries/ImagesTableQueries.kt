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

import app.cash.paging.PagingSource
import app.cash.sqldelight.ExecutableQuery
import app.cash.sqldelight.Query
import app.cash.sqldelight.SuspendingTransactionWithReturn
import app.cash.sqldelight.paging3.QueryPagingSource
import com.akellolcc.cigars.databases.ImagesDatabaseQueries
import com.akellolcc.cigars.databases.models.CigarImage
import com.akellolcc.cigars.screens.components.search.data.FilterParameter
import com.akellolcc.cigars.screens.components.search.data.FiltersList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

class ImagesTableQueries(override val queries: ImagesDatabaseQueries) :
    DatabaseQueries<CigarImage> {
    override fun get(id: Long, where: Long?): Query<CigarImage> {
        return queries.get(id, ::imageFactory)
    }

    override fun all(
        sorting: FilterParameter<Boolean>?,
        filter: FiltersList?,
        vararg args: Pair<String, Any?>
    ): Query<CigarImage> {
        args.find { it.first == CIGAR_ID }?.let {
            return queries.cigarImages(it.second as Long, ::imageFactory)
        }
        args.find { it.first == HUMIDOR_ID }?.let {
            return queries.humidorImages(
                it.second as Long,
                ::imageFactory
            )
        }
        throw IllegalArgumentException("Unknown argument")
    }


    override fun paging(
        sorting: FilterParameter<Boolean>?,
        filter: FiltersList?,
        vararg args: Pair<String, Any?>
    ): PagingSource<Int, CigarImage> {
        fun queryProvider(limit: Long, offset: Long): Query<CigarImage> {
            args.find { it.first == CIGAR_ID }?.let {
                return queries.pagedCigarImages(
                    it.second as Long,
                    limit,
                    offset,
                    ::imageFactory
                )
            }
            args.find { it.first == HUMIDOR_ID }?.let {
                return queries.pagedHumidorImages(
                    it.second as Long,
                    limit,
                    offset,
                    ::imageFactory
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

    override fun find(rowid: Long, where: Long?): Query<CigarImage> {
        return queries.find(rowid, ::imageFactory)
    }

    override fun count(vararg args: Pair<String, Any?>): Query<Long> {
        args.find { it.first == CIGAR_ID }?.let {
            return queries.cigarImagesCount(it.second as Long)
        }
        args.find { it.first == HUMIDOR_ID }?.let {
            return queries.humidorImagesCount(it.second as Long)
        }
        throw IllegalArgumentException("Unknown argument")
    }

    override fun contains(rowid: Long, where: Long?): Query<Long> {
        return queries.contains(rowid)
    }

    override fun lastInsertRowId(): ExecutableQuery<Long> {
        return queries.lastInsertRowId()
    }

    override suspend fun update(entity: CigarImage) {
        queries.update(
            entity.bytes,
            entity.type,
            entity.image,
            entity.notes,
            entity.rowid
        )
    }

    override suspend fun add(entity: CigarImage) {
        queries.add(
            entity.bytes,
            entity.type,
            entity.image,
            entity.notes,
            entity.cigarId,
            entity.humidorId
        )
    }

    override suspend fun remove(rowid: Long, where: Long?) {
        return queries.remove(rowid)
    }

    override suspend fun removeAll() {
        return queries.removeAll()
    }

    override fun empty(): CigarImage {
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