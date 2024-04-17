package com.akellolcc.cigars.databases.repository.impl

import app.cash.sqldelight.Query
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.akellolcc.cigars.databases.CigarHumidorTable
import com.akellolcc.cigars.databases.Database
import com.akellolcc.cigars.databases.HumidorCigarsDatabaseQueries
import com.akellolcc.cigars.databases.RepositoryType
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.Humidor
import com.akellolcc.cigars.databases.extensions.HumidorCigar
import com.akellolcc.cigars.databases.repository.CigarHumidorRepository
import com.akellolcc.cigars.databases.repository.CigarsRepository
import com.akellolcc.cigars.databases.repository.HumidorsRepository
import com.akellolcc.cigars.databases.repository.Repository
import com.akellolcc.cigars.databases.repository.impl.queries.CigarHumidorTableQueries
import com.badoo.reaktive.coroutinesinterop.asObservable
import com.badoo.reaktive.observable.ObservableWrapper
import com.badoo.reaktive.observable.wrap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.map

abstract class SqlDelightBaseCigarHumidorRepository(
    protected val queries: HumidorCigarsDatabaseQueries
) : BaseRepository<HumidorCigar>(CigarHumidorTableQueries(queries)), CigarHumidorRepository {

    abstract fun observeAllQuery(): Query<CigarHumidorTable>

    override fun all(
        sortField: String?,
        accenting: Boolean
    ): ObservableWrapper<List<HumidorCigar>> {
        val hRepo = Database.instance.getRepository<HumidorsRepository>(RepositoryType.Humidors)
        val cRepo = Database.instance.getRepository<CigarsRepository>(RepositoryType.Cigars)
        return observeAllQuery().asFlow().mapToList(Dispatchers.IO).map {
            it.map { humidorCigar ->
                val humidor = hRepo.getSync(humidorCigar.humidorId)
                val cigar = (cRepo as Repository<Cigar>).getSync(humidorCigar.cigarId)
                HumidorCigar(humidorCigar.count, humidor, cigar)
            }
        }.asObservable().wrap()
    }

    override fun updateCount(cigar: Cigar, where: Humidor, count: Long) {
        super.update(HumidorCigar(count, where, cigar))
    }

    override suspend fun doUpsert(entity: HumidorCigar, add: Boolean) {
        if (add) {
            queries.add(
                entity.humidor.rowid,
                entity.cigar.rowid,
                entity.count
            )
        } else {
            queries.update(
                entity.count,
                entity.humidor.rowid,
                entity.cigar.rowid
            )
        }
    }

    override fun add(cigar: Cigar, humidor: Humidor, count: Long): ObservableWrapper<HumidorCigar> {
        return super.add(HumidorCigar(count, humidor, cigar))
    }

    override fun remove(cigar: Cigar, from: Humidor) {
        super.remove(from.rowid, cigar.rowid)
    }

    override fun find(cigar: Cigar, humidor: Humidor): HumidorCigar? {
        return super.find(humidor.rowid, cigar.rowid)
    }
}
