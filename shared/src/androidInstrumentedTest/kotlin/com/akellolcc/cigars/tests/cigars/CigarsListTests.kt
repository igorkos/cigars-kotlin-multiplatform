/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/1/24, 4:22 PM
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
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import com.akellolcc.cigars.databases.models.CigarSortingFields
import com.akellolcc.cigars.screens.components.search.SearchParameterField
import com.akellolcc.cigars.screens.navigation.CigarsRoute
import com.akellolcc.cigars.utils.BaseUiTest
import com.akellolcc.cigars.utils.assertHasNodesWithTag
import com.akellolcc.cigars.utils.assertListOrder
import com.akellolcc.cigars.utils.assertNoNodesWithTag
import com.akellolcc.cigars.utils.pressButton
import com.akellolcc.cigars.utils.replaceText
import com.akellolcc.cigars.utils.sleep
import com.akellolcc.cigars.utils.waitForTag
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test

@OptIn(androidx.compose.ui.test.ExperimentalTestApi::class)
open class CigarsListTests : BaseUiTest() {

    @get:Rule(order = 0)
    val composeTestRule = createAndroidComposeRule(ComponentActivity::class.java)

    @Before
    open fun before() {
        route = CigarsRoute
        with(composeTestRule) {
            setContent {
                CigarsAppContent()
            }
            //Wait for app to load
            waitForTag(tag())
        }
    }

    @Test
    fun cigarsListTest() {
        with(composeTestRule) {
            //Check items displayed
            onNodeWithTag(tag("List")).onChildren()
                .assertListOrder(5, listOf("#1", "#2", "#3", "#4", "#5"))
        }
    }

    @Test
    fun cigarsSortTest() {
        with(composeTestRule) {
            //Check that all top bar elements are displayed
            onNodeWithTag(tag("List")).assertExists()
            onNodeWithTag(tag("Sort"), true).assertExists()
            onNodeWithTag(tag("SortField"), true).assertExists()
            onNodeWithTag(tag("Filter"), true).assertExists()

            //Check available sort fields menu
            onNodeWithTag(tag("SortField"), true).performClick()
            onNodeWithTag(tag("Menu"), true).assertExists()
            onNodeWithTag(tag("Menu"), true).onChildren().assertCountEquals(6)
            val menu = onNodeWithTag(tag("Menu")).onChildren()
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
            onNodeWithTag(tag("Menu"), true).onChildren()[0].performClick()
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
            onNodeWithTag(tag("SortField"), true).performClick()
            onNodeWithTag(tag("Menu"), true).onChildren()[index].performClick()
            sleep(1000)
            var sortedList = onNodeWithTag(tag("List")).onChildren()
            sortedList.assertListOrder(5, expected)
            //reverse sort
            onNodeWithTag(tag("Sort"), true).performClick()
            sleep(1000)
            sortedList = onNodeWithTag(tag("List")).onChildren()
            sortedList.assertListOrder(5, reversed ?: expected.reversed())
        }
    }

