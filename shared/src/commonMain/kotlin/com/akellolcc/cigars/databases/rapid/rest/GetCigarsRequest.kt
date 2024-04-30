/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/29/24, 2:27 PM
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

import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.CigarStrength
import com.akellolcc.cigars.databases.rapid.rest.RestRequest.Companion.GET
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.screens.search.FilterParameter
import com.akellolcc.cigars.utils.fraction
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class RapidCigar(
    val cigarId: Long,
    val brandId: Long,
    val name: String?,
    val length: Double?,
    val ringGauge: Long?,
    val country: String?,
    val filler: String?,
    val wrapper: String?,
    val color: String?,
    val strength: String?,
) {
    var brand: String? = null
    fun toCigar(): Cigar {
        return Cigar(
            cigarId,
            name ?: "Unknown",
            brand,
            country,
            null,
            "Toro",
            wrapper ?: "",
            wrapper ?: "",
            ringGauge ?: 0,
            length?.fraction ?: "0",
            CigarStrength.fromString(strength) ?: CigarStrength.Mild,
            null,
            null,
            null,
            filler ?: "",
            null,
            1,
            shopping = false,
            favorites = false,
            price = null
        )
    }
}

@Serializable
class GetCigarsResponse(val cigars: List<RapidCigar>, val page: Int, val count: Int)
class GetCigarsRequest(val fields: List<FilterParameter<String>>) {
    private var page = 0
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
                    "brand" -> "&brand=${field.value}"
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

    fun next(): Flow<List<Cigar>> {
        if (isLastPage) {
            return flowOf(emptyList())
        }
        page++
        return call()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun call(): Flow<List<Cigar>> {
        return flow {
            val list = restRequest.execute().flatMapConcat { restResponse ->
                if (restResponse.status == 200) {
                    val response = json.decodeFromString<GetCigarsResponse>(restResponse.body)
                    Log.debug("GetCigarsRequest got page=${response.page} count=${response.count} get=${response.cigars.size}")
                    totalItems = response.count
                    receivedItems += response.cigars.size
                    page = response.page
                    response.cigars.asFlow()
                } else {
                    Log.error("GetCigarsRequest got response ${restResponse.status}")
                    throw Exception("Got response ${restResponse.status}")
                }
            }.flatMapConcat {
                GetCigarsBrand(it).execute()
            }.flatMapConcat {
                flowOf(it.toCigar())
            }.toList()
            emit(list)
        }
    }
}

