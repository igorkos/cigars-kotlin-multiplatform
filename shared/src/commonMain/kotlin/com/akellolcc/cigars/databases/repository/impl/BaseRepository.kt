/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/19/24, 6:00 PM
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

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import com.akellolcc.cigars.databases.extensions.BaseEntity
import com.akellolcc.cigars.databases.extensions.HumidorCigar
import com.akellolcc.cigars.databases.repository.DatabaseQueries
import com.akellolcc.cigars.databases.repository.Repository
import com.badoo.reaktive.coroutinesinterop.asObservable
import com.badoo.reaktive.coroutinesinterop.singleFromCoroutine
import com.badoo.reaktive.observable.ObservableWrapper
import com.badoo.reaktive.observable.observable
import com.badoo.reaktive.observable.wrap
import com.badoo.reaktive.single.SingleWrapper
import com.badoo.reaktive.single.wrap
import dev.icerock.moko.mvvm.flow.cMutableStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

abstract class BaseRepository<ENTITY : BaseEntity>(protected open val wrapper: DatabaseQueries<ENTITY>) :
    Repository<ENTITY> {

    override fun getSync(id: Long, where: Long?): ENTITY {
        return wrapper.get(id, where).executeAsOne()
    }

    override fun allSync(sortField: String?, accenting: Boolean): List<ENTITY> {
        return (if (accenting)
            wrapper.allAsc(sortField ?: "name")
        else
            wrapper.allDesc(sortField ?: "name")
                ).executeAsList()
    }

    fun observe(entity: ENTITY): ObservableWrapper<ENTITY> = observe(entity.rowid)
    override fun observe(id: Long): ObservableWrapper<ENTITY> {
        if (id < 0) return MutableStateFlow(wrapper.empty()).cMutableStateFlow().asObservable()
            .wrap()
        return wrapper.get(id).asFlow().mapToOne(Dispatchers.IO).asObservable().wrap()
    }

    override fun all(sortField: String?, accenting: Boolean): ObservableWrapper<List<ENTITY>> {
        return (
                if (accenting)
                    wrapper.allAsc(sortField ?: "name")
                else
                    wrapper.allDesc(sortField ?: "name")
                ).asFlow().mapToList(Dispatchers.IO).asObservable().wrap()
    }

    override fun add(entity: ENTITY): ObservableWrapper<ENTITY> {
        return observable { emitter ->
            CoroutineScope(Dispatchers.IO).launch {
                if (!contains(entity)) {
                    wrapper.transactionWithResult {
                        doUpsert(entity).subscribe {
                            val id = lastInsertRowId()
                            entity.rowid = id
                            emitter.onNext(entity)
                        }
                    }
                } else {
                    emitter.onError(Exception("Can't insert entity: $entity which already exist in the database."))
                }
            }
        }.wrap()
    }

    override fun update(entity: ENTITY): ObservableWrapper<ENTITY> {
        return observable { emitter ->
            if (contains(entity)) {
                doUpsert(entity, false).subscribe {
                    emitter.onNext(entity)
                }
            } else {
                emitter.onError(Exception("Can't update entity: $entity which doesn't exist in the database."))
            }
        }.wrap()
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


    override fun remove(id: Long, where: Long?): ObservableWrapper<Boolean> {
        return observable { emitter ->
            val idExists = contains(id, where)
            if (idExists) {
                doDelete(id, where).subscribe {
                    emitter.onNext(true)
                }
            } else {
                emitter.onError(Exception("Can't remove entity with id: $id which doesn't exist in the database."))
            }
        }.wrap()
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

    override fun count(): Long {
        return wrapper.count().executeAsOne()
    }

    override fun lastInsertRowId(): Long {
        return wrapper.lastInsertRowId().executeAsOne()
    }


    protected abstract fun doUpsert(entity: ENTITY, add: Boolean = true): SingleWrapper<ENTITY>

    protected open fun doDelete(id: Long, where: Long? = null): SingleWrapper<Unit> {
        return singleFromCoroutine {
            wrapper.remove(id, where)
        }.wrap()
    }

    protected fun Long.toBoolean(): Boolean = this != 0L

    protected fun Boolean.toLong(): Long = if (this) {
        1L
    } else {
        0L
    }
}
