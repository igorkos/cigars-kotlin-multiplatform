package com.akellolcc.cigars.databases.repository

import com.akellolcc.cigars.databases.extensions.BaseEntity
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.ObservableWrapper
import kotlinx.coroutines.flow.Flow

interface Repository<ENTITY : BaseEntity> {
    suspend fun get(id: Long): ENTITY

    fun getSync(id: Long): ENTITY
    fun allSync(sortField: String?, accenting: Boolean = false): List<ENTITY>

    fun observe(id: Long): Flow<ENTITY>

    fun observeOrNull(id: Long): Flow<ENTITY?>

    fun observe(entity: ENTITY): Flow<ENTITY>
    fun observeAll(sortField: String? = null, accenting: Boolean = true): Flow<List<ENTITY>>

    fun all(sortField: String? = null, accenting: Boolean = true): Observable<List<ENTITY>>
    suspend fun find(id: Long): ENTITY?

    fun update(entity: ENTITY): Observable<ENTITY>

    fun addOrUpdate(entity: ENTITY)

    fun add(entity: ENTITY): ObservableWrapper<ENTITY>

    fun remove(entity: ENTITY): Boolean

    fun remove(id: Long): Boolean


    fun contains(id: Long): Boolean

    fun count(): Long
}
