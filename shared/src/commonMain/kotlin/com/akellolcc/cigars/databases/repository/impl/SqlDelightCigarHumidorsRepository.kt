package com.akellolcc.cigars.databases.repository.impl

import app.cash.sqldelight.Query
import com.akellolcc.cigars.databases.CigarHumidorTable
import com.akellolcc.cigars.databases.HumidorCigarsDatabaseQueries
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.Humidor
import com.akellolcc.cigars.databases.extensions.HumidorCigar

class SqlDelightCigarHumidorsRepository(
    private val cigarId: Long,
    queries: HumidorCigarsDatabaseQueries
) : SqlDelightBaseCigarHumidorRepository(queries) {

    override fun observeAllQuery(): Query<CigarHumidorTable> {
        return queries.cigarHumidors(cigarId)
    }

}
