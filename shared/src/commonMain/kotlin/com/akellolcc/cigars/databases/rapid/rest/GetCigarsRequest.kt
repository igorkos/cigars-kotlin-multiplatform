/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/16/24, 1:54 PM
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

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
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
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.serialization.json.Json

class GetCigarsRequest(val fields: List<FilterParameter<*>>) : RestRequestInterface<List<Cigar>, Cigar>() {
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
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Cigar> {
        page = params.key ?: 1
        return try {
            val list = restRequest.execute().flatMapConcat { restResponse ->
                process(restResponse).asFlow()
            }.flatMapConcat { rapid ->
                GetCigarsBrand(rapid.brandId).flow().flatMapConcat {
                    val cigar = rapid.toCigar()
                    cigar.brand = it.name
                    flowOf(cigar)
                }
            }.toList()
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

    override fun paging(): Flow<PagingData<Cigar>> {
        Log.debug("GetCigarsRequest paging")
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                initialLoadSize = 20,
                prefetchDistance = 2
            ),
            pagingSourceFactory = {
                Log.debug("GetCigarsRequest pagingSourceFactory")
                this
            },
            initialKey = 1
        ).flow
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

