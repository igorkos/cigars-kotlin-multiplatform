/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/24/24, 12:36 PM
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

import app.cash.sqldelight.Query
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.akellolcc.cigars.databases.CigarHumidorTable
import com.akellolcc.cigars.databases.HumidorCigarsDatabaseQueries
import com.akellolcc.cigars.databases.createRepository
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.History
import com.akellolcc.cigars.databases.extensions.HistoryType
import com.akellolcc.cigars.databases.extensions.Humidor
import com.akellolcc.cigars.databases.extensions.HumidorCigar
import com.akellolcc.cigars.databases.repository.CigarHistoryRepository
import com.akellolcc.cigars.databases.repository.CigarHumidorRepository
import com.akellolcc.cigars.databases.repository.CigarsRepository
import com.akellolcc.cigars.databases.repository.HistoryRepository
import com.akellolcc.cigars.databases.repository.HumidorHistoryRepository
import com.akellolcc.cigars.databases.repository.HumidorsRepository
import com.akellolcc.cigars.databases.repository.Repository
import com.akellolcc.cigars.databases.repository.impl.queries.CigarHumidorTableQueries
import com.akellolcc.cigars.logging.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlin.math.absoluteValue

abstract class SqlDelightBaseCigarHumidorRepository(
    protected val queries: HumidorCigarsDatabaseQueries
) : BaseRepository<HumidorCigar>(CigarHumidorTableQueries(queries)), CigarHumidorRepository {

    abstract fun observeAllQuery(): Query<CigarHumidorTable>

    override fun all(
        sortField: String?,
        accenting: Boolean
    ): Flow<List<HumidorCigar>> {
        val hRepo = createRepository(HumidorsRepository::class)
        val cRepo = createRepository(CigarsRepository::class)
        return observeAllQuery().asFlow().mapToList(Dispatchers.IO).map {
            it.map { humidorCigar ->
                val humidor = hRepo.getSync(humidorCigar.humidorId)
                val cigar = (cRepo as Repository<Cigar>).getSync(humidorCigar.cigarId)
                HumidorCigar(humidorCigar.count, humidor, cigar)
            }
        }.catch {
            Log.error("Error while getting all humidor cigars $it")
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun updateCount(
        entity: HumidorCigar,
        count: Long,
        price: Double?,
        historyType: HistoryType?,
        humidorTo: Humidor?
    ): Flow<HumidorCigar> {
        val c = entity.count - count
        val type = if (c < 0) HistoryType.Addition else HistoryType.Deletion
        var updated: HumidorCigar? = null
        return super.update(entity.copy(count = count)).flatMapConcat {
            updated = it
            val cigarsHistoryDatabase: HistoryRepository =
                createRepository(CigarHistoryRepository::class, entity.cigar.rowid)
            Log.info("UpdateCount: Add Cigar History item ${HistoryType.localized(type)}")
            cigarsHistoryDatabase.add(
                History(
                    -1,
                    c.absoluteValue,
                    Clock.System.now().toEpochMilliseconds(),
                    count,
                    price,
                    historyType ?: type,
                    entity.cigar.rowid,
                    entity.humidor.rowid,
                    humidorTo?.rowid
                )
            )
        }.map { updated!! }.catch {
            Log.error("Error while updating humidor cigars count $it")
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun moveCigar(
        from: HumidorCigar,
        to: Humidor,
        count: Long
    ): Flow<Boolean> {
        val humidorsRepository: HumidorsRepository = createRepository(HumidorsRepository::class)
        var humidorHistoryRepository: HistoryRepository =
            createRepository(HumidorHistoryRepository::class, from.humidor.rowid)
        return flow {
            if (from.count > count) {
                Log.info("Update count of cigars left in humidor from we move")
                updateCount(
                    from,
                    from.count - count,
                    from.cigar.price,
                    HistoryType.Move
                ).collect {
                    emit(true)
                }
            } else {
                Log.info("All Cigars is moved from humidor")
                remove(from.cigar, from.humidor).collect() {
                    emit(it)
                }
            }
        }.flatMapConcat {
            val fromEntry = from.humidor.copy(count = from.humidor.count - count)
            Log.info("Update total count of cigars left in humidor from we move $fromEntry")
            humidorsRepository.update(fromEntry)
        }.flatMapConcat {
            val toEntry = find(from.cigar, to)
            if (toEntry == null) {
                Log.info("Add cigar to humidor")
                add(from.cigar, to, count)
            } else {
                Log.info("Update count of cigar we moved to humidor")
                updateCount(toEntry, toEntry.count + count, toEntry.cigar.price, HistoryType.Move)
            }
        }.flatMapConcat {
            Log.info("Update total count of cigars left in humidor from we move")
            humidorsRepository.update(to.copy(count = to.count + count))
        }.flatMapConcat {
            Log.info("Add history item Move From")
            humidorHistoryRepository.add(
                History(
                    -1L,
                    count,
                    Clock.System.now().toEpochMilliseconds(),
                    count,
                    price = from.cigar.price,
                    type = HistoryType.MoveFrom,
                    cigarId = from.cigar.rowid,
                    humidorFrom = from.humidor.rowid,
                    humidorTo = to.rowid
                )
            )
        }.flatMapConcat {
            Log.info("Add history item Move To")
            humidorHistoryRepository = createRepository(HumidorHistoryRepository::class, to.rowid)
            humidorHistoryRepository.add(
                History(
                    -1L,
                    count,
                    Clock.System.now().toEpochMilliseconds(),
                    count,
                    price = from.cigar.price,
                    type = HistoryType.MoveTo,
                    cigarId = from.cigar.rowid,
                    humidorFrom = to.rowid,
                    humidorTo = to.rowid
                )
            )
        }.flatMapConcat {
            Log.info("Finished moving cigar")
            flowOf(true)
        }.catch {
            Log.error("Error while moving cigars $it")
        }
    }

    override fun doUpsert(entity: HumidorCigar, add: Boolean): Flow<HumidorCigar> {
        return flow {
            if (add) {
                queries.add(
                    entity.count,
                    entity.humidor.rowid,
                    entity.cigar.rowid
                )
            } else {
                queries.update(
                    entity.count,
                    entity.humidor.rowid,
                    entity.cigar.rowid
                )
            }
            emit(entity)
        }
    }

    override fun add(cigar: Cigar, humidor: Humidor, count: Long): Flow<HumidorCigar> {
        return super.add(HumidorCigar(count, humidor, cigar))
    }

    override fun remove(cigar: Cigar, from: Humidor): Flow<Boolean> {
        return super.remove(from.rowid, cigar.rowid)
    }

    override fun find(cigar: Cigar, humidor: Humidor): HumidorCigar? {
        return super.find(humidor.rowid, cigar.rowid)
    }
}
