/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/29/24, 4:14 PM
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
import com.akellolcc.cigars.databases.HumidorCigarsDatabaseQueries
import com.akellolcc.cigars.databases.createRepository
import com.akellolcc.cigars.databases.models.Cigar
import com.akellolcc.cigars.databases.models.History
import com.akellolcc.cigars.databases.models.HistoryType
import com.akellolcc.cigars.databases.models.Humidor
import com.akellolcc.cigars.databases.models.HumidorCigar
import com.akellolcc.cigars.databases.repository.CigarHistoryRepository
import com.akellolcc.cigars.databases.repository.CigarHumidorRepository
import com.akellolcc.cigars.databases.repository.CigarsRepository
import com.akellolcc.cigars.databases.repository.HistoryRepository
import com.akellolcc.cigars.databases.repository.HumidorHistoryRepository
import com.akellolcc.cigars.databases.repository.HumidorsRepository
import com.akellolcc.cigars.databases.sqldelight.queries.CigarHumidorTableQueries
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.screens.components.search.data.FilterParameter
import com.akellolcc.cigars.screens.components.search.data.FiltersList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlin.math.absoluteValue

abstract class SqlDelightBaseCigarHumidorRepository(
    protected val queries: HumidorCigarsDatabaseQueries,
    protected val id: Pair<String, Long>
) : SQLDelightBaseRepository<HumidorCigar>(CigarHumidorTableQueries(queries)), CigarHumidorRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun updateCount(
        entity: HumidorCigar,
        count: Long,
        price: Double?
    ): Flow<HumidorCigar> {
        val c = entity.count - count
        val type = if (c < 0) HistoryType.Addition else HistoryType.Deletion
        var updated: HumidorCigar? = null
        return super.update(entity.copy(count = count)).flatMapConcat {
            updated = it
            val cigarsHistoryDatabase: HistoryRepository = createRepository(CigarHistoryRepository::class, entity.cigar.rowid)
            Log.info("UpdateCount: Add Cigar History item ${HistoryType.localized(type)}")
            cigarsHistoryDatabase.add(
                History(
                    -1,
                    c.absoluteValue,
                    Clock.System.now().toEpochMilliseconds(),
                    count,
                    price,
                    type,
                    entity.cigar,
                    entity.humidor,
                    entity.humidor
                )
            )
        }.flatMapConcat {
            val cigarsRepository: CigarsRepository = createRepository(CigarsRepository::class)
            cigarsRepository.update(entity.cigar.copy(count = count))
        }.map { updated!! }.catch {
            Log.error("Error while updating humidor cigars count $it")
        }
    }

    /**
     * Move cigars from one humidor to another
     * 1. Update count of cigars left in humidor from we move
     * 3. Add Cigar to humidor where we move
     * 2. Update total count of cigars in humidor where we move
     * 3. Add history item Move From To
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun moveCigar(
        from: HumidorCigar,
        to: Humidor,
        count: Long
    ): Flow<Boolean> {
        val humidorsRepository: HumidorsRepository = createRepository(HumidorsRepository::class)
        val humidorHistoryRepository: HistoryRepository = createRepository(HumidorHistoryRepository::class, from.humidor.rowid)
        return flow {
            // If in humidor we have more cigars than we want to move
            //update count of cigars left in humidor from we move
            //else remove cigar from humidor
            if (from.count > count) {
                Log.info("Update count of cigars left in humidor from we move")
                humidorsRepository.updateCigarsCount(from.humidor.rowid, count = from.humidor.count - count).collect {
                    update(from.copy(count = from.count - count)).collect {
                        emit(true)
                    }
                }
            } else {
                Log.info("All Cigars is moved from humidor")
                remove(from.cigar, from.humidor).collect { emit(it) }
            }
        }.flatMapConcat {
            // Add Cigar to humidor where we move
            //First find cigar in humidor where we move
            //if not found add cigar to humidor
            //else update count of cigar in existing entry
            val toEntry = find(from.cigar, to)
            if (toEntry == null) {
                Log.info("Add cigar to humidor")
                add(from.cigar, to, count)
            } else {
                Log.info("Update count of cigar we moved to humidor")
                update(toEntry.copy(count = toEntry.count + count))
            }
        }.flatMapConcat {
            // Update total count of cigars in humidor where we move
            Log.info("Update total count of cigars left in humidor from we move $to")
            val humidor = humidorsRepository.getSync(to.rowid)
            humidorsRepository.updateCigarsCount(to.rowid, humidor.count + count)
        }.flatMapConcat {
            Log.info("Add history item Move From")
            humidorHistoryRepository.add(
                History(
                    -1L,
                    count,
                    Clock.System.now().toEpochMilliseconds(),
                    count,
                    price = from.cigar.price,
                    type = HistoryType.Move,
                    cigar = from.cigar,
                    humidorFrom = from.humidor,
                    humidorTo = to
                )
            )
        }.catch {
            Log.error("Error while moving cigars $it")
        }.flatMapConcat {
            Log.info("Finished moving cigar")
            flowOf(true)
        }
    }

    override fun allSync(
        sorting: FilterParameter<Boolean>?,
        filter: FiltersList?
    ): List<HumidorCigar> {
        return allSync(sorting, filter, id)
    }

    override fun all(
        sorting: FilterParameter<Boolean>?,
        filter: FiltersList?
    ): Flow<List<HumidorCigar>> {
        return all(sorting, filter, id)
    }

    override fun paging(
        sorting: FilterParameter<Boolean>?,
        filter: FiltersList?
    ): Flow<PagingData<HumidorCigar>> {
        return paging(sorting, filter, id)
    }


    override fun count(): Long {
        return count(id)
    }

    override fun add(cigar: Cigar, humidor: Humidor, count: Long): Flow<HumidorCigar> {
        return add(HumidorCigar(count, humidor, cigar))
    }

    override fun remove(cigar: Cigar, from: Humidor): Flow<Boolean> {
        return remove(from.rowid, cigar.rowid)
    }

    override fun find(cigar: Cigar, humidor: Humidor): HumidorCigar? {
        return find(humidor.rowid, cigar.rowid)
    }
}
