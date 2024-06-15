/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/14/24, 7:20 PM
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

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import assertListNode
import assertListOrder
import assertNodeState
import com.akellolcc.cigars.databases.loadDemoSet
import com.akellolcc.cigars.databases.models.Cigar
import com.akellolcc.cigars.databases.models.emptyCigar
import com.akellolcc.cigars.screens.navigation.CigarsDetailsRoute
import com.akellolcc.cigars.screens.navigation.CigarsRoute
import com.akellolcc.cigars.screens.navigation.HumidorCigarsRoute
import com.akellolcc.cigars.screens.navigation.HumidorDetailsRoute
import com.akellolcc.cigars.screens.navigation.HumidorHistoryRoute
import com.akellolcc.cigars.screens.navigation.HumidorsRoute
import com.akellolcc.cigars.screens.navigation.NavRoute
import com.akellolcc.cigars.theme.AssetFiles
import com.akellolcc.cigars.theme.Localize
import com.akellolcc.cigars.utils.assertValuesCard
import com.akellolcc.cigars.utils.assertValuesCardValues
import com.akellolcc.cigars.utils.childWithTextLabel
import com.akellolcc.cigars.utils.screenListContentDescription
import org.junit.FixMethodOrder
import org.junit.runners.MethodSorters
import pressButton
import replaceText
import selectMenuItem
import selectTab
import sleep
import waitForScreen
import kotlin.test.Test

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class HumidorCigarsListTest : CigarsListTests() {
    override var route: NavRoute = HumidorCigarsRoute
    override fun setUp() {
        with(composeTestRule) {
            waitForScreen(CigarsRoute)
            assertListOrder(screenListContentDescription(CigarsRoute), listOf("#1", "#2", "#3", "#4", "#5"))

            selectTab(HumidorsRoute)
            waitForScreen(HumidorsRoute)
            assertListOrder(screenListContentDescription(HumidorsRoute), listOf("Case Elegance Renzo Humidor", "Second"))

            assertListNode(screenListContentDescription(HumidorsRoute), "Case Elegance").performClick()
            waitForScreen(HumidorCigarsRoute)
        }
    }

    @Test
    fun test01_HumidorCigarsList() {
        with(composeTestRule) {
            assertListOrder(screenListContentDescription(HumidorCigarsRoute), listOf("#1", "#2", "#3", "#4", "#5"))

            assertListNode(HumidorCigarsRoute.semantics, Localize.humidor_cigars(50, 0))
        }
    }

    @Test
    fun test02_HumidorHistory() {
        with(composeTestRule) {
            onNodeWithContentDescription(Localize.screen_list_sort_fields_action_descr).assertExists().performClick()
            selectMenuItem(Localize.screen_list_sort_fields_list_descr, Localize.title_humidor_history)
            waitForScreen(HumidorHistoryRoute)

            assertListOrder(screenListContentDescription(HumidorHistoryRoute), listOf("#5", "#4", "#3", "#2", "#1", "Added humidor"))
        }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun test03_displayHumidorDetails() {
        with(composeTestRule) {
            onNodeWithContentDescription(Localize.screen_list_sort_fields_action_descr).assertExists().performClick()
            selectMenuItem(Localize.screen_list_sort_fields_list_descr, Localize.title_humidor_details_menu_desc)

            waitForScreen(HumidorDetailsRoute)

            //Check images carousel
            assertNodeState(Localize.cigar_details_images_block_desc, Localize.images_pager_list_desc, "0:3")

            //Check humidor details
            childWithTextLabel(
                Localize.humidor_details_origin_block_desc,
                Localize.cigar_details_name,
                "Case Elegance Renzo Humidor"
            ).assertExists()

            childWithTextLabel(
                Localize.humidor_details_origin_block_desc,
                Localize.cigar_details_company,
                "Klaro"
            ).assertExists()

            //Check humidor capacity
            assertValuesCard(Localize.humidor_details_size_block_desc, Localize.humidor_details_cigars)
            assertValuesCardValues(
                Localize.humidor_details_size_block_desc,
                mapOf(
                    Localize.humidor_details_holds to "50",
                    Localize.humidor_details_count to "50"
                )
            )

            //Check humidor details
            assertValuesCard(Localize.humidor_details_params_block_desc, Localize.humidor_details_humidor)
            assertValuesCardValues(
                Localize.humidor_details_params_block_desc,
                mapOf(
                    Localize.humidor_details_temperature to "72",
                    Localize.humidor_details_humidity to "72.0"
                )
            )

            //Check cigar notes
            onNodeWithContentDescription(Localize.humidor_details_notes_block_desc).assertExists().onChildren().assertCountEquals(2)
        }
    }

    @Test
    fun test04_editHumidorDetails() {
        with(composeTestRule) {
            onNodeWithContentDescription(Localize.screen_list_sort_fields_action_descr).assertExists().performClick()
            selectMenuItem(Localize.screen_list_sort_fields_list_descr, Localize.title_humidor_details_menu_desc)

            waitForScreen(HumidorDetailsRoute)

            onNodeWithContentDescription(Localize.cigar_details_top_bar_edit_desc).performClick()

            //Check images carousel
            onNodeWithContentDescription(Localize.cigar_details_images_block_desc).assertDoesNotExist()

            //Check humidor details
            childWithTextLabel(
                Localize.humidor_details_origin_block_desc,
                Localize.cigar_details_name,
                "Case Elegance Renzo Humidor"
            ).assertExists()

            childWithTextLabel(
                Localize.humidor_details_origin_block_desc,
                Localize.cigar_details_company,
                "Klaro"
            ).assertExists()


            //Check humidor capacity
            assertValuesCard(Localize.humidor_details_size_block_desc, Localize.humidor_details_cigars, true)
            childWithTextLabel(
                Localize.humidor_details_size_block_desc,
                Localize.humidor_details_holds,
                "50"
            ).assertExists()

            childWithTextLabel(
                Localize.humidor_details_size_block_desc,
                Localize.humidor_details_count,
                "50"
            ).assertExists()

            //Check humidor details
            assertValuesCard(Localize.humidor_details_params_block_desc, Localize.humidor_details_humidor, true)
            childWithTextLabel(
                Localize.humidor_details_params_block_desc,
                Localize.humidor_details_temperature,
                "72"
            ).assertExists()

            childWithTextLabel(
                Localize.humidor_details_params_block_desc,
                Localize.humidor_details_humidity,
                "72.0"
            ).assertExists()

            //Check cigar notes
            onNodeWithContentDescription(Localize.humidor_details_notes_block_desc).assertExists().onChildren().assertCountEquals(2)

            replaceText(
                Localize.humidor_details_params_block_desc,
                Localize.humidor_details_humidity,
                "60",
                "72.0"
            )

            pressButton(Localize.button_save)

            assertValuesCard(Localize.humidor_details_params_block_desc, Localize.humidor_details_humidor)
            assertValuesCardValues(
                Localize.humidor_details_params_block_desc,
                mapOf(
                    Localize.humidor_details_temperature to "72",
                    Localize.humidor_details_humidity to "60.0"
                )
            )
        }
    }

    @Test
    fun test05_addCigarDetails() {
        val cigars: List<Cigar> = loadDemoSet(AssetFiles.test_cigars)
        with(composeTestRule) {
            onNodeWithContentDescription(Localize.humidor_cigars_list_add_action_desc).assertExists().performClick()
            sleep(5000)
            editCigarDetails(emptyCigar, cigars[0])
            onNodeWithContentDescription(Localize.cigar_details_origin_block_desc).performScrollTo()
            checkEditCigarDetails(cigars[0])
            pressButton(Localize.button_save)
            sleep(1000)
            route = CigarsDetailsRoute
            displayCigarDetails(cigars[0], 0)
        }
    }

}