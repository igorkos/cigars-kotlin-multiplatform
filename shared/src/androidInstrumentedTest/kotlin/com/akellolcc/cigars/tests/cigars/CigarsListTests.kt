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


package com.akellolcc.cigars.tests.cigars

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import com.akellolcc.cigars.CigarsApplication
import com.akellolcc.cigars.databases.models.CigarSortingFields
import com.akellolcc.cigars.screens.navigation.CigarsRoute
import com.akellolcc.cigars.utils.BaseUiTest
import com.akellolcc.cigars.utils.assertListOrder
import com.akellolcc.cigars.utils.pressButton
import com.akellolcc.cigars.utils.replaceText
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
            //Wait for app to load
            waitForText("Cigars")
            sleep(500)
            //Check items displayed
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
            //Wait for app to load
            waitForText("Cigars")
            sleep(500)
            //Check that all top bar elements are displayed
            onNodeWithTag("${CigarsRoute.route}-List").assertExists()
            onNodeWithTag("${CigarsRoute.route}-Sort", true).assertExists()
            onNodeWithTag("${CigarsRoute.route}-SortField", true).assertExists()
            onNodeWithTag("${CigarsRoute.route}-Filter", true).assertExists()

            //Check available sort fields menu
            onNodeWithTag("${CigarsRoute.route}-SortField", true).performClick()
            onNodeWithTag("${CigarsRoute.route}-Menu", true).assertExists()
            onNodeWithTag("${CigarsRoute.route}-Menu", true).onChildren().assertCountEquals(6)
            val menu = onNodeWithTag("${CigarsRoute.route}-Menu").onChildren()
            menu.assertListOrder(
                6, listOf(
                    CigarSortingFields.localized(CigarSortingFields.Name),
                    CigarSortingFields.localized(CigarSortingFields.Brand),
                    CigarSortingFields.localized(CigarSortingFields.Country),
                    CigarSortingFields.localized(CigarSortingFields.Shape),
                    CigarSortingFields.localized(CigarSortingFields.Gauge),
                    CigarSortingFields.localized(CigarSortingFields.Length)
                )
            )
            //Closes menu
            onNodeWithTag("${CigarsRoute.route}-Menu", true).onChildren()[0].performClick()
            //Check sorting
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

    @Test
    fun cigarsFilteringTest() {
        with(composeTestRule) {
            setContent {
                setAppContext(LocalContext.current)
                CigarsApplication()
            }
            //Wait for app to load
            waitForText("Cigars")
            sleep(500)
            //Check that all top bar elements are displayed
            onNodeWithTag("${CigarsRoute.route}-List").assertExists()
            onNodeWithTag("${CigarsRoute.route}-Sort", true).assertExists()
            onNodeWithTag("${CigarsRoute.route}-SortField", true).assertExists()
            onNodeWithTag("${CigarsRoute.route}-Filter", true).assertExists()

            //Enable search filter with one element Name
            onNodeWithTag("${CigarsRoute.route}-Filter", true).performClick()
            onNodeWithTag("${CigarsRoute.route}-Search").assertExists()
            onNodeWithTag("SearchParameterField-${CigarSortingFields.Name.value}").assertExists()
            //When one no delete
            onNodeWithTag("SearchParameterField-${CigarSortingFields.Name.value}-leading").assertDoesNotExist()
            //Check available fields menu
            pressButton("Add more")
            onNodeWithTag("SearchComponent-FieldsMenu").assertExists()
            onNodeWithTag("SearchComponent-FieldsMenu").onChildren().assertCountEquals(5)

            var menu = onNodeWithTag("SearchComponent-FieldsMenu").onChildren()
            menu.assertListOrder(
                5, listOf(
                    CigarSortingFields.localized(CigarSortingFields.Brand),
                    CigarSortingFields.localized(CigarSortingFields.Country),
                    CigarSortingFields.localized(CigarSortingFields.Shape),
                    CigarSortingFields.localized(CigarSortingFields.Gauge),
                    CigarSortingFields.localized(CigarSortingFields.Length)
                )
            )
            onNodeWithTag("SearchComponent-FieldsMenu", true).onChildren()[0].performClick()
            onNodeWithTag("SearchParameterField-${CigarSortingFields.Name.value}").assertExists()
            onNodeWithTag("SearchParameterField-${CigarSortingFields.Brand.value}").assertExists()

            pressButton("Add more")
            onNodeWithTag("SearchComponent-FieldsMenu").assertExists()
            onNodeWithTag("SearchComponent-FieldsMenu").onChildren().assertCountEquals(4)
            onNodeWithTag("SearchComponent-FieldsMenu", true).onChildren()[0].performClick()
            onNodeWithTag("SearchParameterField-${CigarSortingFields.Name.value}").assertExists()
            onNodeWithTag("SearchParameterField-${CigarSortingFields.Brand.value}").assertExists()
            onNodeWithTag("SearchParameterField-${CigarSortingFields.Country.value}").assertExists()

            pressButton("Add more")
            onNodeWithTag("SearchComponent-FieldsMenu").assertExists()
            onNodeWithTag("SearchComponent-FieldsMenu").onChildren().assertCountEquals(3)
            onNodeWithTag("SearchComponent-FieldsMenu", true).onChildren()[1].performClick()
            onNodeWithTag("SearchParameterField-${CigarSortingFields.Name.value}").assertExists()
            onNodeWithTag("SearchParameterField-${CigarSortingFields.Brand.value}").assertExists()
            onNodeWithTag("SearchParameterField-${CigarSortingFields.Country.value}").assertExists()
            onNodeWithTag("SearchParameterField-${CigarSortingFields.Gauge.value}").assertExists()

            //Check remove field button
            onNodeWithTag("SearchParameterField-${CigarSortingFields.Name.value}-leading").assertExists()
            onNodeWithTag("SearchParameterField-${CigarSortingFields.Brand.value}-leading").assertExists()
            onNodeWithTag("SearchParameterField-${CigarSortingFields.Country.value}-leading").assertExists()
            onNodeWithTag("SearchParameterField-${CigarSortingFields.Gauge.value}-leading").assertExists()

            //Remove last field
            onNodeWithTag("SearchParameterField-${CigarSortingFields.Gauge.value}-leading").performClick()
            onNodeWithTag("SearchParameterField-${CigarSortingFields.Name.value}").assertExists()
            onNodeWithTag("SearchParameterField-${CigarSortingFields.Brand.value}").assertExists()
            onNodeWithTag("SearchParameterField-${CigarSortingFields.Country.value}").assertExists()

            //Remove first field
            onNodeWithTag("SearchParameterField-${CigarSortingFields.Name.value}-leading").performClick()
            onNodeWithTag("SearchParameterField-${CigarSortingFields.Brand.value}").assertExists()
            onNodeWithTag("SearchParameterField-${CigarSortingFields.Country.value}").assertExists()

            //Check menu again
            pressButton("Add more")
            menu = onNodeWithTag("SearchComponent-FieldsMenu").onChildren()
            menu.assertListOrder(
                4, listOf(
                    CigarSortingFields.localized(CigarSortingFields.Name),
                    CigarSortingFields.localized(CigarSortingFields.Shape),
                    CigarSortingFields.localized(CigarSortingFields.Gauge),
                    CigarSortingFields.localized(CigarSortingFields.Length)
                )
            )
            //Add name remove other
            onNodeWithTag("SearchComponent-FieldsMenu", true).onChildren()[0].performClick()
            onNodeWithTag("SearchParameterField-${CigarSortingFields.Brand.value}-leading").performClick()
            onNodeWithTag("SearchParameterField-${CigarSortingFields.Country.value}-leading").performClick()

            //Check fields
            onNodeWithTag("SearchParameterField-${CigarSortingFields.Name.value}").assertExists()
            onNodeWithTag("SearchParameterField-${CigarSortingFields.Country.value}").assertDoesNotExist()
            onNodeWithTag("SearchParameterField-${CigarSortingFields.Brand.value}").assertDoesNotExist()
            onNodeWithTag("SearchParameterField-${CigarSortingFields.Shape.value}").assertDoesNotExist()
            onNodeWithTag("SearchParameterField-${CigarSortingFields.Gauge.value}").assertDoesNotExist()
            onNodeWithTag("SearchParameterField-${CigarSortingFields.Length.value}").assertDoesNotExist()

            //Check name search
            replaceText(CigarSortingFields.localized(CigarSortingFields.Name), "Fuente")
            sleep(1000)
            onNodeWithTag("${CigarsRoute.route}-List").performScrollToIndex(1)
            onNodeWithTag("${CigarsRoute.route}-List").onChildren().assertCountEquals(1)


            replaceText(CigarSortingFields.localized(CigarSortingFields.Name), "")
            sleep(1000)
            onNodeWithTag("${CigarsRoute.route}-List").performScrollToIndex(4)
            onNodeWithTag("${CigarsRoute.route}-List").onChildren().assertCountEquals(5)

            replaceText(CigarSortingFields.localized(CigarSortingFields.Name), "Serie")
            sleep(1000)
            onNodeWithTag("${CigarsRoute.route}-List").performScrollToIndex(2)
            onNodeWithTag("${CigarsRoute.route}-List").onChildren().assertCountEquals(2)

            replaceText(CigarSortingFields.localized(CigarSortingFields.Name), "")
            sleep(1000)
            onNodeWithTag("${CigarsRoute.route}-List").performScrollToIndex(4)
            onNodeWithTag("${CigarsRoute.route}-List").onChildren().assertCountEquals(5)

            //Check brand search
            pressButton("Add more")
            onNodeWithTag("SearchComponent-FieldsMenu", true).onChildren()[0].performClick()
            onNodeWithTag("SearchParameterField-${CigarSortingFields.Name.value}-leading").performClick()
            onNodeWithTag("SearchParameterField-${CigarSortingFields.Brand.value}").assertExists()

            replaceText(CigarSortingFields.localized(CigarSortingFields.Brand), "Fabrica de")
            sleep(1000)
            onNodeWithTag("${CigarsRoute.route}-List").performScrollToIndex(1)
            onNodeWithTag("${CigarsRoute.route}-List").onChildren().assertCountEquals(1)


            replaceText(CigarSortingFields.localized(CigarSortingFields.Brand), "")
            sleep(1000)
            onNodeWithTag("${CigarsRoute.route}-List").performScrollToIndex(4)
            onNodeWithTag("${CigarsRoute.route}-List").onChildren().assertCountEquals(5)

            replaceText(CigarSortingFields.localized(CigarSortingFields.Brand), "Tabolisa")
            sleep(1000)
            onNodeWithTag("${CigarsRoute.route}-List").performScrollToIndex(2)
            onNodeWithTag("${CigarsRoute.route}-List").onChildren().assertCountEquals(2)

            //Check country search
            pressButton("Add more")
            onNodeWithTag("SearchComponent-FieldsMenu", true).onChildren()[1].performClick()
            onNodeWithTag("SearchParameterField-${CigarSortingFields.Brand.value}-leading").performClick()
            onNodeWithTag("SearchParameterField-${CigarSortingFields.Country.value}").assertExists()

            replaceText(CigarSortingFields.localized(CigarSortingFields.Country), "Dominican")
            sleep(1000)
            onNodeWithTag("${CigarsRoute.route}-List").performScrollToIndex(1)
            onNodeWithTag("${CigarsRoute.route}-List").onChildren().assertCountEquals(1)


            replaceText(CigarSortingFields.localized(CigarSortingFields.Country), "")
            sleep(1000)
            onNodeWithTag("${CigarsRoute.route}-List").performScrollToIndex(4)
            onNodeWithTag("${CigarsRoute.route}-List").onChildren().assertCountEquals(5)

            replaceText(CigarSortingFields.localized(CigarSortingFields.Country), "Nicaragua")
            sleep(1000)
            onNodeWithTag("${CigarsRoute.route}-List").performScrollToIndex(3)
            onNodeWithTag("${CigarsRoute.route}-List").onChildren().assertCountEquals(4)

            //Check country + gauge search
            pressButton("Add more")
            onNodeWithTag("SearchComponent-FieldsMenu", true).onChildren()[3].performClick()
            onNodeWithTag("SearchParameterField-${CigarSortingFields.Country.value}").assertExists()
            onNodeWithTag("SearchParameterField-${CigarSortingFields.Gauge.value}").assertExists()

            onNodeWithTag("SearchParameterField-${CigarSortingFields.Gauge.value}-input").replaceText("52")
            sleep(1000)
            onNodeWithTag("${CigarsRoute.route}-List").performScrollToIndex(3)
            onNodeWithTag("${CigarsRoute.route}-List").onChildren().assertCountEquals(4)

            onNodeWithTag("SearchParameterField-${CigarSortingFields.Gauge.value}-input").replaceText("60")
            sleep(1000)
            onNodeWithTag("${CigarsRoute.route}-List").performScrollToIndex(0)
            onNodeWithTag("${CigarsRoute.route}-List").onChildren().assertCountEquals(1)


            replaceText(CigarSortingFields.localized(CigarSortingFields.Country), "Dominican")
            sleep(1000)
            onNodeWithTag("${CigarsRoute.route}-List").assertDoesNotExist()
        }
    }
}