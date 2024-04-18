package com.akellolcc.cigars.databases.repository.impl

import com.akellolcc.cigars.databases.HistoryDatabaseQueries
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.History
import com.akellolcc.cigars.databases.repository.HistoryRepository
import com.akellolcc.cigars.databases.repository.impl.queries.HistoryTableQueries
import com.badoo.reaktive.coroutinesinterop.singleFromCoroutine
import com.badoo.reaktive.single.SingleWrapper
import com.badoo.reaktive.single.wrap

abstract class SqlDelightHistoryRepository(
    protected open var id: Long,
    protected val queries: HistoryDatabaseQueries
) : BaseRepository<History>(HistoryTableQueries(queries)), HistoryRepository {

    override fun doUpsert(entity: History, add: Boolean): SingleWrapper<History> {
        return singleFromCoroutine {
            if (add) {
                queries.add(
                    entity.count,
                    entity.date,
                    entity.left,
                    entity.price,
                    entity.type.type,
                    entity.cigarId,
                    entity.humidorFrom,
                    entity.humidorTo
                )
            } else {
                queries.update(
                    entity.count,
                    entity.date,
                    entity.left,
                    entity.price,
                    entity.type.type,
                    entity.cigarId,
                    entity.humidorFrom,
                    entity.humidorTo,
                    entity.rowid
                )
            }
            entity
        }.wrap()
    }

    override fun humidorName(id: Long): String = queries.humidorName(id).executeAsOne()

    override fun cigarName(id: Long): String = queries.cigarName(id).executeAsOne()

}
