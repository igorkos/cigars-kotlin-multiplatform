package com.akellolcc.cigars.databases.repository.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.akellolcc.cigars.databases.CigarsDatabaseQueries
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.Humidor
import com.akellolcc.cigars.databases.extensions.HumidorCigar
import com.akellolcc.cigars.databases.repository.CigarHumidorRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SqlDelightHumidorCigarsRepository(
    private val humidorId: Long,
    queries: CigarsDatabaseQueries
) : BaseRepository<HumidorCigar>(queries), CigarHumidorRepository {

    fun allSync(): List<HumidorCigar> =
        queries.humidorCigars(humidorId, ::humidorCigarFactory).executeAsList()

    override fun observeAll(sortField: String?, accenting: Boolean): Flow<List<HumidorCigar>> {
        return queries.humidorCigars(humidorId, ::humidorCigarFactory).asFlow()
            .mapToList(Dispatchers.Main)
    }

    fun add(entity: Cigar, count: Long) {
        CoroutineScope(Dispatchers.Main).launch {
            queries.addCigarToHumidor(humidorId, entity.rowid, count)
        }
    }

    override suspend fun doUpsert(entity: HumidorCigar) {
        TODO("Not yet implemented")
    }

    override fun doDelete(id: Long) {
        CoroutineScope(Dispatchers.Main).launch {
            queries.removeCigarFromHumidor(humidorId, id)
        }
    }

    override fun add(entity: Humidor, count: Long) {
        TODO("Not yet implemented")
    }

    override fun observe(id: Long): Flow<HumidorCigar> {
        TODO("Not yet implemented")
    }

    override fun observeOrNull(id: Long): Flow<HumidorCigar?> {
        TODO("Not yet implemented")
    }

    override fun contains(id: Long): Boolean {
        TODO("Not yet implemented")
    }

}
