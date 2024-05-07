/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/5/24, 10:12 PM
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

import com.akellolcc.cigars.logging.Log
import io.ktor.client.HttpClient
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.bodyAsText
import io.ktor.http.encodeURLQueryComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.math.min


data class RestResponse(val status: Int, val body: String)

data class RestError(val status: Int, val body: String)


data class RestRequest(
    val method: String,
    val url: String,
    val body: String? = null,
    val token: String? = null,
    val cache: Boolean = false
) {
    companion object {
        const val GET = "GET"
        const val POST = "POST"
        const val PUT = "PUT"
        const val DELETE = "DELETE"
        const val RAPID_KEY = "1d231078e9msh6d5d9403dcf4a9bp106fcfjsnf5eaa93115f0"
        const val RAPID_HOST = "cigars.p.rapidapi.com"
        private var remaining = Long.MAX_VALUE
        private val clientMemory = HttpClient {
            install(HttpCache) {
                publicStorage(HttpCacheStorage(true))
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.debug("Rapid Request $message")
                    }
                }
                level = LogLevel.INFO
            }
        }


        private val clientPermanent = HttpClient {
            install(HttpCache) {
                publicStorage(HttpCacheStorage(false))
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.debug("Rapid Request $message")
                    }
                }
                level = LogLevel.INFO
            }
        }
    }

    fun execute(): Flow<RestResponse> {
        return flow {
            val encoded = url.encodeURLQueryComponent()
            val response = (if (cache) clientPermanent else clientMemory).get(encoded) {
                headers {
                    append("X-RapidAPI-Key", RAPID_KEY)
                    append("X-RapidAPI-Host", RAPID_HOST)
                }
            }
            val reqCount = response.headers["x-ratelimit-requests-remaining"]?.toLong() ?: Long.MAX_VALUE
            if (reqCount < remaining) {
                remaining = min(reqCount, remaining)
                Log.debug("Rapid request quota remaining -> $remaining")
            }
            emit(RestResponse(response.status.value, response.bodyAsText()))
        }
    }
}


