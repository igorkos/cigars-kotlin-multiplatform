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
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

class SqlDelightHumidorsRepository(queries: CigarsDatabaseQueries) : BaseRepository<Humidor>(queries), HumidorsRepository {

    override fun allSync(sortField: String?, accenting: Boolean): List<Humidor> = if(accenting)
            queries.allHumidorsAsc(sortField ?:"name",::humidorFactory).executeAsList()
        else
            queries.allHumidorsDesc(sortField ?:"name",::humidorFactory).executeAsList()

    override fun getSync(id: Long): Humidor {
        return queries.humidor(id, ::humidorFactory).executeAsOne()
    }

    override fun observe(id: Long): Flow<Humidor> {
        return queries.humidor(id, ::humidorFactory).asFlow().mapToOne(Dispatchers.IO)
    }

    override fun observeOrNull(id: Long): Flow<Humidor?> {
        return queries.humidor(id, ::humidorFactory).asFlow().mapToOneOrNull(Dispatchers.IO)
    }

    override fun observeAll(sortField: String?, accenting: Boolean): Flow<List<Humidor>> {
        return (
                if(accenting)
                    queries.allHumidorsAsc(sortField ?:"name",::humidorFactory)
                else
                    queries.allHumidorsDesc(sortField ?:"name",::humidorFactory)
                ).asFlow().mapToList(Dispatchers.IO)
    }

    override fun add(entity: Humidor, callback: (suspend (Long) -> Unit)?) {
        super.add(entity) {
            queries.addHistory(1,  Clock.System.now().toEpochMilliseconds(), 1, entity.price, 0, -1, it)
            callback?.invoke(it)
        }
    }
    override suspend fun doUpsert(entity: Humidor) {
        if(!contains(entity.rowid)) {
            queries.addHumidor(
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
        } else {
            queries.updateHumidor(
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
                entity.type,
                entity.rowid
            )
        }
    }

    override fun doDelete(id: Long) {
        CoroutineScope(Dispatchers.Main).launch {
            queries.removeCigar(id)
        }
    }

    override fun count(): Long {
        return queries.humidorsCount().executeAsOne()
    }

    override fun contains(id: Long): Boolean {
        return queries.cigarExists(id).executeAsOne() != 0L
    }

}
