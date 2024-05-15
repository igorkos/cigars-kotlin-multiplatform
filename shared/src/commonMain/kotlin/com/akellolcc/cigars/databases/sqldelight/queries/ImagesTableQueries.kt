/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/29/24, 12:01 AM
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
import com.akellolcc.cigars.databases.ImagesDatabaseQueries
import com.akellolcc.cigars.databases.models.CigarImage
import com.akellolcc.cigars.screens.components.search.data.FilterParameter

class ImagesTableQueries(override val queries: ImagesDatabaseQueries) :
    DatabaseQueries<CigarImage> {
    override fun get(id: Long, where: Long?): Query<CigarImage> {
        return queries.get(id, ::imageFactory)
    }

    override fun allAsc(sortBy: String, filter: List<FilterParameter<*>>?): Query<CigarImage> {
        return queries.allAsc(::imageFactory)
    }

    override fun allDesc(sortBy: String, filter: List<FilterParameter<*>>?): Query<CigarImage> {
        return queries.allDesc(::imageFactory)
    }

    override fun find(rowid: Long, where: Long?): Query<CigarImage> {
        return queries.find(rowid, ::imageFactory)
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