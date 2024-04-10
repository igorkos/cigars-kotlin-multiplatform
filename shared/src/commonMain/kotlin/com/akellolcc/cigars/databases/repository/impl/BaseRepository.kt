package com.akellolcc.cigars.databases.repository.impl

import com.akellolcc.cigars.databases.CigarsDatabaseQueries
import com.akellolcc.cigars.databases.extensions.BaseEntity
import com.akellolcc.cigars.databases.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

abstract class BaseRepository<ENTITY : BaseEntity>(protected val queries: CigarsDatabaseQueries) : Repository<ENTITY> {

    override suspend fun get(id: Long): ENTITY {
        return observe(id).first()
    }

    override fun getSync(id: Long): ENTITY {
        TODO("Not yet implemented")
    }

    override suspend fun find(id: Long): ENTITY? {
        return observeOrNull(id).first()
    }

    override fun observe(entity: ENTITY): Flow<ENTITY> {
        return observe(entity.rowid)
    }

    override suspend fun all(sortField: String?, accenting: Boolean): List<ENTITY> {
        return observeAll(sortField, accenting).first()
    }

    override fun add(entity: ENTITY, callback: (suspend (Long) -> Unit)?) {
        if (!contains(entity.rowid)) {
            CoroutineScope(Dispatchers.Main).launch {
                queries.transactionWithResult {
                    doUpsert(entity)
                    callback?.let {
                        val id = queries.lastInsertRowId().executeAsOne()
                        it(id)
                    }
                }
            }
        } else {
            error("Can't insert entity: $entity which already exist in the database.")
        }
    }

    override fun remove(entity: ENTITY) = remove(entity.rowid)

    override fun remove(id: Long): Boolean {
        val idExists = contains(id)
        if (idExists) {
            doDelete(id)
        }
        return idExists
    }

    override fun update(entity: ENTITY) {
        if (contains(entity.rowid)) {
            CoroutineScope(Dispatchers.Main).launch {
                doUpsert(entity)
            }
        } else {
            // TODO: Throw custom repository exception
            error("Can't update entity: $entity which doesn't exist in the database.")
        }
    }

    override fun addOrUpdate(entity: ENTITY) {
        CoroutineScope(Dispatchers.Main).launch {
            doUpsert(entity)
        }
    }

    protected open suspend fun doUpsert(entity: ENTITY) {}

    protected abstract fun doDelete(id: Long)

    protected fun Long.toBoolean(): Boolean = this != 0L

    protected fun Boolean.toLong(): Long = if (this) {
        1L
    } else {
        0L
    }
}
