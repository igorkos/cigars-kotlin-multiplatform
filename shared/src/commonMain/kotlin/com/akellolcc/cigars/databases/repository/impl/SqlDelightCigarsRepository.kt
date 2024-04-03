package com.akellolcc.cigars.databases.repository.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.akellolcc.cigars.databases.CigarsDatabaseQueries
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.repository.CigarsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SqlDelightCigarsRepository(
    private val roomQueries: CigarsDatabaseQueries,
) : BaseRepository<Cigar>(), CigarsRepository {

    fun allSync(): List<Cigar> = roomQueries.allCigars(::cigarFactory).executeAsList()

    override fun observe(id: Long): Flow<Cigar> {
        return roomQueries.cigar(id, ::cigarFactory).asFlow().mapToOne(Dispatchers.Main)
    }

    override fun observeOrNull(id: Long): Flow<Cigar?> {
        return roomQueries.cigar(id, ::cigarFactory).asFlow().mapToOneOrNull(Dispatchers.Main)
    }

    override fun observeAll(): Flow<List<Cigar>> {
        return roomQueries.allCigars(::cigarFactory).asFlow().mapToList(Dispatchers.Main)
    }

    override fun doUpsert(entity: Cigar) {
        CoroutineScope(Dispatchers.Main).launch {
            val id = roomQueries.transactionWithResult {
                roomQueries.addCigar(entity.rowid,
                    entity.name,
                    entity.brand,
                    entity.country,
                    entity.date,
                    entity.cigar,
                    entity.wrapper,
                    entity.binder,
                    entity.gauge,
                    entity.length,
                    entity.strength,
                    entity.rating,
                    entity.myrating,
                    entity.notes,
                    entity.filler,
                    entity.link,
                    entity.shopping,
                    entity.favorites)
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

    private fun cigarFactory(
        rowid: Long,
        name: String,
        brand: String?,
        country: String?,
        date: Long?,
        cigar: String,
        wrapper: String,
        binder: String,
        gauge: Long,
        length: String,
        strength: Long,
        rating: Long?,
        myrating: Long?,
        notes: String?,
        filler: String,
        link: String?,
        shopping: Boolean,
        favorites: Boolean,
    ): Cigar {
        return Cigar(
            rowid,
            name,
            brand,
            country,
            date,
            cigar,
            wrapper,
            binder,
            gauge,
            length,
            strength,
            rating,
            myrating,
            notes,
            filler,
            link,
            shopping,
            favorites
        )
    }
}
