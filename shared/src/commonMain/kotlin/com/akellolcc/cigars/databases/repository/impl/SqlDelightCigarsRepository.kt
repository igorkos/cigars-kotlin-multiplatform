package com.akellolcc.cigars.databases.repository.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.CigarStrength
import com.akellolcc.cigars.databases.extensions.emptyCigar
import com.akellolcc.cigars.databases.repository.CigarsRepository
import dev.icerock.moko.mvvm.flow.cMutableStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SqlDelightCigarsRepository : BaseRepository<Cigar>(), CigarsRepository {
    override fun getSync(id: Long): Cigar {
        return roomQueries.cigar(id, ::cigarFactory).executeAsOne()
    }

    override fun observe(id: Long): Flow<Cigar> {
        if (id < 0) return MutableStateFlow(emptyCigar.copy()).cMutableStateFlow()
        return roomQueries.cigar(id, ::cigarFactory).asFlow().mapToOne(Dispatchers.Main)
    }

    override fun observeOrNull(id: Long): Flow<Cigar?> {
        if (id < 0) return MutableStateFlow(emptyCigar.copy()).cMutableStateFlow()
        return roomQueries.cigar(id, ::cigarFactory).asFlow().mapToOneOrNull(Dispatchers.Main)
    }

    override fun observeAll(): Flow<List<Cigar>> {
        return roomQueries.allCigars(::cigarFactory).asFlow().mapToList(Dispatchers.Main)
    }

    fun observeFavorites(): Flow<List<Cigar>> =
        roomQueries.favoriteCigars(::cigarFactory).asFlow().mapToList(Dispatchers.Main)

    fun observeHumidorCigars(): Flow<List<Cigar>> =
        roomQueries.favoriteCigars(::cigarFactory).asFlow().mapToList(Dispatchers.Main)

    override fun doUpsert(entity: Cigar) {
        CoroutineScope(Dispatchers.Main).launch {
            val id = roomQueries.transactionWithResult {
                roomQueries.addCigar(
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
                    entity.shopping,
                    entity.favorites
                )
                roomQueries.lastInsertRowId().executeAsOne()
            }
            entity.rowid = id
        }
    }

    override fun doDelete(id: Long) {
        CoroutineScope(Dispatchers.Main).launch {
            roomQueries.removeCigar(id)
        }
    }

    override fun contains(id: Long): Boolean {
        return roomQueries.cigarExists(id).executeAsOne() != 0L
    }
}
