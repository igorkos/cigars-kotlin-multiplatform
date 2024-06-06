/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/19/24, 1:10 PM
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
import com.akellolcc.cigars.databases.models.Brand
import com.akellolcc.cigars.databases.rapid.rest.GetCigarsBrand
import com.akellolcc.cigars.databases.rapid.rest.GetCigarsBrands
import com.akellolcc.cigars.databases.repository.BrandsRepository
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.screens.components.search.data.FilterParameter
import com.akellolcc.cigars.screens.components.search.data.FiltersList
import com.akellolcc.cigars.utils.ObjectFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

open class RapidBrandsRepository : BrandsRepository {
    private var request: GetCigarsBrands? = null

    override fun getSync(id: Long, where: Long?): Brand {
        val request = GetCigarsBrand(id)
        return request.sync()
    }

    override fun allSync(sorting: FilterParameter<Boolean>?, filter: FiltersList?): List<Brand> {
        Log.debug("Load entities searching fields: $filter")
        if (filter.isNullOrEmpty()) return listOf()
        val search = filter.first().value as String
        if (request == null || search != request!!.brand) {
            request = GetCigarsBrands(search)
        }
        return request!!.sync()
    }

    override fun all(sorting: FilterParameter<Boolean>?, filter: FiltersList?): Flow<List<Brand>> {
        Log.debug("Load entities searching fields: $filter")
        if (filter.isNullOrEmpty()) return flow { emit(listOf()) }
        val search = filter.first().value as String
        if (request == null || search != request!!.brand) {
            request = GetCigarsBrands(search)
        }
        return request!!.flow()
    }

    override fun observe(id: Long): Flow<Brand> {
        val request = GetCigarsBrand(id)
        return request.flow()
    }

    override fun paging(sorting: FilterParameter<Boolean>?, filter: FiltersList?): Flow<PagingData<Brand>> {
        Log.debug("Load entities searching fields: $filter")
        val search = filter?.first()?.value as? String
        if (request == null || search != request!!.brand) {
            request = GetCigarsBrands(search)
        }
        return request!!.paging()
    }

    override fun allSync(
        sorting: FilterParameter<Boolean>?,
        filter: FiltersList?,
        vararg args: Pair<String, Any?>
    ): List<Brand> {
        throw IllegalArgumentException("Please use allSync(sorting, filter)")
    }

    override fun all(
        sorting: FilterParameter<Boolean>?,
        filter: FiltersList?,
        vararg args: Pair<String, Any?>
    ): Flow<List<Brand>> {
        throw IllegalArgumentException("Please use all(sorting, filter)")
    }

    override fun paging(
        sorting: FilterParameter<Boolean>?,
        filter: FiltersList?,
        vararg args: Pair<String, Any?>
    ): Flow<PagingData<Brand>> {
        throw IllegalArgumentException("Please use paging(sorting, filter)")
    }

    override fun remove(id: Long, where: Long?): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override fun removeAll() {
        TODO("Not yet implemented")
    }

    override fun find(id: Long, where: Long?): Brand? {
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

    override fun add(entity: Brand): Flow<Brand> {
        TODO("Not yet implemented")
    }

    override fun addAll(entities: List<Brand>): Flow<List<Brand>> {
        TODO("Not yet implemented")
    }

    override fun update(entity: Brand): Flow<Brand> {
        TODO("Not yet implemented")
    }


    companion object Factory : ObjectFactory<RapidBrandsRepository>() {
        override fun factory(data: Any?): RapidBrandsRepository {
            return RapidBrandsRepository()
        }
    }
}
