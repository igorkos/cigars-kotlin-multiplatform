/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/27/24, 2:24 PM
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
 ******************************************************************************************************************************************/

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
import com.akellolcc.cigars.databases.repository.CigarHumidorsRepository
import com.akellolcc.cigars.databases.repository.CigarsRepository
import com.akellolcc.cigars.databases.repository.HumidorsRepository
import com.akellolcc.cigars.databases.repository.impl.queries.CigarsTableQueries
import com.akellolcc.cigars.utils.collectFirst
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.Clock

open class SqlDelightCigarsRepository(protected val queries: CigarsDatabaseQueries) :
    BaseRepository<Cigar>(CigarsTableQueries(queries)), CigarsRepository {

    /**
     * Add a new cigar to the database
     * 1. Add to Cigars table
     * 2. Add to Cigar to Humidor (CigarHumidors table)
     * 3. Add to History entry (History table)
     * 4. Update cigars count in humidor
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun add(cigar: Cigar, humidor: Humidor): Flow<Cigar> {
        //Add to cigars table
        return super.add(cigar).flatMapConcat {
            //Add to Cigars to Humidor
            val hcRepository: CigarHumidorsRepository = createRepository(CigarHumidorsRepository::class, it.rowid)
            hcRepository.add(it, humidor, cigar.count)
        }.flatMapConcat {
            //Add to History entry
            val hisRepository: CigarHistoryRepository = createRepository(CigarHistoryRepository::class, it.cigar.rowid)
            hisRepository.add(
                History(
                    -1,
                    it.count,
                    Clock.System.now().toEpochMilliseconds(),
                    it.count,
                    cigar.price,
                    HistoryType.Addition,
                    it.cigar,
                    it.humidor,
                    it.humidor
                )
            )
        }.flatMapConcat {
            //Update cigars count in humidor
            val humidorsRepository: HumidorsRepository = createRepository(HumidorsRepository::class)
            val humidorTo = humidorsRepository.getSync(it.humidorTo.rowid)
            humidorsRepository.updateCigarsCount(it.humidorTo.rowid, humidorTo.count + it.count)
        }.flatMapConcat {
            flowOf(cigar)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun addAll(cigars: List<Cigar>, humidor: Humidor): Flow<List<Cigar>> {
        if (cigars.isEmpty()) {
            return flowOf(emptyList())
        }
        return flow {
            cigars.asFlow().flatMapConcat {
                add(it, humidor)
            }.collectFirst(cigars.size) {
                emit(it)
            }
        }
    }

    override fun updateFavorite(value: Boolean, cigar: Cigar): Flow<Boolean> {
        return flow {
            queries.setFavorite(value, cigar.rowid)
            emit(value)
        }
    }

    override fun updateRating(value: Long, cigar: Cigar): Flow<Long> {
        return flow {
            queries.setRating(value, cigar.rowid)
            emit(value)
        }
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
