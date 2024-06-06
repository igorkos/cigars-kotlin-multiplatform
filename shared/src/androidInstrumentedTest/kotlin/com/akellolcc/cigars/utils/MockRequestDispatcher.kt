/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/22/24, 12:18 AM
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

package com.akellolcc.cigars.utils

import android.content.Context
import com.akellolcc.cigars.theme.readTextFile
import dev.icerock.moko.resources.FileResource
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import java.net.HttpURLConnection

class MockRequestDispatcher(private val context: Context) : Dispatcher() {

    private val simpleResponses: MutableMap<String, MockResponse> = mutableMapOf()
    private val complexResponses: MutableMap<String, MockRequestHandler> = mutableMapOf()

    private fun mockResponse(responseBody: String, status: Int = HttpURLConnection.HTTP_OK) =
        MockResponse()
            .setResponseCode(status)
            .setBody(responseBody)

    fun addResponse(
        pathPattern: String,
        filename: FileResource,
        httpMethod: String = "GET",
        status: Int = HttpURLConnection.HTTP_OK
    ) {
        readTextFile(filename)?.let {
            val response = mockResponse(it, status)
            val responseKey = "$httpMethod/$pathPattern"
            // adding the http method into the key allows for a repeated pathPattern
            // that is used by both GET and POST to behave differently for eg.
            if (simpleResponses[responseKey] != null) {
                simpleResponses.replace(responseKey, response)
            } else {
                simpleResponses[responseKey] = response
            }
        }
    }

    fun addResponse(
        pathPattern: String,
        requestHandler: MockRequestHandler,
        httpMethod: String = "GET",
    ) {
        val responseKey = "$httpMethod/$pathPattern"
        if (complexResponses[responseKey] != null) {
            complexResponses.replace(responseKey, requestHandler)
        } else {
            complexResponses[responseKey] = requestHandler
        }
    }

    override fun dispatch(request: RecordedRequest): MockResponse {
        println("Incoming request: $request")
        Thread.sleep(200) // provide a small delay to better mimic real life network call across a mobile network
        val responseKey = request.method + request.path

        var response = findComplexResponse(responseKey, request)

        if (response == null) {
            response = findSimpleResponse(responseKey)
        }

        if (response == null) {
            println("no response found for $responseKey")
            response = errorResponse(responseKey)
        }
        return response
    }

    private fun findComplexResponse(responseKey: String, request: RecordedRequest): MockResponse? {
        for (pathPattern in complexResponses.keys) {
            if (responseKey.matches(Regex(pathPattern))) {
                val handler = complexResponses[pathPattern]
                if (handler != null) {
                    return handler(request)
                }
            }
        }
        return null
    }

    private fun findSimpleResponse(responseKey: String): MockResponse? {
        for (pathPattern in simpleResponses.keys) {
            if (responseKey.matches(Regex(pathPattern))) {
                val response = simpleResponses[pathPattern]
                if (response != null) {
                    return response
                }
            }
        }
        return null
    }

    private fun errorResponse(reason: String): MockResponse {
        return mockResponse("""{"error":"response not found for "$reason"}""", HttpURLConnection.HTTP_INTERNAL_ERROR)
    }
}