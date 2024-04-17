/*
 * Copyright (c) 2024.
 */

package com.akellolcc.cigars.databases.repository

import app.cash.sqldelight.ExecutableQuery
import app.cash.sqldelight.Query
import app.cash.sqldelight.SuspendingTransactionWithReturn
import com.akellolcc.cigars.databases.extensions.BaseEntity
import com.badoo.reaktive.observable.ObservableWrapper

interface Repository<ENTITY : BaseEntity> {
    /**
     * Get the entity from the database.
     */
    fun getSync(id: Long, where: Long? = null): ENTITY

    fun allSync(sortField: String? = null, accenting: Boolean = true): List<ENTITY>

    fun observe(id: Long): ObservableWrapper<ENTITY>

    fun all(sortField: String? = null, accenting: Boolean = true): ObservableWrapper<List<ENTITY>>

    fun add(entity: ENTITY): ObservableWrapper<ENTITY>

    fun update(entity: ENTITY): ObservableWrapper<ENTITY>

    fun remove(id: Long, where: Long? = null): ObservableWrapper<Boolean>

    fun removeAll()

    fun find(id: Long, where: Long? = null): ENTITY?

    fun contains(id: Long, where: Long? = null): Boolean

    fun count(): Long

    fun lastInsertRowId(): Long
}

interface DatabaseQueries<T : BaseEntity> {
    val queries: Any
    fun get(id: Long, where: Long? = null): Query<T>
    fun allAsc(sort: String): Query<T>

    fun allDesc(sort: String): Query<T>

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
