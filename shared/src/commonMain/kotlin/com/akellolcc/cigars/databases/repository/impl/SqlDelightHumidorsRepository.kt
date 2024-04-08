package com.akellolcc.cigars.databases.repository.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.akellolcc.cigars.databases.CigarsDatabaseQueries
import com.akellolcc.cigars.databases.extensions.Humidor
import com.akellolcc.cigars.databases.repository.HumidorsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

class SqlDelightHumidorsRepository(queries: CigarsDatabaseQueries) : BaseRepository<Humidor>(queries), HumidorsRepository {

    fun allSync(): List<Humidor> = queries.allHumidors(::humidorFactory).executeAsList()

    override fun getSync(id: Long): Humidor {
        return queries.humidor(id, ::humidorFactory).executeAsOne()
    }

    override fun observe(id: Long): Flow<Humidor> {
        return queries.humidor(id, ::humidorFactory).asFlow().mapToOne(Dispatchers.Main)
    }

    override fun observeOrNull(id: Long): Flow<Humidor?> {
        return queries.humidor(id, ::humidorFactory).asFlow().mapToOneOrNull(Dispatchers.Main)
    }

    override fun observeAll(): Flow<List<Humidor>> {
        return queries.allHumidors(::humidorFactory).asFlow().mapToList(Dispatchers.Main)
    }

    override fun add(entity: Humidor) {
        super.add(entity)
        CoroutineScope(Dispatchers.Main).launch {
            queries.addHistory(1,  Clock.System.now().toEpochMilliseconds(), 1, entity.price, 0, -1, entity.rowid)
        }
    }
    override fun doUpsert(entity: Humidor) {
        CoroutineScope(Dispatchers.Main).launch {
            val id = queries.transactionWithResult {
                queries.addHumidor(
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
                queries.lastInsertRowId().executeAsOne()
            }
            entity.rowid = id
        }
    }

    override fun doDelete(id: Long) {
        CoroutineScope(Dispatchers.Main).launch {
            queries.removeCigar(id)
        }
    }

    override fun contains(id: Long): Boolean {
        return queries.cigarExists(id).executeAsOne() != 0L
    }

}
