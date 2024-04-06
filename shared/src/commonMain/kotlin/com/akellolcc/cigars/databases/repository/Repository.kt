package com.akellolcc.cigars.databases.repository

import com.akellolcc.cigars.databases.extensions.BaseEntity
import kotlinx.coroutines.flow.Flow

interface Repository<ENTITY : BaseEntity> {
    suspend fun get(id: Long): ENTITY

    fun getSync(id: Long): ENTITY

    suspend fun find(id: Long): ENTITY?

    fun observe(id: Long): Flow<ENTITY>

    fun observeOrNull(id: Long): Flow<ENTITY?>

    fun observe(entity: ENTITY): Flow<ENTITY>

    suspend fun all(): List<ENTITY>

    fun observeAll(): Flow<List<ENTITY>>

    fun add(entity: ENTITY)

    fun remove(entity: ENTITY): Boolean

    fun remove(id: Long): Boolean

    fun update(entity: ENTITY)

    fun addOrUpdate(entity: ENTITY)

    fun contains(id: Long): Boolean
}
