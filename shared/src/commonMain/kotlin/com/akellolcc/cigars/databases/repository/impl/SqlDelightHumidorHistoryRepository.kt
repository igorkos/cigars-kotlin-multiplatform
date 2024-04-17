package com.akellolcc.cigars.databases.repository.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.akellolcc.cigars.databases.HistoryDatabaseQueries
import com.akellolcc.cigars.databases.extensions.History
import com.akellolcc.cigars.databases.repository.impl.queries.historyFactory
import com.badoo.reaktive.coroutinesinterop.asObservable
import com.badoo.reaktive.observable.ObservableWrapper
import com.badoo.reaktive.observable.wrap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

class SqlDelightHumidorHistoryRepository(
    id: Long,
    queries: HistoryDatabaseQueries
) : SqlDelightHistoryRepository(id, queries) {

    override fun all(sortField: String?, accenting: Boolean): ObservableWrapper<List<History>> {
        return queries.humidorHistory(id, ::historyFactory).asFlow().mapToList(Dispatchers.IO)
            .asObservable().wrap()
    }
}
