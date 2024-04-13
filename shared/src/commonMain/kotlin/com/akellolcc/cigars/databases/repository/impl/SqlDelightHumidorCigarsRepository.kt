package com.akellolcc.cigars.databases.repository.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.akellolcc.cigars.databases.CigarsDatabaseQueries
import com.akellolcc.cigars.databases.Database
import com.akellolcc.cigars.databases.RepositoryType
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.Humidor
import com.akellolcc.cigars.databases.extensions.HumidorCigar
import com.akellolcc.cigars.databases.repository.CigarHumidorRepository
import com.akellolcc.cigars.databases.repository.CigarsRepository
import com.akellolcc.cigars.databases.repository.HumidorsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SqlDelightHumidorCigarsRepository(
    private val humidorId: Long,
    queries: CigarsDatabaseQueries
) : BaseRepository<HumidorCigar>(queries), CigarHumidorRepository {

    override fun observeAll(sortField: String?, accenting: Boolean): Flow<List<HumidorCigar>> {
        val hRepo = Database.getInstance().getRepository<HumidorsRepository>(RepositoryType.Humidors)
        val cRepo = Database.getInstance().getRepository<CigarsRepository>(RepositoryType.Cigars)
        return queries.humidorCigars(humidorId).asFlow()
            .mapToList(Dispatchers.Main).map {
                it.map { humidorCigar ->
                    val humidor = hRepo.getSync(humidorCigar.humidorId)
                    val cigar = cRepo.getSync(humidorCigar.cigarId)
                    HumidorCigar(humidorCigar.count, humidor, cigar )
                }
            }
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

    override fun find(cigar: Cigar, humidor: Humidor): HumidorCigar? {
        return queries.findHumidorCigar(cigar.rowid, humidor.rowid).executeAsOneOrNull()?.let {
            HumidorCigar(it.count, humidor, cigar)
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
