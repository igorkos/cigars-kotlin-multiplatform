/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/28/24, 9:38 PM
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

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.akellolcc.cigars.CigarsApplication
import com.akellolcc.cigars.databases.Database
import com.akellolcc.cigars.logging.Log
import dev.icerock.moko.resources.FileResource
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.AfterClass
import org.junit.BeforeClass
import java.net.HttpURLConnection

typealias MockRequestHandler = (request: RecordedRequest) -> MockResponse

open class BaseUiTest() {

    companion object {
        const val MOCK_SERVER_PORT = 47777
        val BASE_URL = "http://localhost:$MOCK_SERVER_PORT"
        protected val dispatcher = MockRequestDispatcher(UiTestUtils.testContext)
        protected var webServer: MockWebServer? = null

        @BeforeClass
        @JvmStatic
        fun startMockServer() {
            Log.initLog { event ->
                println(event)
            }
            /*if (webServer == null) {
                Log.debug("Mock Web Server starting")
                webServer = MockWebServer()
                webServer!!.start(MOCK_SERVER_PORT)
                webServer!!.dispatcher = dispatcher
            }*/
        }

        @AfterClass
        @JvmStatic
        fun shutDownServer() {
            webServer?.shutdown()
            webServer = null
        }
    }

    @Composable
    fun CigarsAppContent() {
        setAppContext(LocalContext.current)
        Database.createInstance(false)
        Database.instance.reset()
        Pref.isFirstStart = true
        CigarsApplication()
    }

    fun addResponse(
        pathPattern: String,
        filename: FileResource,
        httpMethod: String = "GET",
        status: Int = HttpURLConnection.HTTP_OK
    ) = dispatcher.addResponse(pathPattern, filename, httpMethod, status)

    fun addResponse(
        pathPattern: String,
        requestHandler: MockRequestHandler,
        httpMethod: String = "GET",

        ) = dispatcher.addResponse(pathPattern, requestHandler, httpMethod)
}