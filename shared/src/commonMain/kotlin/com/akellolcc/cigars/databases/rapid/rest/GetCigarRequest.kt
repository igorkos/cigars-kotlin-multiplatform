/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/16/24, 11:24 AM
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

import com.akellolcc.cigars.databases.models.Cigar
import com.akellolcc.cigars.databases.rapid.models.GetCigarResponse
import com.akellolcc.cigars.databases.rapid.models.RapidCigar
import com.akellolcc.cigars.databases.rapid.rest.RestRequest.Companion.GET
import com.akellolcc.cigars.logging.Log
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.serialization.json.Json

class GetCigarRequest(val cigarId: Long) : RestRequestInterface<Cigar, Any>() {
    companion object {
        private const val BASE_URL = "https://cigars.p.rapidapi.com/cigars/"
    }

    private val restRequest: RestRequest
        get() {
            val url = BASE_URL + cigarId
            return RestRequest(GET, url)
        }
    private val json = Json { ignoreUnknownKeys = true }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun flow(): Flow<Cigar> {
        return flow {
            restRequest.execute().flatMapConcat { restResponse ->
                flowOf(process(restResponse))
            }.flatMapConcat { rapid ->
                GetCigarsBrand(rapid.brandId).flow().flatMapConcat {
                    val cigar = rapid.toCigar()
                    cigar.brand = it.name
                    flowOf(cigar)
                }
            }.collect {
                emit(it)
            }
        }
    }

    override fun sync(): Cigar {
        val restResponse = restRequest.executeSync()
        val rapidCigar = process(restResponse)
        val brand = GetCigarsBrand(rapidCigar.brandId).sync()
        val cigar = rapidCigar.toCigar()
        cigar.brand = brand.name
        return cigar
    }

    private fun process(restResponse: RestResponse): RapidCigar {
        if (restResponse.status == 200) {
            val response = json.decodeFromString<GetCigarResponse>(restResponse.body)
            Log.debug("GetCigarsRequest got get=${response.cigar}")
            return response.cigar
        } else {
            Log.error("GetCigarsRequest got response ${restResponse.status}")
            throw Exception("Got response ${restResponse.status}")
        }
    }
}

