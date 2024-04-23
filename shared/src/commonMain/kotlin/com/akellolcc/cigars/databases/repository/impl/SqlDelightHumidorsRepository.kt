/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/23/24, 3:34 PM
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.akellolcc.cigars.databases.repository.impl

import com.akellolcc.cigars.databases.HumidorsDatabaseQueries
import com.akellolcc.cigars.databases.RepositoryFactory
import com.akellolcc.cigars.databases.createRepository
import com.akellolcc.cigars.databases.extensions.History
import com.akellolcc.cigars.databases.extensions.HistoryType
import com.akellolcc.cigars.databases.extensions.Humidor
import com.akellolcc.cigars.databases.repository.HumidorHistoryRepository
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
            val hisDatabase: HumidorHistoryRepository =
                createRepository(HumidorHistoryRepository::class, entity.rowid)
            hisDatabase.add(
                History(
                    -1,
                    1,
                    Clock.System.now().toEpochMilliseconds(),
                    1,
                    entity.price,
                    HistoryType.Addition,
                    -1,
                    it.rowid,
                    null
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

    override fun numberOfEntries(): Long {
        return queries.count().executeAsOne()
    }

    companion object Factory : RepositoryFactory<SqlDelightHumidorsRepository>() {
        override fun factory(data: Any?): SqlDelightHumidorsRepository {
            val queries = SqlDelightDatabase.instance.database.humidorsDatabaseQueries
            return SqlDelightHumidorsRepository(queries)
        }
    }
}