    @Test
    fun cigarsFilteringTest() {
        with(composeTestRule) {
            //Check that all top bar elements are displayed
            onNodeWithTag(tag("List")).assertExists()
            onNodeWithTag(tag("Sort"), true).assertExists()
            onNodeWithTag(tag("SortField"), true).assertExists()
            onNodeWithTag(tag("Filter"), true).assertExists()

            //Enable search filter with one element Name
            onNodeWithTag(tag("Filter"), true).performClick()
            onNodeWithTag(tag("Search")).assertExists()
            onNodeWithTag(SearchParameterField.testTag(CigarSortingFields.Name)).assertExists()
            //When one no delete
            onNodeWithTag(SearchParameterField.testTag(CigarSortingFields.Name, "leading")).assertDoesNotExist()
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
            assertHasNodesWithTag(
                listOf(
                    SearchParameterField.testTag(CigarSortingFields.Name),
                    SearchParameterField.testTag(CigarSortingFields.Brand)
                )
            )

            pressButton("Add more")
            onNodeWithTag("SearchComponent-FieldsMenu").assertExists().onChildren().assertCountEquals(4)[0].performClick()
            assertHasNodesWithTag(
                listOf(
                    SearchParameterField.testTag(CigarSortingFields.Name),
                    SearchParameterField.testTag(CigarSortingFields.Brand),
                    SearchParameterField.testTag(CigarSortingFields.Country)
                )
            )

            pressButton("Add more")
            onNodeWithTag("SearchComponent-FieldsMenu").assertExists().onChildren().assertCountEquals(3)[1].performClick()
            assertHasNodesWithTag(
                listOf(
                    SearchParameterField.testTag(CigarSortingFields.Name),
                    SearchParameterField.testTag(CigarSortingFields.Brand),
                    SearchParameterField.testTag(CigarSortingFields.Country),
                    SearchParameterField.testTag(CigarSortingFields.Gauge)
                )
            )

            //Check remove field button
            assertHasNodesWithTag(
                listOf(
                    SearchParameterField.testTag(CigarSortingFields.Name, "leading"),
                    SearchParameterField.testTag(CigarSortingFields.Brand, "leading"),
                    SearchParameterField.testTag(CigarSortingFields.Country, "leading"),
                    SearchParameterField.testTag(CigarSortingFields.Gauge, "leading"),
                )
            )
            //Remove last field
            onNodeWithTag(SearchParameterField.testTag(CigarSortingFields.Gauge, "leading")).performClick()
            assertHasNodesWithTag(
                listOf(
                    SearchParameterField.testTag(CigarSortingFields.Name, "leading"),
                    SearchParameterField.testTag(CigarSortingFields.Brand, "leading"),
                    SearchParameterField.testTag(CigarSortingFields.Country, "leading"),
                )
            )

            //Remove first field
            onNodeWithTag(SearchParameterField.testTag(CigarSortingFields.Name, "leading")).performClick()
            assertHasNodesWithTag(
                listOf(
                    SearchParameterField.testTag(CigarSortingFields.Brand, "leading"),
                    SearchParameterField.testTag(CigarSortingFields.Country, "leading"),
                )
            )

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
            onNodeWithTag(SearchParameterField.testTag(CigarSortingFields.Brand, "leading")).performClick()
            onNodeWithTag(SearchParameterField.testTag(CigarSortingFields.Country, "leading")).performClick()

            //Check fields
            onNodeWithTag(SearchParameterField.testTag(CigarSortingFields.Name)).assertExists()
            assertNoNodesWithTag(
                listOf(
                    SearchParameterField.testTag(CigarSortingFields.Country),
                    SearchParameterField.testTag(CigarSortingFields.Brand),
                    SearchParameterField.testTag(CigarSortingFields.Shape),
                    SearchParameterField.testTag(CigarSortingFields.Gauge),
                    SearchParameterField.testTag(CigarSortingFields.Length)
                )
            )

            //Check name search
            replaceText(CigarSortingFields.localized(CigarSortingFields.Name), "Fuente")
            sleep(1000)
            onNodeWithTag(tag("List")).performScrollToIndex(1)
            onNodeWithTag(tag("List")).onChildren().assertCountEquals(1)


            replaceText(CigarSortingFields.localized(CigarSortingFields.Name), "")
            sleep(1000)
            onNodeWithTag(tag("List")).performScrollToIndex(4)
            onNodeWithTag(tag("List")).onChildren().assertCountEquals(5)

            replaceText(CigarSortingFields.localized(CigarSortingFields.Name), "Serie")
            sleep(1000)
            onNodeWithTag(tag("List")).performScrollToIndex(2)
            onNodeWithTag(tag("List")).onChildren().assertCountEquals(2)

            replaceText(CigarSortingFields.localized(CigarSortingFields.Name), "")
            sleep(1000)
            onNodeWithTag(tag("List")).performScrollToIndex(4)
            onNodeWithTag(tag("List")).onChildren().assertCountEquals(5)

            //Check brand search
            pressButton("Add more")
            onNodeWithTag("SearchComponent-FieldsMenu", true).onChildren()[0].performClick()
            onNodeWithTag(SearchParameterField.testTag(CigarSortingFields.Name, "leading")).performClick()
            onNodeWithTag(SearchParameterField.testTag(CigarSortingFields.Brand)).assertExists()

            replaceText(CigarSortingFields.localized(CigarSortingFields.Brand), "Fabrica de")
            sleep(1000)
            onNodeWithTag(tag("List")).performScrollToIndex(1)
            onNodeWithTag(tag("List")).onChildren().assertCountEquals(1)


            replaceText(CigarSortingFields.localized(CigarSortingFields.Brand), "")
            sleep(1000)
            onNodeWithTag(tag("List")).performScrollToIndex(4)
            onNodeWithTag(tag("List")).onChildren().assertCountEquals(5)

            replaceText(CigarSortingFields.localized(CigarSortingFields.Brand), "Tabolisa")
            sleep(1000)
            onNodeWithTag(tag("List")).performScrollToIndex(2)
            onNodeWithTag(tag("List")).onChildren().assertCountEquals(2)

            //Check country search
            pressButton("Add more")
            onNodeWithTag("SearchComponent-FieldsMenu", true).onChildren()[1].performClick()
            onNodeWithTag(SearchParameterField.testTag(CigarSortingFields.Brand, "leading")).performClick()
            onNodeWithTag(SearchParameterField.testTag(CigarSortingFields.Country)).assertExists()

            replaceText(CigarSortingFields.localized(CigarSortingFields.Country), "Dominican")
            sleep(1000)
            onNodeWithTag(tag("List")).performScrollToIndex(1)
            onNodeWithTag(tag("List")).onChildren().assertCountEquals(1)


            replaceText(CigarSortingFields.localized(CigarSortingFields.Country), "")
            sleep(1000)
            onNodeWithTag(tag("List")).performScrollToIndex(4)
            onNodeWithTag(tag("List")).onChildren().assertCountEquals(5)

            replaceText(CigarSortingFields.localized(CigarSortingFields.Country), "Nicaragua")
            sleep(1000)
            onNodeWithTag(tag("List")).performScrollToIndex(3)
            onNodeWithTag(tag("List")).onChildren().assertCountEquals(4)

            //Check country + gauge search
            pressButton("Add more")
            onNodeWithTag("SearchComponent-FieldsMenu", true).onChildren()[3].performClick()
            onNodeWithTag(SearchParameterField.testTag(CigarSortingFields.Country)).assertExists()
            onNodeWithTag(SearchParameterField.testTag(CigarSortingFields.Gauge)).assertExists()

            onNodeWithTag(SearchParameterField.testTag(CigarSortingFields.Gauge, "input")).replaceText("52")
            sleep(1000)
            onNodeWithTag(tag("List")).performScrollToIndex(3)
            onNodeWithTag(tag("List")).onChildren().assertCountEquals(4)

            onNodeWithTag(SearchParameterField.testTag(CigarSortingFields.Gauge, "input")).replaceText("60")
            sleep(1000)
            onNodeWithTag(tag("List")).performScrollToIndex(0)
            onNodeWithTag(tag("List")).onChildren().assertCountEquals(1)


            replaceText(CigarSortingFields.localized(CigarSortingFields.Country), "Dominican")
            sleep(1000)
            onNodeWithTag(tag("List")).assertDoesNotExist()
        }
    }
}