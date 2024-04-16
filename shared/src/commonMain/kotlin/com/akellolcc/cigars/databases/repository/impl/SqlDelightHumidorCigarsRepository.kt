package com.akellolcc.cigars.databases.repository.impl

import app.cash.sqldelight.Query
import com.akellolcc.cigars.databases.CigarHumidorTable
import com.akellolcc.cigars.databases.CigarsDatabaseQueries

class SqlDelightHumidorCigarsRepository(
    private val humidorId: Long,
    queries: CigarsDatabaseQueries
) : SqlDelightBaseCigarHumidorRepository(queries) {

    override fun observeAllQuery(): Query<CigarHumidorTable> {
        return queries.humidorCigars(humidorId)
    }
}
