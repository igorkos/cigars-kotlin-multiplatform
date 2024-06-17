/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/15/24, 12:25 PM
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

import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import assertListNode
import assertListOrder
import com.akellolcc.cigars.databases.loadDemoSet
import com.akellolcc.cigars.databases.models.Cigar
import com.akellolcc.cigars.databases.models.Humidor
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
import com.akellolcc.cigars.utils.screenListContentDescription
import org.junit.FixMethodOrder
import org.junit.runners.MethodSorters
import pressButton
import selectMenuItem
import selectTab
import sleep
import waitForScreen
import kotlin.test.Test

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class HumidorCigarsListTest : CigarsListTests() {
    override var route: NavRoute = HumidorCigarsRoute
    override fun setUp() {
        val humidors: List<Humidor> = loadDemoSet(AssetFiles.demo_humidors)
        with(composeTestRule) {
            waitForScreen(CigarsRoute)
            assertListOrder(screenListContentDescription(CigarsRoute), listOf("#1", "#2", "#3", "#4", "#5"))

            selectTab(HumidorsRoute)
            waitForScreen(HumidorsRoute)
            assertListOrder(screenListContentDescription(HumidorsRoute), humidors.map { it.name })

            assertListNode(screenListContentDescription(HumidorsRoute), humidors.first().name).performClick()
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

    @Test
    fun test03_displayHumidorDetails() {
        val humidors: List<Humidor> = humidors()
        with(composeTestRule) {
            onNodeWithContentDescription(Localize.screen_list_sort_fields_action_descr).assertExists().performClick()
            selectMenuItem(Localize.screen_list_sort_fields_list_descr, Localize.title_humidor_details_menu_desc)

            waitForScreen(HumidorDetailsRoute)
            humidors.forEach {
                assertDisplayHumidorDetails(it, 3)
            }
        }
    }

    @Test
    fun test04_editHumidorDetails() {
        val humidor: Humidor = humidors().first()
        val updated: Humidor = loadDemoSet<Humidor>(AssetFiles.test_humidors).first()
        with(composeTestRule) {
            onNodeWithContentDescription(Localize.screen_list_sort_fields_action_descr).assertExists().performClick()
            selectMenuItem(Localize.screen_list_sort_fields_list_descr, Localize.title_humidor_details_menu_desc)

            waitForScreen(HumidorDetailsRoute)

            onNodeWithContentDescription(Localize.cigar_details_top_bar_edit_desc).performClick()

            //Check humidor details
            assertHumidorDetailsEditing(humidor)

            editHumidorDetails(humidor, updated)

            pressButton(Localize.button_save)

            waitForScreen(HumidorDetailsRoute)

            assertDisplayHumidorDetails(updated, 3)
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