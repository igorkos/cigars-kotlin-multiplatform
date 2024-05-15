/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/15/24, 1:02 PM
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

import com.akellolcc.cigars.databases.models.Brand
import com.akellolcc.cigars.databases.rapid.rest.GetCigarsBrand
import com.akellolcc.cigars.databases.rapid.rest.GetCigarsBrands
import com.akellolcc.cigars.databases.repository.BrandsRepository
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.screens.components.search.data.FilterParameter
import com.akellolcc.cigars.utils.ObjectFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

open class RapidBrandsRepository : BrandsRepository {
    private var request: GetCigarsBrands? = null

    override fun getSync(id: Long, where: Long?): Brand {
        val request = GetCigarsBrand(id)
        return request.executeSync()
    }

    override fun allSync(sorting: FilterParameter<Boolean>?, filter: List<FilterParameter<*>>?, page: Int): List<Brand> {
        Log.debug("Load entities searching fields: $filter")
        if (filter.isNullOrEmpty()) return listOf()
        val search = filter.first().value as String
        if (request == null || page == 0 || search != request!!.brand) {
            request = GetCigarsBrands(search)
        }
        return request!!.executeSync()
    }

    override fun observe(id: Long): Flow<Brand> {
        val request = GetCigarsBrand(id)
        return request.execute()
    }

    override fun all(sorting: FilterParameter<Boolean>?, filter: List<FilterParameter<*>>?, page: Int): Flow<List<Brand>> {
        Log.debug("Load entities searching fields: $filter")
        if (filter.isNullOrEmpty()) return flow { emit(listOf()) }
        val search = filter.first().value as String
        if (request == null || page == 0 || search != request!!.brand) {
            request = GetCigarsBrands(search)
        }
        return request!!.execute()
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
