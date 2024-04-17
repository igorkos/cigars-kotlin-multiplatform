/*
 * Copyright (c) 2024.
 */

package com.akellolcc.cigars.databases.repository.impl.queries

import app.cash.sqldelight.ExecutableQuery
import app.cash.sqldelight.Query
import app.cash.sqldelight.SuspendingTransactionWithReturn
import com.akellolcc.cigars.databases.HumidorCigarsDatabaseQueries
import com.akellolcc.cigars.databases.extensions.HumidorCigar
import com.akellolcc.cigars.databases.repository.DatabaseQueries

class CigarHumidorTableQueries(override val queries: HumidorCigarsDatabaseQueries) :
    DatabaseQueries<HumidorCigar> {
    override fun get(id: Long, where: Long?): Query<HumidorCigar> {
        return queries.get(id, where!!, ::humidorCigarFactory)
    }

    override fun allAsc(sort: String): Query<HumidorCigar> {
        return queries.allAsc(::humidorCigarFactory)
    }

    override fun allDesc(sort: String): Query<HumidorCigar> {
        return queries.allDesc(::humidorCigarFactory)
    }

    override fun find(rowid: Long, where: Long?): Query<HumidorCigar> {
        return queries.find(rowid, where!!, ::humidorCigarFactory)
    }

    override fun count(): Query<Long> {
        return queries.count()
    }

    override fun contains(rowid: Long, where: Long?): Query<Long> {
        return queries.contains(rowid, where!!)
    }

    override fun lastInsertRowId(): ExecutableQuery<Long> {
        return queries.lastInsertRowId()
    }

    override suspend fun remove(rowid: Long, where: Long?) {
        return queries.remove(rowid, where!!)
    }

    override suspend fun removeAll() {
        return queries.removeAll()
    }

    override fun empty(): HumidorCigar {
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