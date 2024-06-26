/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/19/24, 1:08 PM
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

package com.akellolcc.cigars.databases.sqldelight

import androidx.paging.PagingData
import com.akellolcc.cigars.databases.CigarsDatabaseQueries
import com.akellolcc.cigars.databases.createRepository
import com.akellolcc.cigars.databases.models.Cigar
import com.akellolcc.cigars.databases.models.History
import com.akellolcc.cigars.databases.models.HistoryType
import com.akellolcc.cigars.databases.models.Humidor
import com.akellolcc.cigars.databases.repository.CigarHistoryRepository
import com.akellolcc.cigars.databases.repository.CigarHumidorsRepository
import com.akellolcc.cigars.databases.repository.CigarsRepository
import com.akellolcc.cigars.databases.repository.HumidorsRepository
import com.akellolcc.cigars.databases.sqldelight.queries.CigarsTableQueries
import com.akellolcc.cigars.screens.components.search.data.FilterParameter
import com.akellolcc.cigars.screens.components.search.data.FiltersList
import com.akellolcc.cigars.utils.ObjectFactory
import com.akellolcc.cigars.utils.collectFirst
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.Clock

open class SqlDelightCigarsRepository(protected val queries: CigarsDatabaseQueries) :
    SQLDelightBaseRepository<Cigar>(CigarsTableQueries(queries)), CigarsRepository {

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

    override fun allSync(sorting: FilterParameter<Boolean>?, filter: FiltersList?): List<Cigar> {
        return super.allSync(sorting, filter, Pair("", null))
    }

    override fun all(sorting: FilterParameter<Boolean>?, filter: FiltersList?): Flow<List<Cigar>> {
        return super.all(sorting, filter, Pair("", null))
    }

    override fun paging(sorting: FilterParameter<Boolean>?, filter: FiltersList?): Flow<PagingData<Cigar>> {
        return super.paging(sorting, filter, Pair("", null))
    }

    override fun count(): Long {
        return super.count(Pair("favorites", false))
    }

    companion object Factory : ObjectFactory<SqlDelightCigarsRepository>() {
        override fun factory(data: Any?): SqlDelightCigarsRepository {
            val queries = SqlDelightDatabase.instance.database.cigarsDatabaseQueries
            return SqlDelightCigarsRepository(queries)
        }
    }
}
