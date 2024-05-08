/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/28/24, 11:56 PM
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
import com.akellolcc.cigars.databases.extensions.BaseEntity
import com.akellolcc.cigars.screens.components.search.data.FilterParameter

interface DatabaseQueries<T : BaseEntity> {
    val queries: Any
    fun get(id: Long, where: Long? = null): Query<T>
    fun allAsc(sortBy: String, filter: List<FilterParameter<*>>?): Query<T>

    fun allDesc(sortBy: String, filter: List<FilterParameter<*>>?): Query<T>

    fun find(rowid: Long, where: Long? = null): Query<T>

    fun count(): Query<Long>
    fun contains(rowid: Long, where: Long? = null): Query<Long>
    fun lastInsertRowId(): ExecutableQuery<Long>

    suspend fun remove(rowid: Long, where: Long? = null)

    suspend fun removeAll()

    fun empty(): T
    suspend fun <R> transactionWithResult(
        bodyWithReturn: suspend SuspendingTransactionWithReturn<R>.() -> R,
    ): R
}