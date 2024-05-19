/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/19/24, 1:24 PM
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

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import app.cash.sqldelight.Query
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import com.akellolcc.cigars.databases.models.BaseEntity
import com.akellolcc.cigars.databases.models.HumidorCigar
import com.akellolcc.cigars.databases.repository.Repository
import com.akellolcc.cigars.databases.sqldelight.queries.DatabaseQueries
import com.akellolcc.cigars.screens.components.search.data.FilterParameter
import com.akellolcc.cigars.screens.components.search.data.FiltersList
import com.akellolcc.cigars.utils.collectFirst
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

abstract class SQLDelightBaseRepository<ENTITY : BaseEntity>(protected open val wrapper: DatabaseQueries<ENTITY>) :
    Repository<ENTITY> {

    override fun allSync(sorting: FilterParameter<Boolean>?, filter: FiltersList?): List<ENTITY> {
        throw IllegalArgumentException("Must override allSync")
    }

    override fun all(sorting: FilterParameter<Boolean>?, filter: FiltersList?): Flow<List<ENTITY>> {
        throw IllegalArgumentException("Must override all")
    }

    override fun paging(sorting: FilterParameter<Boolean>?, filter: FiltersList?): Flow<PagingData<ENTITY>> {
        throw IllegalArgumentException("Must override paging")
    }

    override fun count(): Long {
        throw IllegalArgumentException("Must override count")
    }

    override fun getSync(id: Long, where: Long?): ENTITY {
        return wrapper.get(id, where).executeAsOne()
    }

    private fun queryAll(
        sorting: FilterParameter<Boolean>?, filter: FiltersList?,
        vararg args: Pair<String, Any?>
    ): Query<ENTITY> {
        return wrapper.all(sorting, filter, *args)
    }

    override fun allSync(
        sorting: FilterParameter<Boolean>?,
        filter: FiltersList?,
        vararg args: Pair<String, Any?>
    ): List<ENTITY> =
        queryAll(sorting, filter, *args).executeAsList()


    override fun all(
        sorting: FilterParameter<Boolean>?,
        filter: FiltersList?,
        vararg args: Pair<String, Any?>
    ): Flow<List<ENTITY>> = queryAll(sorting, filter, *args).asFlow().mapToList(Dispatchers.IO)

    override fun observe(id: Long): Flow<ENTITY> {
        if (id < 0) return MutableStateFlow(wrapper.empty())
        return wrapper.get(id).asFlow().mapToOne(Dispatchers.IO)
    }

    override fun paging(
        sorting: FilterParameter<Boolean>?,
        filter: FiltersList?,
        vararg args: Pair<String, Any?>
    ): Flow<PagingData<ENTITY>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = { wrapper.paging(sorting, filter, *args) },
            initialKey = 0
        ).flow
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun addAll(entities: List<ENTITY>): Flow<List<ENTITY>> {
        return flow {
            if (entities.isEmpty()) {
                emit(emptyList())
                return@flow
            }
            val list = mutableListOf<ENTITY>()
            entities.asFlow().flatMapConcat { entity ->
                add(entity)
            }.collectFirst(entities.size) {
                emit(list)
            }
        }
    }

    override fun add(entity: ENTITY): Flow<ENTITY> {
        return flow {
            if (entity.rowid < 0 || !contains(entity)) {
                wrapper.transactionWithResult {
                    wrapper.add(entity)
                    val id = lastInsertRowId()
                    entity.rowid = id
                    emit(entity)
                }
            } else {
                throw Exception("Can't insert entity: $entity which already exist in the database.")
            }
        }
    }

    override fun update(entity: ENTITY): Flow<ENTITY> {
        return flow {
            if (contains(entity)) {
                wrapper.update(entity)
            } else {
                throw Exception("Can't update entity: $entity which doesn't exist in the database.")
            }
        }
    }

    fun remove(entity: ENTITY) = remove(entity.rowid)
    override fun removeAll() {
        CoroutineScope(Dispatchers.IO).launch {
            wrapper.removeAll()
        }
    }

    override fun find(id: Long, where: Long?): ENTITY? {
        return wrapper.find(id, where).executeAsOneOrNull()
    }


    override fun remove(id: Long, where: Long?): Flow<Boolean> {
        return flow {
            val idExists = contains(id, where)
            if (idExists) {
                wrapper.remove(id, where)
            } else {
                throw Exception("Can't remove entity with id: $id which doesn't exist in the database.")
            }
        }
    }

    fun contains(entity: ENTITY): Boolean {
        return when (entity) {
            is HumidorCigar -> contains(entity.humidor.rowid, entity.cigar.rowid)
            else -> return wrapper.contains(entity.rowid).executeAsOne() != 0L
        }
    }

    override fun contains(id: Long, where: Long?): Boolean {
        return wrapper.contains(id, where).executeAsOne() != 0L
    }

    override fun count(vararg args: Pair<String, Any?>): Long {
        return wrapper.count(*args).executeAsOne()
    }

    override fun lastInsertRowId(): Long {
        return wrapper.lastInsertRowId().executeAsOne()
    }

    protected open fun doDelete(id: Long, where: Long? = null): Flow<Unit> {
        return flow {
            wrapper.remove(id, where)
            emit(Unit)
        }
    }

    protected fun Long.toBoolean(): Boolean = this != 0L

    protected fun Boolean.toLong(): Long = if (this) {
        1L
    } else {
        0L
    }
}
