/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/29/24, 4:18 PM
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


package com.akellolcc.cigars.tests

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import com.akellolcc.cigars.CigarsApplication
import com.akellolcc.cigars.utils.BaseUiTest
import com.akellolcc.cigars.utils.pressButton
import com.akellolcc.cigars.utils.setAppContext
import com.akellolcc.cigars.utils.sleep
import com.akellolcc.cigars.utils.textIsDisplayed
import com.akellolcc.cigars.utils.waitForText
import org.junit.Rule
import kotlin.test.Test


class CigarsAppTest : BaseUiTest() {

    @get:Rule(order = 0)
    val composeTestRule = createAndroidComposeRule(ComponentActivity::class.java)

    @Test
    @OptIn(androidx.compose.ui.test.ExperimentalTestApi::class)
    fun tabNavigation() {
        with(composeTestRule) {
            setContent {
                setAppContext(LocalContext.current)
                CigarsApplication()
            }
            waitForText("Cigars")
            sleep(500)
            onNodeWithTag("CigarsScreen-List").onChildren().assertCountEquals(5)
            textIsDisplayed("#1", true)
            textIsDisplayed("#2", true)
            textIsDisplayed("#3", true)
            textIsDisplayed("#4", true)
            textIsDisplayed("#5", true)

            pressButton("Humidors")
            waitForText("Humidors")
            sleep(500)
            onNodeWithTag("HumidorsScreen-List").onChildren().assertCountEquals(2)
            textIsDisplayed("Case Elegance Renzo Humidor")
            textIsDisplayed("Second")

            pressButton("Favorites")
            waitForText("Favorites")
            sleep(500)
            onNodeWithTag("FavoritesScreen-List").assertDoesNotExist()
            textIsDisplayed("Nothing to show")

            pressButton("Search")
            waitForText("Search")
            sleep(500)
            textIsDisplayed("Add more")
        }
    }
}