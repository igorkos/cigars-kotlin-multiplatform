package com.akellolcc.cigars.databases.repository.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.akellolcc.cigars.databases.extensions.Humidor
import com.akellolcc.cigars.databases.repository.HumidorsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SqlDelightHumidorsRepository() : BaseRepository<Humidor>(), HumidorsRepository {

    fun allSync(): List<Humidor> = roomQueries.allHumidors(::humidorFactory).executeAsList()

    override fun observe(id: Long): Flow<Humidor> {
        return roomQueries.humidor(id, ::humidorFactory).asFlow().mapToOne(Dispatchers.Main)
    }

    override fun observeOrNull(id: Long): Flow<Humidor?> {
        return roomQueries.humidor(id, ::humidorFactory).asFlow().mapToOneOrNull(Dispatchers.Main)
    }

    override fun observeAll(): Flow<List<Humidor>> {
        return roomQueries.allHumidors(::humidorFactory).asFlow().mapToList(Dispatchers.Main)
    }

    override fun doUpsert(entity: Humidor) {
        CoroutineScope(Dispatchers.Main).launch {
            val id = roomQueries.transactionWithResult {
                roomQueries.addHumidor(
                    entity.rowid,
                    entity.name,
                    entity.brand,
                    entity.holds,
                    entity.count,
                    entity.temperature,
                    entity.humidity,
                    entity.notes,
                    entity.link,
                    entity.autoOpen,
                    entity.sorting,
                    entity.type
                )
                roomQueries.lastInsertRowId().executeAsOne()
            }
            entity.rowid = id
        }
    }

    override fun doDelete(id: Long) {
        CoroutineScope(Dispatchers.Main).launch {
            roomQueries.removeCigar(id)
        }
    }

    override fun contains(id: Long): Boolean {
        return roomQueries.cigarExists(id).executeAsOne() != 0L
    }

}
