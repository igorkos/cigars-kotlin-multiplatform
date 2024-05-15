/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/15/24, 3:16 PM
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

import app.cash.paging.PagingSourceLoadParams
import app.cash.paging.PagingSourceLoadResult
import app.cash.paging.PagingSourceLoadResultPage
import com.akellolcc.cigars.databases.models.Cigar
import com.akellolcc.cigars.databases.rapid.models.GetCigarsResponse
import com.akellolcc.cigars.databases.rapid.models.RapidCigar
import com.akellolcc.cigars.databases.rapid.rest.RestRequest.Companion.GET
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.screens.components.search.data.FilterParameter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.serialization.json.Json

class GetCigarsRequest(val fields: List<FilterParameter<*>>) : RestRequestInterface<List<Cigar>> {
    var page = 0
    private var totalItems = 0
    private var receivedItems = 0
    private val isLastPage: Boolean
        get() = if (page == 0) false else receivedItems >= totalItems

    companion object {
        private const val BASE_URL = "https://cigars.p.rapidapi.com/cigars?"
    }

    private val restRequest: RestRequest
        get() {
            var url = BASE_URL + "page=$page"
            fields.forEach { field ->
                url += when (field.key) {
                    "name" -> "&name=${field.value}"
                    "brand" -> "&brandId=${field.value}"
                    "country" -> "&country=${field.value}"
                    "filler" -> "&filler=${field.value}"
                    "wrapper" -> "&wrapper=${field.value}"
                    "color" -> "&color=${field.value}"
                    "strength" -> "&strength=${field.value}"
                    else -> {}
                }
            }
            return RestRequest(GET, url)
        }
    private val json = Json { ignoreUnknownKeys = true }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun execute(): Flow<List<Cigar>> {
        if (isLastPage) {
            return flowOf(emptyList())
        }
        page++
        return flow {
            val list = restRequest.execute().flatMapConcat { restResponse ->
                process(restResponse).asFlow()
            }.flatMapConcat { rapid ->
                GetCigarsBrand(rapid.brandId).execute().flatMapConcat {
                    val cigar = rapid.toCigar()
                    cigar.brand = it.name
                    flowOf(cigar)
                }
            }.toList()
            emit(list)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun paged(params: PagingSourceLoadParams<Int>): PagingSourceLoadResult<Int, Cigar> {
        val reqPage = params.key ?: 1
        if (isLastPage) {
            return PagingSourceLoadResultPage(
                data = emptyList(),
                prevKey = (reqPage - 1).takeIf { it > 0 },
                nextKey = null
            )
        }
        val list = restRequest.execute().flatMapConcat { restResponse ->
            process(restResponse).asFlow()
        }.flatMapConcat { rapid ->
            GetCigarsBrand(rapid.brandId).execute().flatMapConcat {
                val cigar = rapid.toCigar()
                cigar.brand = it.name
                flowOf(cigar)
            }
        }
    }

    override fun executeSync(): List<Cigar> {
        if (isLastPage) {
            return emptyList()
        }
        page++
        val restResponse = restRequest.executeSync()
        val rapidCigars = process(restResponse)
        return rapidCigars.map {
            val brand = GetCigarsBrand(it.brandId).executeSync()
            val cigar = it.toCigar()
            cigar.brand = brand.name
            cigar
        }
    }

    private fun process(restResponse: RestResponse): List<RapidCigar> {
        if (restResponse.status == 200) {
            val response = json.decodeFromString<GetCigarsResponse>(restResponse.body)
            Log.debug("GetCigarsRequest got page=${response.page} count=${response.count} get=${response.cigars.size}")
            totalItems = response.count
            receivedItems += response.cigars.size
            page = response.page
            return response.cigars
        } else {
            Log.error("GetCigarsRequest got response ${restResponse.status}")
            throw Exception("Got response ${restResponse.status}")
        }
    }
}

