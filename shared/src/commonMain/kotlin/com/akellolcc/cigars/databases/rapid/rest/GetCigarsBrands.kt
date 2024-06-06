/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/17/24, 11:07 AM
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

package com.akellolcc.cigars.databases.rapid.rest

import com.akellolcc.cigars.databases.models.Brand
import com.akellolcc.cigars.databases.rapid.models.GetCigarsBrandsResponse
import com.akellolcc.cigars.logging.Log
import kotlinx.serialization.json.Json

class GetCigarsBrands(val brand: String?) : RestRequestInterface<List<Brand>, Brand>() {
    private var page = 0
    private var totalItems = 0
    private var receivedItems = 0
    private val isLastPage: Boolean
        get() = if (page == 0) false else receivedItems >= totalItems

    companion object {
        private const val BASE_URL = "https://cigars.p.rapidapi.com/brands?"
    }

    private val restRequest: RestRequest
        get() {
            val url = BASE_URL + "page=$page" +
                    if (brand != null) "&search=$brand" else ""
            //TODO: remove on production cache
            return RestRequest(RestRequest.GET, url, cache = true)
        }
    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Brand> {
        page = params.key ?: 1
        return try {
            val list = sync()
            LoadResult.Page(
                data = list,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (list.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            Log.error("GetCigarsRequest failed with ${e.message}")
            LoadResult.Error(e)
        }
    }


    override fun sync(): List<Brand> {
        val restResponse = restRequest.executeSync()
        return process(restResponse)
    }

    private fun process(restResponse: RestResponse): List<Brand> {
        if (restResponse.status == 200) {
            val response = json.decodeFromString<GetCigarsBrandsResponse>(restResponse.body)
            Log.debug("GetCigarsRequest got page=${response.page} count=${response.count} get=${response.brands.size}")
            totalItems = response.count
            receivedItems += response.brands.size
            page = response.page
            return response.brands.map {
                it.toBrand()
            }
        } else {
            Log.error("GetCigarsRequest got response ${restResponse.status}")
            throw Exception("Got response ${restResponse.status}")
        }
    }


}