/*
 * Copyright (c) 2024.
 */

package com.akellolcc.cigars.databases.repository.impl.queries

import app.cash.sqldelight.ExecutableQuery
import app.cash.sqldelight.Query
import app.cash.sqldelight.SuspendingTransactionWithReturn
import com.akellolcc.cigars.databases.HistoryDatabaseQueries
import com.akellolcc.cigars.databases.extensions.History
import com.akellolcc.cigars.databases.repository.DatabaseQueries

class HistoryTableQueries(override val queries: HistoryDatabaseQueries) : DatabaseQueries<History> {
    override fun get(id: Long, where: Long?): Query<History> {
        return queries.get(id, ::historyFactory)
    }

    override fun allAsc(sort: String): Query<History> {
        return queries.allAsc(::historyFactory)
    }

    override fun allDesc(sort: String): Query<History> {
        return queries.allDesc(::historyFactory)
    }

    override fun find(rowid: Long, where: Long?): Query<History> {
        return queries.find(rowid, ::historyFactory)
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