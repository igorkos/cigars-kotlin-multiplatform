/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/22/24, 12:55 PM
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


package com.akellolcc.cigars.cigars

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.akellolcc.cigars.CigarsApplication
import com.akellolcc.cigars.databases.models.CigarSortingFields
import com.akellolcc.cigars.screens.navigation.CigarsRoute
import com.akellolcc.cigars.utils.BaseUiTest
import com.akellolcc.cigars.utils.assertListOrder
import com.akellolcc.cigars.utils.setAppContext
import com.akellolcc.cigars.utils.sleep
import com.akellolcc.cigars.utils.textIsDisplayed
import com.akellolcc.cigars.utils.waitForText
import org.junit.Rule
import kotlin.test.Test

@OptIn(androidx.compose.ui.test.ExperimentalTestApi::class)
class CigarsListTests : BaseUiTest() {

    @get:Rule(order = 0)
    val composeTestRule = createAndroidComposeRule(ComponentActivity::class.java)

    @Test
    fun cigarsListTest() {
        with(composeTestRule) {
            setContent {
                setAppContext(LocalContext.current)
                CigarsApplication()
            }
            waitForText("Cigars")
            sleep(500)
            onNodeWithTag("${CigarsRoute.route}-List").onChildren().assertCountEquals(5)
            textIsDisplayed("#1", true)
            textIsDisplayed("#2", true)
            textIsDisplayed("#3", true)
            textIsDisplayed("#4", true)
            textIsDisplayed("#5", true)
        }
    }

    @Test
    fun cigarsSortTest() {
        with(composeTestRule) {
            setContent {
                setAppContext(LocalContext.current)
                CigarsApplication()
            }
            waitForText("Cigars")
            sleep(500)
            onNodeWithTag("${CigarsRoute.route}-List").assertExists()
            onNodeWithTag("${CigarsRoute.route}-Sort", true).assertExists()
            onNodeWithTag("${CigarsRoute.route}-SortField", true).assertExists()
            onNodeWithTag("${CigarsRoute.route}-Filter", true).assertExists()

            onNodeWithTag("${CigarsRoute.route}-SortField", true).performClick()
            onNodeWithTag("${CigarsRoute.route}-Menu", true).assertExists()
            onNodeWithTag("${CigarsRoute.route}-Menu", true).onChildren().assertCountEquals(6)
            textIsDisplayed(CigarSortingFields.localized(CigarSortingFields.Name))
            textIsDisplayed(CigarSortingFields.localized(CigarSortingFields.Brand))
            textIsDisplayed(CigarSortingFields.localized(CigarSortingFields.Country))
            textIsDisplayed(CigarSortingFields.localized(CigarSortingFields.Shape), expectedOccurrences = 6)
            textIsDisplayed(CigarSortingFields.localized(CigarSortingFields.Gauge), expectedOccurrences = 6)
            textIsDisplayed(CigarSortingFields.localized(CigarSortingFields.Length), expectedOccurrences = 6)
            //Closes menu
            onNodeWithTag("${CigarsRoute.route}-Menu", true).onChildren()[0].performClick()

            assertSortingBy(0, listOf("#1", "#2", "#3", "#4", "#5"))
            assertSortingBy(1, listOf("#1", "#2", "#3", "#5", "#4"), listOf("#4", "#3", "#5", "#2", "#1"))
            assertSortingBy(2, listOf("#1", "#2", "#3", "#4", "#5"), listOf("#2", "#3", "#4", "#5", "#1"))
            assertSortingBy(3, listOf("#1", "#2", "#3", "#4", "#5"), listOf("#3", "#4", "#5", "#2", "#1"))
            assertSortingBy(4, listOf("#1", "#3", "#4", "#5", "#2"), listOf("#2", "#3", "#4", "#5", "#1"))
            assertSortingBy(5, listOf("#2", "#3", "#5", "#4", "#1"), listOf("#1", "#4", "#3", "#5", "#2"))

        }
    }

    private fun assertSortingBy(index: Int, expected: List<String>, reversed: List<String>? = null) {
        with(composeTestRule) {
            onNodeWithTag("${CigarsRoute.route}-SortField", true).performClick()
            onNodeWithTag("${CigarsRoute.route}-Menu", true).onChildren()[index].performClick()
            sleep(1000)
            var sortedList = onNodeWithTag("${CigarsRoute.route}-List").onChildren()
            sortedList.assertListOrder(5, expected)
            //reverse sort
            onNodeWithTag("${CigarsRoute.route}-Sort", true).performClick()
            sleep(1000)
            sortedList = onNodeWithTag("${CigarsRoute.route}-List").onChildren()
            sortedList.assertListOrder(5, reversed ?: expected.reversed())
        }
    }
}