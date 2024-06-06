/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/19/24, 1:09 PM
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

import androidx.paging.PagingData
import com.akellolcc.cigars.databases.models.BaseEntity
import com.akellolcc.cigars.screens.components.search.data.FilterParameter
import com.akellolcc.cigars.screens.components.search.data.FiltersList
import kotlinx.coroutines.flow.Flow

interface InternalRepository<ENTITY : BaseEntity> {
    /**
     * Protected methods
     */
    fun allSync(
        sorting: FilterParameter<Boolean>? = null,
        filter: FiltersList? = null,
        vararg args: Pair<String, Any?>
    ): List<ENTITY>


    fun all(
        sorting: FilterParameter<Boolean>? = null,
        filter: FiltersList? = null,
        vararg args: Pair<String, Any?>
    ): Flow<List<ENTITY>>

    fun paging(
        sorting: FilterParameter<Boolean>?,
        filter: FiltersList?,
        vararg args: Pair<String, Any?>
    ): Flow<PagingData<ENTITY>>

    fun count(vararg args: Pair<String, Any?>): Long
}

interface Repository<ENTITY : BaseEntity> : InternalRepository<ENTITY> {
    /**
     * Public methods
     */
    fun allSync(
        sorting: FilterParameter<Boolean>? = null,
        filter: FiltersList? = null
    ): List<ENTITY>

    fun all(
        sorting: FilterParameter<Boolean>? = null,
        filter: FiltersList? = null,
    ): Flow<List<ENTITY>>

    fun paging(
        sorting: FilterParameter<Boolean>?,
        filter: FiltersList?,
    ): Flow<PagingData<ENTITY>>

    fun count(): Long

    fun observe(id: Long): Flow<ENTITY>

    fun getSync(id: Long, where: Long? = null): ENTITY

    fun add(entity: ENTITY): Flow<ENTITY>

    fun addAll(entities: List<ENTITY>): Flow<List<ENTITY>>

    fun update(entity: ENTITY): Flow<ENTITY>

    fun remove(id: Long, where: Long? = null): Flow<Boolean>

    fun removeAll()

    fun find(id: Long, where: Long? = null): ENTITY?

    fun contains(id: Long, where: Long? = null): Boolean


    fun lastInsertRowId(): Long
}

