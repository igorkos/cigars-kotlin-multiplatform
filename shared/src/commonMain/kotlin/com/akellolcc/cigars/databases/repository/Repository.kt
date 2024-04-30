/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/28/24, 11:58 PM
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

package com.akellolcc.cigars.databases.repository

import com.akellolcc.cigars.databases.extensions.BaseEntity
import com.akellolcc.cigars.screens.search.FilterParameter
import kotlinx.coroutines.flow.Flow

interface Repository<ENTITY : BaseEntity> {
    /**
     * Get the entity from the database.
     */
    fun getSync(id: Long, where: Long? = null): ENTITY

    fun allSync(sorting: FilterParameter<Boolean>? = null, filter: List<FilterParameter<*>>? = null): List<ENTITY>

    fun observe(id: Long): Flow<ENTITY>

    fun all(sorting: FilterParameter<Boolean>? = null, filter: List<FilterParameter<*>>? = null): Flow<List<ENTITY>>

    fun add(entity: ENTITY): Flow<ENTITY>

    fun addAll(entities: List<ENTITY>): Flow<List<ENTITY>>

    fun update(entity: ENTITY): Flow<ENTITY>

    fun remove(id: Long, where: Long? = null): Flow<Boolean>

    fun removeAll()

    fun find(id: Long, where: Long? = null): ENTITY?

    fun contains(id: Long, where: Long? = null): Boolean

    fun count(): Long

    fun lastInsertRowId(): Long
}

