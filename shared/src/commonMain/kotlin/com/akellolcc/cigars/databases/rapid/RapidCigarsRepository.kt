/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/19/24, 1:11 PM
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

package com.akellolcc.cigars.databases.rapid

import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.akellolcc.cigars.databases.models.Cigar
import com.akellolcc.cigars.databases.models.Humidor
import com.akellolcc.cigars.databases.rapid.rest.GetCigarRequest
import com.akellolcc.cigars.databases.rapid.rest.GetCigarsRequest
import com.akellolcc.cigars.databases.repository.CigarsSearchRepository
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.screens.components.search.data.FilterParameter
import com.akellolcc.cigars.screens.components.search.data.FiltersList
import com.akellolcc.cigars.screens.components.search.data.compareFilterParameters
import com.akellolcc.cigars.utils.ObjectFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

open class RapidCigarsRepository : CigarsSearchRepository {
    private var request: GetCigarsRequest? = null

    override fun getSync(id: Long, where: Long?): Cigar {
        val request = GetCigarRequest(id)
        return request.sync()
    }

    override fun allSync(sorting: FilterParameter<Boolean>?, filter: FiltersList?): List<Cigar> {
        if (filter == null) return listOf()
        if (request == null || !compareFilterParameters(filter, request!!.fields)) {
            request = GetCigarsRequest(filter.map { it.copy() })
        }
        return request!!.sync()
    }

    override fun all(sorting: FilterParameter<Boolean>?, filter: FiltersList?): Flow<List<Cigar>> {
        Log.debug("Load entities searching fields: $filter")
        if (filter == null) return flow { emit(listOf()) }
        if (request == null || !compareFilterParameters(filter, request!!.fields)) {
            request = GetCigarsRequest(filter.map { it.copy() })
        }
        return request!!.flow()
    }

    override fun observe(id: Long): Flow<Cigar> {
        val request = GetCigarRequest(id)
        return request.flow()
    }

    override fun paging(sorting: FilterParameter<Boolean>?, filter: FiltersList?): Flow<PagingData<Cigar>> {
        Log.debug("Load entities searching fields: $filter")
        if (filter == null) return flow { PagingSource.LoadResult.Page(data = listOf(), prevKey = null, nextKey = null) }
        val search = filter.first().value
        if (request == null || !compareFilterParameters(filter, request!!.fields)) {
            request = GetCigarsRequest(filter.map { it.copy() })
        }
        return request!!.paging()
    }

    override fun allSync(
        sorting: FilterParameter<Boolean>?,
        filter: FiltersList?,
        vararg args: Pair<String, Any?>
    ): List<Cigar> {
        throw IllegalArgumentException("Please use allSync(sorting: FilterParameter<Boolean>?, filter: List<FilterParameter<*>>?)")
    }

    override fun all(
        sorting: FilterParameter<Boolean>?,
        filter: FiltersList?,
        vararg args: Pair<String, Any?>
    ): Flow<List<Cigar>> {
        throw IllegalArgumentException("Please use all(sorting: FilterParameter<Boolean>?, filter: List<FilterParameter<*>>?)")
    }

    override fun paging(
        sorting: FilterParameter<Boolean>?,
        filter: FiltersList?,
        vararg args: Pair<String, Any?>
    ): Flow<PagingData<Cigar>> {
        throw IllegalArgumentException("Please use paging(sorting: FilterParameter<Boolean>?, filter: List<FilterParameter<*>>?)")
    }


    override fun remove(id: Long, where: Long?): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override fun removeAll() {
        TODO("Not yet implemented")
    }

    override fun find(id: Long, where: Long?): Cigar? {
        TODO("Not yet implemented")
    }

    override fun contains(id: Long, where: Long?): Boolean {
        TODO("Not yet implemented")
    }

    override fun count(): Long {
        TODO("Not yet implemented")
    }

    override fun count(vararg args: Pair<String, Any?>): Long {
        TODO("Not yet implemented")
    }

    override fun lastInsertRowId(): Long {
        TODO("Not yet implemented")
    }

    override fun add(cigar: Cigar, humidor: Humidor): Flow<Cigar> {
        TODO("Not yet implemented")
    }

    override fun add(entity: Cigar): Flow<Cigar> {
        TODO("Not yet implemented")
    }

    override fun addAll(cigars: List<Cigar>, humidor: Humidor): Flow<List<Cigar>> {
        TODO("Not yet implemented")
    }

    override fun addAll(entities: List<Cigar>): Flow<List<Cigar>> {
        TODO("Not yet implemented")
    }

    override fun update(entity: Cigar): Flow<Cigar> {
        TODO("Not yet implemented")
    }

    override fun updateFavorite(value: Boolean, cigar: Cigar): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override fun updateRating(value: Long, cigar: Cigar): Flow<Long> {
        TODO("Not yet implemented")
    }


    companion object Factory : ObjectFactory<RapidCigarsRepository>() {
        override fun factory(data: Any?): RapidCigarsRepository {
            return RapidCigarsRepository()
        }
    }
}
