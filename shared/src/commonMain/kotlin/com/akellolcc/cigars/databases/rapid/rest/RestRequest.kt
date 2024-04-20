/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/19/24, 6:00 PM
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

import com.badoo.reaktive.observable.ObservableWrapper
import com.badoo.reaktive.observable.observable
import com.badoo.reaktive.observable.wrap
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

data class RestResponse(val status: Int, val body: String)

data class RestError(val status: Int, val body: String)

data class RestRequest(
    val method: String,
    val url: String,
    val body: String? = null,
    val token: String? = null
) {
    companion object {
        const val GET = "GET"
        const val POST = "POST"
        const val PUT = "PUT"
        const val DELETE = "DELETE"
        const val RAPID_KEY = "1d231078e9msh6d5d9403dcf4a9bp106fcfjsnf5eaa93115f0"
        const val RAPID_HOST = "cigars.p.rapidapi.com"
    }

    fun execute(): ObservableWrapper<RestResponse> {
        val client = HttpClient()
        return observable {
            CoroutineScope(Dispatchers.IO).launch {
                val response = client.get(url) {
                    headers {
                        append("X-RapidAPI-Key", RAPID_KEY)
                        append("X-RapidAPI-Host", RAPID_HOST)
                    }
                }
                it.onNext(RestResponse(response.status.value, response.bodyAsText()))
            }
        }.wrap()
    }
}


