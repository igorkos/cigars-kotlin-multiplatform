package com.akellolcc.cigars.databases.repository.impl

import com.akellolcc.cigars.databases.Database
import com.akellolcc.cigars.databases.HumidorsDatabaseQueries
import com.akellolcc.cigars.databases.RepositoryType
import com.akellolcc.cigars.databases.extensions.History
import com.akellolcc.cigars.databases.extensions.HistoryType
import com.akellolcc.cigars.databases.extensions.Humidor
import com.akellolcc.cigars.databases.repository.HistoryRepository
import com.akellolcc.cigars.databases.repository.HumidorsRepository
import com.akellolcc.cigars.databases.repository.impl.queries.HumidorsTableQueries
import com.badoo.reaktive.coroutinesinterop.singleFromCoroutine
import com.badoo.reaktive.observable.ObservableWrapper
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.observable.wrap
import com.badoo.reaktive.single.SingleWrapper
import com.badoo.reaktive.single.wrap
import kotlinx.datetime.Clock

class SqlDelightHumidorsRepository(val queries: HumidorsDatabaseQueries) :
    BaseRepository<Humidor>(HumidorsTableQueries(queries)), HumidorsRepository {

    override fun add(entity: Humidor): ObservableWrapper<Humidor> {
        return super.add(entity).map {
            val hisDatabase: HistoryRepository =
                Database.instance.getRepository(RepositoryType.HumidorHistory, entity.rowid)
            hisDatabase.add(
                History(
                    -1,
                    1,
                    Clock.System.now().toEpochMilliseconds(),
                    1,
                    entity.price,
                    HistoryType.Addition,
                    -1,
                    it.rowid
                )
            )
            it
        }.wrap()
    }

    override fun doUpsert(entity: Humidor, add: Boolean): SingleWrapper<Humidor> {
        return singleFromCoroutine {
            if (add) {
                queries.add(
                    entity.name,
                    entity.brand,
                    entity.holds,
                    entity.count,
                    entity.temperature,
                    entity.humidity,
                    entity.notes,
                    entity.link,
                    entity.autoOpen,
                    entity.sorting,
                    entity.type
                )
            } else {
                queries.update(
                    entity.name,
                    entity.brand,
                    entity.holds,
                    entity.count,
                    entity.temperature,
                    entity.humidity,
                    entity.notes,
                    entity.link,
                    entity.autoOpen,
                    entity.sorting,
                    entity.type,
                    entity.rowid
                )
            }
            entity
        }.wrap()
    }
}
