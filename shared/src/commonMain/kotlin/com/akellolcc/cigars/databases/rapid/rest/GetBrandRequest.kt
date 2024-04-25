/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/24/24, 3:10 PM
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
 */

package com.akellolcc.cigars.databases.rapid.rest

import com.akellolcc.cigars.databases.rapid.rest.RestRequest.Companion.GET
import com.akellolcc.cigars.logging.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class RapidCigarBrand(val brandId: Long, val name: String?)

@Serializable
class GetCigarsBrandResponse(val brand: RapidCigarBrand)

class GetCigarsBrand(val cigar: RapidCigar) {
    companion object {
        private const val BASE_URL = "https://cigars.p.rapidapi.com/brands/"
    }

    private val restRequest: RestRequest
        get() {
            val url = BASE_URL + cigar.brandId
            return RestRequest(GET, url, cache = true)
        }

    fun execute(): Flow<RapidCigar> {
        return flow {
            restRequest.execute().map { restResponse ->
                if (restResponse.status == 200) {
                    val response = Json.decodeFromString<GetCigarsBrandResponse>(restResponse.body)
                    //Log.debug("Got brand=${response.brand.brandId} name=${response.brand.name}")
                    cigar.brand = response.brand.name
                    emit(cigar)
                } else {
                    Log.error("GetCigarsBrand got response ${restResponse.status}")
                    throw Exception("Got response ${restResponse.status}")
                }
            }
        }
    }

}

@Serializable
class GetCigarsBrandsResponse(val brands: List<RapidCigarBrand>, val count: Int, val page: Int)


class GetCigarsBrands(val brand: String?) {
    private var page = 0
    private var totalItems = 0
    private var receivedItems = 0
    val isLastPage: Boolean
        get() = if (page == 0) false else receivedItems >= totalItems

    companion object {
        private const val BASE_URL = "https://cigars.p.rapidapi.com/brands?"
    }

    private val restRequest: RestRequest
        get() {
            val url = BASE_URL + "page=$page" +
                    if (brand != null) "&search=$brand" else ""
            //TODO: remove on production cache
            return RestRequest(GET, url, cache = true)
        }
    private val json = Json { ignoreUnknownKeys = true }

    fun next(): Flow<List<RapidCigarBrand>> {
        if (isLastPage) {
            return flowOf(emptyList())
        }
        page++
        return call()
    }

    private fun call(): Flow<List<RapidCigarBrand>> {
        return flow {
            restRequest.execute().map { restResponse ->
                if (restResponse.status == 200) {
                    val response = json.decodeFromString<GetCigarsBrandsResponse>(restResponse.body)
                    Log.debug("GetCigarsRequest got page=${response.page} count=${response.count} get=${response.brands.size}")
                    totalItems = response.count
                    receivedItems += response.brands.size
                    page = response.page
                    emit(response.brands)
                } else {
                    Log.error("GetCigarsRequest got response ${restResponse.status}")
                    throw Exception("Got response ${restResponse.status}")
                }
            }
        }
    }

}