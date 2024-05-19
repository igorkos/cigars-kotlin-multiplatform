/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/19/24, 1:15 PM
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
import com.akellolcc.cigars.databases.models.BaseEntity
import com.akellolcc.cigars.screens.components.search.data.FilterParameter
import com.akellolcc.cigars.screens.components.search.data.FiltersList

const val CIGAR_ID = "cigar_id"
const val HUMIDOR_ID = "humor_id"
const val FAVORITES_ID = "favorites"

interface DatabaseQueries<T : BaseEntity> {
    val queries: Any
    fun get(id: Long, where: Long? = null): Query<T>
    fun all(sorting: FilterParameter<Boolean>? = null, filter: FiltersList? = null, vararg args: Pair<String, Any?>): Query<T>

    fun paging(
        sorting: FilterParameter<Boolean>? = null,
        filter: FiltersList? = null,
        vararg args: Pair<String, Any?>
    ): PagingSource<Int, T>

    fun find(rowid: Long, where: Long? = null): Query<T>

    fun count(vararg args: Pair<String, Any?>): Query<Long>
    fun contains(rowid: Long, where: Long? = null): Query<Long>

    fun lastInsertRowId(): ExecutableQuery<Long>

    suspend fun add(entity: T)

    suspend fun update(entity: T)
    suspend fun remove(rowid: Long, where: Long? = null)

    suspend fun removeAll()

    fun empty(): T
    suspend fun <R> transactionWithResult(bodyWithReturn: suspend SuspendingTransactionWithReturn<R>.() -> R): R
}