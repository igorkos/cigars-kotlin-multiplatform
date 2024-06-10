/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/10/24, 12:47 PM
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

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import assertListOrder
import com.akellolcc.cigars.databases.models.CigarSortingFields
import com.akellolcc.cigars.screens.components.search.SearchParameterField
import com.akellolcc.cigars.screens.navigation.CigarsRoute
import com.akellolcc.cigars.screens.navigation.NavRoute
import com.akellolcc.cigars.theme.Localize
import com.akellolcc.cigars.utils.BaseUiTest
import com.akellolcc.cigars.utils.assertHasNodes
import com.akellolcc.cigars.utils.assertNoNodes
import com.akellolcc.cigars.utils.screenListContentDescription
import pressButton
import replaceText
import selectMenuItem
import sleep
import waitForScreen
import kotlin.test.Test

@OptIn(androidx.compose.ui.test.ExperimentalTestApi::class)
open class CigarsListTests : BaseUiTest() {
    override var route: NavRoute = CigarsRoute
    private val LIST_TAG: String
        get() {
            return "${route.title} ${Localize.screen_list_descr}"
        }

    override fun setUp() {
        with(composeTestRule) {
            waitForScreen(CigarsRoute)
            //Check items displayed
            assertListOrder(screenListContentDescription(CigarsRoute), listOf("#1", "#2", "#3", "#4", "#5"))
        }
    }

    @Test
    fun cigarsSortTest() {
        with(composeTestRule) {
            //Check that all top bar elements are displayed
            onNodeWithContentDescription(Localize.screen_list_filter_action_descr).assertExists()
            onNodeWithContentDescription(Localize.screen_list_sort_action_descr).assertExists()
            //Check available sort fields menu
            onNodeWithContentDescription(Localize.screen_list_sort_fields_action_descr).assertExists().performClick()
            assertListOrder(
                Localize.screen_list_sort_fields_list_descr,
                listOf(
                    CigarSortingFields.localized(CigarSortingFields.Name),
                    CigarSortingFields.localized(CigarSortingFields.Brand),
                    CigarSortingFields.localized(CigarSortingFields.Country),
                    CigarSortingFields.localized(CigarSortingFields.Shape),
                    CigarSortingFields.localized(CigarSortingFields.Gauge),
                    CigarSortingFields.localized(CigarSortingFields.Length)
                )
            )
            //Closes menu
            onNodeWithContentDescription(Localize.screen_list_sort_fields_action_descr).assertExists().performClick()
            //Check sorting
            assertSortingBy(0, listOf("#1", "#2", "#3", "#4", "#5"))
            assertSortingBy(1, listOf("#1", "#2", "#3", "#5", "#4"))
            assertSortingBy(2, listOf("#1", "#2", "#3", "#4", "#5"))
            assertSortingBy(3, listOf("#1", "#2", "#3", "#4", "#5"))
            assertSortingBy(4, listOf("#1", "#3", "#4", "#5", "#2"))
            assertSortingBy(5, listOf("#2", "#3", "#5", "#4", "#1"))

        }
    }

    private fun assertSortingBy(index: Int, expected: List<String>) {
        with(composeTestRule) {
            onNodeWithContentDescription(Localize.screen_list_sort_fields_action_descr).assertExists().performClick()
            onNodeWithContentDescription(Localize.screen_list_sort_fields_list_descr).onChildren()[index].performClick()
            sleep(1000)
            assertListOrder(screenListContentDescription(route), expected)
            //reverse sort
            onNodeWithContentDescription(Localize.screen_list_sort_action_descr).performClick()
            sleep(1000)
            assertListOrder(screenListContentDescription(route), expected.reversed(), true)
        }
    }

    private fun assertSearchingBy(field: CigarSortingFields, menuIndex: Int, values: List<Pair<String, Int>>, remove: CigarSortingFields?) {
        with(composeTestRule) {
            if (menuIndex >= 0) {
                pressButton(Localize.button_title_add_search_field)
                selectMenuItem(Localize.filter_control_add_field_menu_descr, menuIndex, CigarSortingFields.localized(field))
            }

            if (remove != null) {
                onNodeWithContentDescription(
                    SearchParameterField.semantics(
                        remove,
                        SearchParameterField.LEADING_TAG
                    )
                ).performClick()
            }

            for (value in values) {
                replaceText(
                    SearchParameterField.semantics(field, SearchParameterField.INPUT_FIELD_TAG),
                    CigarSortingFields.localized(field),
                    value.first
                )
                sleep(1000)
                onNodeWithContentDescription(LIST_TAG).performScrollToIndex(value.second - 1)
                onNodeWithContentDescription(LIST_TAG).onChildren().assertCountEquals(value.second)
            }
        }
    }

