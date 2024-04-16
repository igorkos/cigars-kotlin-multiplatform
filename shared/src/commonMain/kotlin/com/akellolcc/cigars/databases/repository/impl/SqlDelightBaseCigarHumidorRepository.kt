package com.akellolcc.cigars.databases.repository.impl

import app.cash.sqldelight.Query
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.akellolcc.cigars.databases.CigarHumidorTable
import com.akellolcc.cigars.databases.CigarsDatabaseQueries
import com.akellolcc.cigars.databases.Database
import com.akellolcc.cigars.databases.RepositoryType
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.Humidor
import com.akellolcc.cigars.databases.extensions.HumidorCigar
import com.akellolcc.cigars.databases.repository.CigarHumidorRepository
import com.akellolcc.cigars.databases.repository.CigarsRepository
import com.akellolcc.cigars.databases.repository.HumidorsRepository
import com.badoo.reaktive.observable.ObservableWrapper
import com.badoo.reaktive.observable.observable
import com.badoo.reaktive.observable.wrap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

abstract class SqlDelightBaseCigarHumidorRepository(
    queries: CigarsDatabaseQueries
) : BaseRepository<HumidorCigar>(queries), CigarHumidorRepository {

    override fun observeAll(sortField: String?, accenting: Boolean): Flow<List<HumidorCigar>> {
        val hRepo = Database.instance.getRepository<HumidorsRepository>(RepositoryType.Humidors)
        val cRepo = Database.instance.getRepository<CigarsRepository>(RepositoryType.Cigars)
        return observeAllQuery().asFlow()
            .mapToList(Dispatchers.Main).map {
                it.map { humidorCigar ->
                    val humidor = hRepo.getSync(humidorCigar.humidorId)
                    val cigar = cRepo.getSync(humidorCigar.cigarId)
                    HumidorCigar(humidorCigar.count, humidor, cigar)
                }
            }
    }

    protected abstract fun observeAllQuery(): Query<CigarHumidorTable>

    override fun add(cigar: Cigar, humidor: Humidor, count: Long): ObservableWrapper<HumidorCigar> {
        return observable { emitter ->
            CoroutineScope(Dispatchers.IO).launch {
                val humidorCigar = HumidorCigar(count, humidor, cigar)
                doUpsert(humidorCigar, true)
                emitter.onNext(humidorCigar)
                emitter.onComplete()
            }
        }.wrap()
    }

    override fun updateCount(cigar: Cigar, where: Humidor, count: Long) {
        super.update(HumidorCigar(count, where, cigar))
    }

    override suspend fun doUpsert(entity: HumidorCigar, add: Boolean) {
        if (add) {
            queries.addCigarToHumidor(
                entity.humidor!!.rowid,
                entity.cigar!!.rowid,
                entity.count
            )
        } else {
            queries.updateCigarToHumidorCount(
                entity.count,
                entity.humidor!!.rowid,
                entity.cigar!!.rowid
            )
        }
    }

    override fun find(cigar: Cigar, humidor: Humidor): HumidorCigar? {
        return queries.findHumidorCigar(cigar.rowid, humidor.rowid).executeAsOneOrNull()?.let {
            HumidorCigar(it.count, humidor, cigar)
        }
    }

    override fun remove(cigar: Cigar, from: Humidor) {
        CoroutineScope(Dispatchers.IO).launch {
            queries.removeCigarFromHumidor(cigar.rowid, from.rowid)
        }
    }

    override fun doDelete(id: Long) {

    }

    override fun add(entity: HumidorCigar): ObservableWrapper<HumidorCigar> {
        TODO("Not yet implemented")
    }

    override fun remove(entity: HumidorCigar): Boolean {
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
