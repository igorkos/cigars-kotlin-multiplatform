/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/15/24, 12:01 PM
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

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.akellolcc.cigars.CigarsApplication
import com.akellolcc.cigars.databases.Database
import com.akellolcc.cigars.databases.createRepository
import com.akellolcc.cigars.databases.models.Humidor
import com.akellolcc.cigars.databases.repository.HumidorsRepository
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.screens.navigation.NavRoute
import dev.icerock.moko.resources.FileResource
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import java.net.HttpURLConnection

typealias MockRequestHandler = (request: RecordedRequest) -> MockResponse

open class BaseUiTest() {

    @get:Rule(order = 0)
    val composeTestRule = createAndroidComposeRule(ComponentActivity::class.java)

    open lateinit var route: NavRoute

    @Before
    fun before() {
        with(composeTestRule) {
            setContent {
                setAppContext(LocalContext.current)
                Database.createInstance(false)
                Database.instance.reset()
                Pref.isFirstStart = true
                CigarsApplication()
            }
            setUp()
        }
    }

    open fun setUp() {}

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

    fun tag(component: String? = null, route: NavRoute? = null): String {
        val root = route?.route ?: this.route?.route ?: ""
        if (component == null) return root
        return "${root}-$component"
    }

    fun humidors(): List<Humidor> {
        var humidorsDatabase: HumidorsRepository? = createRepository(HumidorsRepository::class)
        return humidorsDatabase?.allSync() ?: emptyList()
    }

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

}