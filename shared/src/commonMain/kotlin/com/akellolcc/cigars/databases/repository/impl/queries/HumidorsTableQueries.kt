/*
 * Copyright (c) 2024.
 */

package com.akellolcc.cigars.databases.repository.impl.queries

import app.cash.sqldelight.ExecutableQuery
import app.cash.sqldelight.Query
import app.cash.sqldelight.SuspendingTransactionWithReturn
import com.akellolcc.cigars.databases.HumidorsDatabaseQueries
import com.akellolcc.cigars.databases.extensions.Humidor
import com.akellolcc.cigars.databases.repository.DatabaseQueries

class HumidorsTableQueries(override val queries: HumidorsDatabaseQueries) :
    DatabaseQueries<Humidor> {
    override fun get(id: Long, where: Long?): Query<Humidor> {
        return queries.get(id, ::humidorFactory)
    }

    override fun allAsc(sort: String): Query<Humidor> {
        return queries.allAsc(sort, ::humidorFactory)
    }

    override fun allDesc(sort: String): Query<Humidor> {
        return queries.allDesc(sort, ::humidorFactory)
    }

    override fun find(rowid: Long, where: Long?): Query<Humidor> {
        return queries.find(rowid, ::humidorFactory)
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

    override fun empty(): Humidor {
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