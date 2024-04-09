package com.akellolcc.cigars.databases.repository.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import com.akellolcc.cigars.databases.CigarsDatabaseQueries
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.History
import com.akellolcc.cigars.databases.extensions.Humidor
import com.akellolcc.cigars.databases.repository.HistoryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

abstract class SqlDelightHistoryRepository(
    protected open val id: Long,
    queries: CigarsDatabaseQueries
) : BaseRepository<History>(queries), HistoryRepository {

    override suspend fun doUpsert(entity: History) {
        CoroutineScope(Dispatchers.Main).launch {
            queries.addHistory(
                entity.count,
                entity.date,
                entity.left,
                entity.price,
                entity.type.type,
                entity.cigarId,
                entity.humidorId
            )
            queries.lastInsertRowId().executeAsOne()
        }
    }

    override fun doDelete(id: Long) {}


    override suspend fun cigar(id: Long): Cigar {
        return queries.cigar(id, ::cigarFactory).asFlow().mapToOne(Dispatchers.Main).first()
    }

    override suspend fun humidor(id: Long): Humidor {
        return queries.humidor(id, ::humidorFactory).asFlow().mapToOne(Dispatchers.Main).first()
    }

    override fun humidorName(id: Long): String = queries.humidorName(id).executeAsOne()

    override fun cigarName(id: Long): String = queries.cigarName(id).executeAsOne()
    override fun observe(id: Long): Flow<History> {
        TODO("Not yet implemented")
    }

    override fun observeOrNull(id: Long): Flow<History?> {
        TODO("Not yet implemented")
    }


    override fun contains(id: Long): Boolean {
        return queries.historyExists(id).executeAsOne() != 0L
    }
}
