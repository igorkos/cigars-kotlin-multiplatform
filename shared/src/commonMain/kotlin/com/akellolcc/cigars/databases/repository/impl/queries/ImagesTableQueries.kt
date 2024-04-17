/*
 * Copyright (c) 2024.
 */

package com.akellolcc.cigars.databases.repository.impl.queries

import app.cash.sqldelight.ExecutableQuery
import app.cash.sqldelight.Query
import app.cash.sqldelight.SuspendingTransactionWithReturn
import com.akellolcc.cigars.databases.ImagesDatabaseQueries
import com.akellolcc.cigars.databases.extensions.CigarImage
import com.akellolcc.cigars.databases.repository.DatabaseQueries

class ImagesTableQueries(override val queries: ImagesDatabaseQueries) :
    DatabaseQueries<CigarImage> {
    override fun get(id: Long, where: Long?): Query<CigarImage> {
        return queries.get(id, ::imageFactory)
    }

    override fun allAsc(sort: String): Query<CigarImage> {
        return queries.allAsc(::imageFactory)
    }

    override fun allDesc(sort: String): Query<CigarImage> {
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