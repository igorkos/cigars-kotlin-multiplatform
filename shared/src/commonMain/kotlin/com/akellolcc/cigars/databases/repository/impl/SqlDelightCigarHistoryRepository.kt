package com.akellolcc.cigars.databases.repository.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.History
import com.akellolcc.cigars.databases.extensions.Humidor
import com.akellolcc.cigars.databases.repository.HistoryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SqlDelightCigarHistoryRepository(
    private val id: Long
) : BaseRepository<History>(), HistoryRepository {

    fun allSync(): List<History> = roomQueries.cigarHistory(id, ::historyFactory).executeAsList()

    override fun observeAll(): Flow<List<History>> {
        return roomQueries.cigarHistory(id, ::historyFactory).asFlow().mapToList(Dispatchers.IO)
    }

    override fun doUpsert(entity: History) {
        CoroutineScope(Dispatchers.Main).launch {
            roomQueries.addHistory(
                entity.count,
                entity.date,
                entity.left,
                entity.price,
                entity.type.type,
                entity.cigarId,
                entity.humidorId
            )
            roomQueries.lastInsertRowId().executeAsOne()
        }
    }

    override fun doDelete(id: Long) {}


    override suspend fun cigar(id: Long): Cigar {
        return roomQueries.cigar(id, ::cigarFactory).asFlow().mapToOne(Dispatchers.Main).first()
    }

    override suspend fun humidor(id: Long): Humidor {
        return roomQueries.humidor(id, ::humidorFactory).asFlow().mapToOne(Dispatchers.Main).first()
    }

    fun humidorName(id: Long): String = roomQueries.humidorName(id).executeAsOne()
    override fun observe(id: Long): Flow<History> {
        TODO("Not yet implemented")
    }

    override fun observeOrNull(id: Long): Flow<History?> {
        TODO("Not yet implemented")
    }


    override fun contains(id: Long): Boolean {
        return roomQueries.historyExists(id).executeAsOne() != 0L
    }
}
