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

class SqlDelightCigarHumidorRepository(
    private val cigarId: Long,
    private val roomQueries: CigarsDatabaseQueries,
) : BaseRepository<HumidorCigar>(), CigarHumidorRepository {

    fun allSync(): List<HumidorCigar> = roomQueries.cigarHumidors(cigarId, ::factory).executeAsList()

    override fun observeAll(): Flow<List<HumidorCigar>> {
        return roomQueries.cigarHumidors(cigarId, ::factory).asFlow().mapToList(Dispatchers.Main)
    }

    fun add(entity: Humidor, count: Long) {
        CoroutineScope(Dispatchers.Main).launch {
            roomQueries.addCigarToHumidor(entity.rowid,
                cigarId,
                count)
        }
    }

    override fun doUpsert(entity: HumidorCigar) {
        TODO("Not yet implemented")
    }

    override fun doDelete(id: Long) {
        CoroutineScope(Dispatchers.Main).launch {
            roomQueries.removeCigarFromHumidor(id, cigarId)
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

    private fun factory(
        count: Long?,
        humidorId: Long?,
        cigarId: Long?,
    ): HumidorCigar {
        val humidor = roomQueries.humidor(humidorId!!).executeAsOne()
        return HumidorCigar(
            count = count,
            humidor = Humidor(humidor),
            cigar = null,
        )
    }
}
