package com.akellolcc.cigars.databases.repository.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.HumidorCigar
import com.akellolcc.cigars.databases.repository.CigarHumidorRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SqlDelightHumidorCigarsRepository(
    private val humidorId: Long
) : BaseRepository<HumidorCigar>(), CigarHumidorRepository {

    fun allSync(): List<HumidorCigar> =
        roomQueries.humidorCigars(humidorId, ::humidorCigarFactory).executeAsList()

    override fun observeAll(): Flow<List<HumidorCigar>> {
        return roomQueries.humidorCigars(humidorId, ::humidorCigarFactory).asFlow()
            .mapToList(Dispatchers.Main)
    }

    fun add(entity: Cigar, count: Long) {
        CoroutineScope(Dispatchers.Main).launch {
            roomQueries.addCigarToHumidor(humidorId, entity.rowid, count)
        }
    }

    override fun doUpsert(entity: HumidorCigar) {
        TODO("Not yet implemented")
    }

    override fun doDelete(id: Long) {
        CoroutineScope(Dispatchers.Main).launch {
            roomQueries.removeCigarFromHumidor(humidorId, id)
        }
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
