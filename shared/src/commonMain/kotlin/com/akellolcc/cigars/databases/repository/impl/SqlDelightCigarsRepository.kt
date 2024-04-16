package com.akellolcc.cigars.databases.repository.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.akellolcc.cigars.databases.CigarsDatabaseQueries
import com.akellolcc.cigars.databases.Database
import com.akellolcc.cigars.databases.RepositoryType
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.CigarStrength
import com.akellolcc.cigars.databases.extensions.History
import com.akellolcc.cigars.databases.extensions.HistoryType
import com.akellolcc.cigars.databases.extensions.Humidor
import com.akellolcc.cigars.databases.extensions.emptyCigar
import com.akellolcc.cigars.databases.repository.CigarHumidorRepository
import com.akellolcc.cigars.databases.repository.CigarsRepository
import com.akellolcc.cigars.databases.repository.HistoryRepository
import com.badoo.reaktive.observable.ObservableWrapper
import com.badoo.reaktive.observable.flatMap
import com.badoo.reaktive.observable.observable
import com.badoo.reaktive.observable.wrap
import dev.icerock.moko.mvvm.flow.cMutableStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

open class SqlDelightCigarsRepository(queries: CigarsDatabaseQueries) :
    BaseRepository<Cigar>(queries), CigarsRepository {
    override fun add(cigar: Cigar, humidor: Humidor): ObservableWrapper<Cigar> {
        return super.add(cigar).flatMap {
            val hcDatabase: CigarHumidorRepository =
                Database.instance.getRepository(RepositoryType.CigarHumidors, humidor.rowid)
            hcDatabase.add(it, humidor, cigar.count)
        }.flatMap {
            val hisDatabase: HistoryRepository =
                Database.instance.getRepository(RepositoryType.CigarHistory, it.cigar.rowid)
            hisDatabase.add(
                History(
                    -1,
                    it.count,
                    Clock.System.now().toEpochMilliseconds(),
                    it.count,
                    cigar.price,
                    HistoryType.Addition,
                    it.cigar.rowid,
                    it.humidor.rowid
                )
            )
        }.flatMap {
            observable {
                it.onNext(cigar)
            }
        }.wrap()
    }

    override fun getSync(id: Long): Cigar {
        return queries.cigar(id, ::cigarFactory).executeAsOne()
    }

    override fun observe(id: Long): Flow<Cigar> {
        if (id < 0) return MutableStateFlow(emptyCigar.copy()).cMutableStateFlow()
        return queries.cigar(id, ::cigarFactory).asFlow().mapToOne(Dispatchers.IO)
    }

    override fun observeOrNull(id: Long): Flow<Cigar?> {
        if (id < 0) return MutableStateFlow(emptyCigar.copy()).cMutableStateFlow()
        return queries.cigar(id, ::cigarFactory).asFlow().mapToOneOrNull(Dispatchers.IO)
    }

    override fun observeAll(sortField: String?, accenting: Boolean): Flow<List<Cigar>> {
        return (
                if (accenting)
                    queries.allCigarsAsc(sortField ?: "name", ::cigarFactory)
                else
                    queries.allCigarsDesc(sortField ?: "name", ::cigarFactory)
                ).asFlow().mapToList(Dispatchers.IO)
    }


    override suspend fun doUpsert(entity: Cigar, add: Boolean) {
        queries.addCigar(
            entity.rowid,
            entity.name,
            entity.brand,
            entity.country,
            entity.date,
            entity.cigar,
            entity.wrapper,
            entity.binder,
            entity.gauge,
            entity.length,
            CigarStrength.toLong(entity.strength),
            entity.rating,
            entity.myrating,
            entity.notes,
            entity.filler,
            entity.link,
            entity.count,
            entity.shopping,
            entity.favorites
        )
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
