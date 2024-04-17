package com.akellolcc.cigars.databases.repository.impl

import com.akellolcc.cigars.databases.HistoryDatabaseQueries
import com.akellolcc.cigars.databases.extensions.History
import com.akellolcc.cigars.databases.repository.HistoryRepository
import com.akellolcc.cigars.databases.repository.impl.queries.HistoryTableQueries

abstract class SqlDelightHistoryRepository(
    protected open val id: Long,
    protected val queries: HistoryDatabaseQueries
) : BaseRepository<History>(HistoryTableQueries(queries)), HistoryRepository {

    override suspend fun doUpsert(entity: History, add: Boolean) {
        if (add) {
            queries.add(
                entity.count,
                entity.date,
                entity.left,
                entity.price,
                entity.type.type,
                entity.cigarId,
                entity.humidorId
            )
        } else {
            queries.update(
                entity.count,
                entity.date,
                entity.left,
                entity.price,
                entity.type.type,
                entity.cigarId,
                entity.humidorId,
                entity.rowid
            )
        }
    }

    override fun humidorName(id: Long): String = queries.humidorName(id).executeAsOne()

    override fun cigarName(id: Long): String = queries.cigarName(id).executeAsOne()

}