    @Test
    fun cigarsFilteringTest() {
        with(composeTestRule) {
            //Check that all top bar elements are displayed
            onNodeWithContentDescription(Localize.screen_list_sort_action_descr).assertExists()
            onNodeWithContentDescription(Localize.screen_list_sort_fields_action_descr).assertExists()
            onNodeWithContentDescription(Localize.screen_list_filter_action_descr).assertExists().performClick()

            onNodeWithContentDescription(Localize.screen_list_filter_control_descr).assertExists()
            onNodeWithContentDescription(SearchParameterField.semantics(CigarSortingFields.Name)).assertExists()
            //When one no delete
            onNodeWithContentDescription(
                SearchParameterField.semantics(
                    CigarSortingFields.Name,
                    SearchParameterField.LEADING_TAG
                )
            ).assertDoesNotExist()
            //Check available fields menu
            pressButton(Localize.filter_control_add_field_descr)
            assertListOrder(
                Localize.filter_control_add_field_menu_descr,
                listOf(
                    CigarSortingFields.localized(CigarSortingFields.Brand),
                    CigarSortingFields.localized(CigarSortingFields.Country),
                    CigarSortingFields.localized(CigarSortingFields.Shape),
                    CigarSortingFields.localized(CigarSortingFields.Gauge),
                    CigarSortingFields.localized(CigarSortingFields.Length)
                )
            )

            //Select Brand
            selectMenuItem(Localize.filter_control_add_field_menu_descr, 0, CigarSortingFields.localized(CigarSortingFields.Brand))
            assertHasNodes(
                Localize.screen_list_filter_control_descr,
                listOf(
                    SearchParameterField.semantics(CigarSortingFields.Name),
                    SearchParameterField.semantics(CigarSortingFields.Brand)
                )
            )

            pressButton(Localize.button_title_add_search_field)
            selectMenuItem(Localize.filter_control_add_field_menu_descr, 0, CigarSortingFields.localized(CigarSortingFields.Country))
            assertHasNodes(
                Localize.screen_list_filter_control_descr,
                listOf(
                    SearchParameterField.semantics(CigarSortingFields.Name),
                    SearchParameterField.semantics(CigarSortingFields.Brand),
                    SearchParameterField.semantics(CigarSortingFields.Country)
                )
            )

            pressButton(Localize.button_title_add_search_field)
            selectMenuItem(Localize.filter_control_add_field_menu_descr, 1, CigarSortingFields.localized(CigarSortingFields.Gauge))
            assertHasNodes(
                Localize.screen_list_filter_control_descr,
                listOf(
                    SearchParameterField.semantics(CigarSortingFields.Name),
                    SearchParameterField.semantics(CigarSortingFields.Brand),
                    SearchParameterField.semantics(CigarSortingFields.Country),
                    SearchParameterField.semantics(CigarSortingFields.Gauge)
                )
            )
            //onNode(hasContentDescriptionExactly(Localize.screen_list_filter_control_descr)).printToLog("Filter")
            //sleep(5000)
            //Check remove field button
            assertHasNodes(
                Localize.screen_list_filter_control_descr,
                listOf(
                    SearchParameterField.semantics(CigarSortingFields.Name, SearchParameterField.LEADING_TAG),
                    SearchParameterField.semantics(CigarSortingFields.Brand, SearchParameterField.LEADING_TAG),
                    SearchParameterField.semantics(CigarSortingFields.Country, SearchParameterField.LEADING_TAG),
                    SearchParameterField.semantics(CigarSortingFields.Gauge, SearchParameterField.LEADING_TAG),
                )
            )
            //Remove last field
            onNodeWithContentDescription(
                SearchParameterField.semantics(
                    CigarSortingFields.Gauge,
                    SearchParameterField.LEADING_TAG
                )
            ).performClick()
            assertHasNodes(
                Localize.screen_list_filter_control_descr,
                listOf(
                    SearchParameterField.semantics(CigarSortingFields.Name, SearchParameterField.LEADING_TAG),
                    SearchParameterField.semantics(CigarSortingFields.Brand, SearchParameterField.LEADING_TAG),
                    SearchParameterField.semantics(CigarSortingFields.Country, SearchParameterField.LEADING_TAG),
                )
            )

            //Remove first field
            onNodeWithContentDescription(
                SearchParameterField.semantics(
                    CigarSortingFields.Name,
                    SearchParameterField.LEADING_TAG
                )
            ).performClick()
            assertHasNodes(
                Localize.screen_list_filter_control_descr,
                listOf(
                    SearchParameterField.semantics(CigarSortingFields.Brand, SearchParameterField.LEADING_TAG),
                    SearchParameterField.semantics(CigarSortingFields.Country, SearchParameterField.LEADING_TAG),
                )
            )

            //Check menu again
            pressButton(Localize.button_title_add_search_field)
            assertListOrder(
                Localize.filter_control_add_field_menu_descr, listOf(
                    CigarSortingFields.localized(CigarSortingFields.Name),
                    CigarSortingFields.localized(CigarSortingFields.Shape),
                    CigarSortingFields.localized(CigarSortingFields.Gauge),
                    CigarSortingFields.localized(CigarSortingFields.Length)
                )
            )
            //Add name remove other
            selectMenuItem(Localize.filter_control_add_field_menu_descr, 0, CigarSortingFields.localized(CigarSortingFields.Name))
            onNodeWithContentDescription(
                SearchParameterField.semantics(
                    CigarSortingFields.Brand,
                    SearchParameterField.LEADING_TAG
                )
            ).performClick()
            onNodeWithContentDescription(
                SearchParameterField.semantics(
                    CigarSortingFields.Country,
                    SearchParameterField.LEADING_TAG
                )
            ).performClick()

            //Check fields
            assertHasNodes(
                Localize.screen_list_filter_control_descr,
                listOf(SearchParameterField.semantics(CigarSortingFields.Name))
            )
            assertNoNodes(
                Localize.screen_list_filter_control_descr,
                listOf(
                    SearchParameterField.semantics(CigarSortingFields.Country),
                    SearchParameterField.semantics(CigarSortingFields.Brand),
                    SearchParameterField.semantics(CigarSortingFields.Shape),
                    SearchParameterField.semantics(CigarSortingFields.Gauge),
                    SearchParameterField.semantics(CigarSortingFields.Length)
                )
            )

            //Check name search
            assertSearchingBy(
                CigarSortingFields.Name,
                -1,
                listOf(Pair("Fuente", 1), Pair("", 5), Pair("Serie", 2), Pair("", 5)),
                null
            )

            //Check brand search
            assertSearchingBy(
                CigarSortingFields.Brand,
                0,
                listOf(Pair("Fabrica de", 1), Pair("", 5), Pair("Tabolisa", 2), Pair("", 5)),
                CigarSortingFields.Name
            )

            //Check country search
            assertSearchingBy(
                CigarSortingFields.Country,
                1,
                listOf(Pair("Dominican", 1), Pair("", 5), Pair("Nicaragua", 4), Pair("", 5)),
                CigarSortingFields.Brand
            )

            //Check country + gauge search
            pressButton(Localize.button_title_add_search_field)
            selectMenuItem(Localize.filter_control_add_field_menu_descr, 3, CigarSortingFields.localized(CigarSortingFields.Gauge))
            assertHasNodes(
                Localize.screen_list_filter_control_descr,
                listOf(
                    SearchParameterField.semantics(CigarSortingFields.Country),
                    SearchParameterField.semantics(CigarSortingFields.Gauge)
                )
            )
            replaceText(
                SearchParameterField.semantics(CigarSortingFields.Gauge, SearchParameterField.INPUT_FIELD_TAG),
                CigarSortingFields.localized(CigarSortingFields.Gauge),
                "52"
            )
            sleep(1000)
            onNodeWithContentDescription(LIST_TAG).performScrollToIndex(3)
            onNodeWithContentDescription(LIST_TAG).onChildren().assertCountEquals(4)
            replaceText(
                SearchParameterField.semantics(CigarSortingFields.Gauge, SearchParameterField.INPUT_FIELD_TAG),
                CigarSortingFields.localized(CigarSortingFields.Gauge),
                "60"
            )
            sleep(1000)
            onNodeWithContentDescription(LIST_TAG).performScrollToIndex(0)
            onNodeWithContentDescription(LIST_TAG).onChildren().assertCountEquals(1)

            replaceText(
                SearchParameterField.semantics(CigarSortingFields.Country, SearchParameterField.INPUT_FIELD_TAG),
                CigarSortingFields.localized(CigarSortingFields.Country),
                "Dominican"
            )
            sleep(1000)
            onNodeWithContentDescription(LIST_TAG).assertDoesNotExist()

        }
    }
}