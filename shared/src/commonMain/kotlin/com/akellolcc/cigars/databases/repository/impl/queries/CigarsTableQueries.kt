/*
 * Copyright (c) 2024.
 */

package com.akellolcc.cigars.databases.repository.impl.queries

import app.cash.sqldelight.ExecutableQuery
import app.cash.sqldelight.Query
import app.cash.sqldelight.SuspendingTransactionWithReturn
import com.akellolcc.cigars.databases.CigarsDatabaseQueries
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.emptyCigar
import com.akellolcc.cigars.databases.repository.DatabaseQueries

class CigarsTableQueries(override val queries: CigarsDatabaseQueries) : DatabaseQueries<Cigar> {
    override fun get(id: Long, where: Long?): Query<Cigar> {
        return queries.get(id, ::cigarFactory)
    }

    override fun allAsc(sort: String): Query<Cigar> {
        return queries.allAsc(sort, ::cigarFactory)
    }

    override fun allDesc(sort: String): Query<Cigar> {
        return queries.allDesc(sort, ::cigarFactory)
    }

    override fun find(rowid: Long, where: Long?): Query<Cigar> {
        return queries.find(rowid, ::cigarFactory)
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