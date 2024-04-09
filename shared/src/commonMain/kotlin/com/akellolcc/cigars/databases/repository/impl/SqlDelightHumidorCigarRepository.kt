package com.akellolcc.cigars.databases.repository.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.akellolcc.cigars.databases.CigarsDatabaseQueries
import com.akellolcc.cigars.databases.extensions.Humidor
import com.akellolcc.cigars.databases.extensions.HumidorCigar
import com.akellolcc.cigars.databases.repository.CigarHumidorRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SqlDelightCigarHumidorsRepository(
    private val cigarId: Long,
    queries: CigarsDatabaseQueries
) : BaseRepository<HumidorCigar>(queries), CigarHumidorRepository {

    fun allSync(): List<HumidorCigar> =
        queries.cigarHumidors(cigarId, ::humidorCigarFactory).executeAsList()

    override fun observeAll(): Flow<List<HumidorCigar>> {
        return queries.cigarHumidors(cigarId, ::humidorCigarFactory).asFlow()
            .mapToList(Dispatchers.Main)
    }

    override fun add(entity: Humidor, count: Long) {
        CoroutineScope(Dispatchers.Main).launch {
            queries.addCigarToHumidor(
                entity.rowid,
                cigarId,
                count
            )
        }
    }

    override suspend fun doUpsert(entity: HumidorCigar) {
        queries.addCigarToHumidor(
            entity.humidor!!.rowid,
            entity.cigar!!.rowid,
            entity.count
        )
    }

    override fun doDelete(id: Long) {
        CoroutineScope(Dispatchers.Main).launch {
            queries.removeCigarFromHumidor(id, cigarId)
        }
    }

    override fun observe(id: Long): Flow<HumidorCigar> {
        TODO("Not yet implemented")
    }

    override fun observeOrNull(id: Long): Flow<HumidorCigar?> {
        TODO("Not yet implemented")
    }

    override fun update(entity: HumidorCigar) {
        entity.humidor?.let {
            super.update(entity)
        }
    }

    override fun contains(id: Long): Boolean {
        return queries.humidorCigarExists(cigarId, id).executeAsOne() != 0L
    }

}
