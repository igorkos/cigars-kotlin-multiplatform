/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/24/24, 12:49 PM
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

import com.akellolcc.cigars.databases.CigarsDatabaseQueries
import com.akellolcc.cigars.databases.RepositoryFactory
import com.akellolcc.cigars.databases.createRepository
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.CigarStrength
import com.akellolcc.cigars.databases.extensions.History
import com.akellolcc.cigars.databases.extensions.HistoryType
import com.akellolcc.cigars.databases.extensions.Humidor
import com.akellolcc.cigars.databases.repository.CigarHistoryRepository
import com.akellolcc.cigars.databases.repository.CigarHumidorRepository
import com.akellolcc.cigars.databases.repository.CigarsRepository
import com.akellolcc.cigars.databases.repository.impl.queries.CigarsTableQueries
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.Clock

open class SqlDelightCigarsRepository(protected val queries: CigarsDatabaseQueries) :
    BaseRepository<Cigar>(CigarsTableQueries(queries)), CigarsRepository {

    //CigarsRepository
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun add(cigar: Cigar, humidor: Humidor): Flow<Cigar> {
        return super.add(cigar).flatMapConcat {
            val hcDatabase: CigarHumidorRepository = createRepository(CigarHumidorRepository::class)
            hcDatabase.add(it, humidor, cigar.count)
        }.flatMapConcat {
            val hisDatabase: CigarHistoryRepository =
                createRepository(CigarHistoryRepository::class, it.cigar.rowid)
            hisDatabase.add(
                History(
                    -1,
                    it.count,
                    Clock.System.now().toEpochMilliseconds(),
                    it.count,
                    cigar.price,
                    HistoryType.Addition,
                    it.cigar.rowid,
                    it.humidor.rowid,
                    null
                )
            )
        }.flatMapConcat {
            flowOf(cigar)
        }
    }

    override fun updateFavorite(value: Boolean, cigar: Cigar): Flow<Cigar> {
        return update(cigar.copy(favorites = value))
    }

    override fun updateRating(value: Long, cigar: Cigar): Flow<Cigar> {
        return update(cigar.copy(myrating = value))
    }

    override fun numberOfEntries(): Long {
        return queries.count().executeAsOne()
    }

    //BaseRepository
    override fun doUpsert(entity: Cigar, add: Boolean): Flow<Cigar> {
        return flow {
            if (add) {
                queries.add(
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
            } else {
                queries.update(
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
                    entity.favorites,
                    entity.rowid
                )
            }
            emit(entity)
        }
    }

    companion object Factory : RepositoryFactory<SqlDelightCigarsRepository>() {
        override fun factory(data: Any?): SqlDelightCigarsRepository {
            val queries = SqlDelightDatabase.instance.database.cigarsDatabaseQueries
            return SqlDelightCigarsRepository(queries)
        }
    }
}
