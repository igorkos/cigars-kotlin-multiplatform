package com.akellolcc.cigars.databases.repository.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.akellolcc.cigars.databases.CigarsDatabaseQueries
import com.akellolcc.cigars.databases.extensions.History
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow

class SqlDelightHumidorHistoryRepository(
    id: Long,
    queries: CigarsDatabaseQueries
) : SqlDelightHistoryRepository(id, queries) {

    override fun observeAll(sortField: String?, accenting: Boolean): Flow<List<History>> {
        return queries.humidorHistory(id, ::historyFactory).asFlow().mapToList(Dispatchers.IO)
    }
}
