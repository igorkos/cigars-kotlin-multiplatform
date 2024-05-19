/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/16/24, 11:07 AM
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
import com.akellolcc.cigars.databases.rapid.models.GetCigarsBrandResponse
import com.akellolcc.cigars.databases.rapid.rest.RestRequest.Companion.GET
import com.akellolcc.cigars.logging.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import kotlinx.serialization.json.Json

class GetCigarsBrand(val brand: Long) : RestRequestInterface<Brand, Any>() {
    companion object {
        private const val BASE_URL = "https://cigars.p.rapidapi.com/brands/"
    }

    private val restRequest: RestRequest
        get() {
            val url = BASE_URL + brand
            return RestRequest(GET, url, cache = true)
        }

    override fun flow(): Flow<Brand> {
        return flow {
            restRequest.execute().map { restResponse ->
                val brand = process(restResponse)
                emit(brand)
            }.single()
        }
    }

    override fun sync(): Brand {
        val restResponse = restRequest.executeSync()
        return process(restResponse)
    }


    private fun process(restResponse: RestResponse): Brand {
        if (restResponse.status == 200) {
            val response = Json.decodeFromString<GetCigarsBrandResponse>(restResponse.body)
            return response.brand.toBrand()
        } else {
            Log.error("GetCigarsBrand got response ${restResponse.status}")
            throw Exception("Got response ${restResponse.status}")
        }
    }
}


