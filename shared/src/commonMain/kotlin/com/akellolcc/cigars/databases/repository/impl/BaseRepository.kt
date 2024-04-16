package com.akellolcc.cigars.databases.repository.impl

import com.akellolcc.cigars.databases.CigarsDatabaseQueries
import com.akellolcc.cigars.databases.extensions.BaseEntity
import com.akellolcc.cigars.databases.repository.Repository
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.ObservableWrapper
import com.badoo.reaktive.observable.observable
import com.badoo.reaktive.observable.wrap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

abstract class BaseRepository<ENTITY : BaseEntity>(protected val queries: CigarsDatabaseQueries) :
    Repository<ENTITY> {

    override suspend fun get(id: Long): ENTITY {
        return observe(id).first()
    }

    override fun getSync(id: Long): ENTITY {
        TODO("Not yet implemented")
    }

    override fun allSync(sortField: String?, accenting: Boolean): List<ENTITY> {
        return listOf()
    }

    override suspend fun find(id: Long): ENTITY? {
        return observeOrNull(id).first()
    }

    override fun observe(entity: ENTITY): Flow<ENTITY> {
        return observe(entity.rowid)
    }

    override fun all(sortField: String?, accenting: Boolean): Observable<List<ENTITY>> {
        return observable { emitter ->
            CoroutineScope(Dispatchers.IO).launch {
                observeAll(sortField, accenting).collect {
                    emitter.onNext(it)
                    emitter.onComplete()
                }
            }
        }
    }

    override fun add(entity: ENTITY): ObservableWrapper<ENTITY> {
        return observable { emitter ->
            CoroutineScope(Dispatchers.IO).launch {
                if (!contains(entity.rowid)) {
                    queries.transactionWithResult {
                        doUpsert(entity)
                        val id = queries.lastInsertRowId().executeAsOne()
                        entity.rowid = id
                        emitter.onNext(entity)
                        emitter.onComplete()
                    }
                } else {
                    emitter.onError(Exception("Can't insert entity: $entity which already exist in the database."))
                }
            }
        }.wrap()
    }

    override fun remove(entity: ENTITY) = remove(entity.rowid)

    override fun remove(id: Long): Boolean {
        val idExists = contains(id)
        if (idExists) {
            doDelete(id)
        }
        return idExists
    }

    override fun update(entity: ENTITY): Observable<ENTITY> {
        return observable { emitter ->
            if (contains(entity.rowid)) {
                CoroutineScope(Dispatchers.IO).launch {
                    doUpsert(entity, false)
                    emitter.onNext(entity)
                }
            } else {
                emitter.onError(Exception("Can't update entity: $entity which doesn't exist in the database."))
            }
        }
    }

    override fun addOrUpdate(entity: ENTITY) {
        CoroutineScope(Dispatchers.IO).launch {
            doUpsert(entity)
        }
    }

    override fun count(): Long {
        return 0L
    }

    protected open suspend fun doUpsert(entity: ENTITY, add: Boolean = true) {}

    protected abstract fun doDelete(id: Long)

    protected fun Long.toBoolean(): Boolean = this != 0L

    protected fun Boolean.toLong(): Long = if (this) {
        1L
    } else {
        0L
    }
}
