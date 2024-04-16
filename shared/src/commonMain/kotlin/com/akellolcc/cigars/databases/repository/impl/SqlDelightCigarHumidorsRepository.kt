package com.akellolcc.cigars.databases.repository.impl

import app.cash.sqldelight.Query
import com.akellolcc.cigars.databases.CigarHumidorTable
import com.akellolcc.cigars.databases.CigarsDatabaseQueries

class SqlDelightCigarHumidorsRepository(
    private val cigarId: Long,
    queries: CigarsDatabaseQueries
) : SqlDelightBaseCigarHumidorRepository(queries) {

    override fun observeAllQuery(): Query<CigarHumidorTable> {
        return queries.cigarHumidors(cigarId)
    }

}
