/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/14/24, 6:57 PM
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
import assertNodeState
import com.akellolcc.cigars.databases.loadDemoSet
import com.akellolcc.cigars.databases.models.Cigar
import com.akellolcc.cigars.screens.navigation.CigarHistoryRoute
import com.akellolcc.cigars.screens.navigation.CigarsDetailsRoute
import com.akellolcc.cigars.screens.navigation.CigarsRoute
import com.akellolcc.cigars.screens.navigation.NavRoute
import com.akellolcc.cigars.theme.AssetFiles
import com.akellolcc.cigars.theme.Localize
import com.akellolcc.cigars.utils.CigarDetailsUtils
import com.akellolcc.cigars.utils.assertValuesCard
import com.akellolcc.cigars.utils.screenListContentDescription
import org.junit.FixMethodOrder
import org.junit.runners.MethodSorters
import pressBackButton
import pressButton
import sleep
import waitForScreen
import kotlin.test.Test

@Suppress("SameParameterValue")
@OptIn(androidx.compose.ui.test.ExperimentalTestApi::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
open class CigarDetailsTests : CigarDetailsUtils() {
    override var route: NavRoute = CigarsDetailsRoute
    override fun setUp() {
        with(composeTestRule) {
            waitForScreen(CigarsRoute)
            //Check items displayed
            assertListOrder(screenListContentDescription(CigarsRoute), listOf("#1", "#2", "#3", "#4", "#5"))
            assertListNode(screenListContentDescription(CigarsRoute), "#1").performClick()
            sleep(3000)
            waitForScreen(route)
        }
    }

    @Test
    fun test1_displayCigarDetails() {
        val demoCigars: List<Cigar> = loadDemoSet(AssetFiles.demo_cigars)
        val cigar = demoCigars[0]
        displayCigarDetails(cigar, 1)
    }


    @Test
    fun test2_cigarRating() {
        //Check cigar ratings
        setRating(0, 50)
        setRating(50, 0)
    }


    @Test
    fun test3_cigarHumidors() {
        with(composeTestRule) {

            onNodeWithContentDescription(Localize.cigar_details_humidors_block_desc).performScrollTo()
            //Check cigar humidors
            assertValuesCard(Localize.cigar_details_humidors_block_desc, Localize.cigar_details_humidors, true)
            assertListOrder(Localize.cigar_details_humidors_block_desc, listOf("Total number of cigars: 10", "Case"))


            //Move 5 cigars from Case to Second
            moveCigars(
                "Case Elegance Renzo Humidor",
                10,
                "Second",
                4,
                listOf("Case Elegance Renzo Humidor"),
                listOf("Second"),
                listOf("Total number of cigars: 10", "Case Elegance Renzo Humidor", "Second"),
                listOf("6", "4"),
            )
            moveCigars(
                "Second",
                4,
                "Case Elegance Renzo Humidor",
                4,
                listOf("Case Elegance Renzo Humidor", "Second"),
                listOf("Case Elegance Renzo Humidor"),
                listOf("Total number of cigars: 10", "Case Elegance Renzo Humidor"),
                listOf("10"),
            )
            moveCigars(
                "Case Elegance Renzo Humidor",
                10,
                "Second",
                3,
                listOf("Case Elegance Renzo Humidor"),
                listOf("Second"),
                listOf("Total number of cigars: 10", "Case Elegance Renzo Humidor", "Second"),
                listOf("7", "3"),
            )
            moveCigars(
                "Second",
                3,
                "Case Elegance Renzo Humidor",
                1,
                listOf("Case Elegance Renzo Humidor", "Second"),
                listOf("Case Elegance Renzo Humidor"),
                listOf("Total number of cigars: 10", "Case Elegance Renzo Humidor", "Second"),
                listOf("8", "2"),
            )
        }

    }

    @Test
    fun test4_addCigarsToHumidor() {
        with(composeTestRule) {
            //Check cigar humidors
            assertListOrder(Localize.cigar_details_humidors_block_desc, listOf("Total number of cigars: 10", "Case"))

            changeCigarsCount("Case Elegance Renzo Humidor", 1, 10, 5)
            changeCigarsCount("Case Elegance Renzo Humidor", 1, 5, 10)
        }

    }

    @Test
    fun test5_editCigarDetails() {
        val demoCigars: List<Cigar> = loadDemoSet(AssetFiles.demo_cigars)
        val updatedCigars: List<Cigar> = loadDemoSet(AssetFiles.test_cigars)
        val cigar = demoCigars[0]
        val updated = updatedCigars[0]
        with(composeTestRule) {
            onNodeWithContentDescription(Localize.cigar_details_top_bar_edit_desc).performClick()
            checkEditCigarDetails(cigar)
            editCigarDetails(cigar, updated)
            onNodeWithContentDescription(Localize.cigar_details_origin_block_desc).performScrollTo()
            checkEditCigarDetails(updated)
            pressButton(Localize.button_save)
            sleep(1000)
            displayCigarDetails(updated, 1)
        }
    }

    @Test
    fun test6_cigarHistory() {
        with(composeTestRule) {
            onNodeWithContentDescription(Localize.cigar_details_top_bar_history_desc).assertExists().performClick()
            waitForScreen(CigarHistoryRoute)

            assertListOrder(screenListContentDescription(CigarHistoryRoute), listOf("Added 10 cigars"))

            pressBackButton()
            waitForScreen(CigarsDetailsRoute)

            changeCigarsCount("Case Elegance Renzo Humidor", 1, 10, 11)

            onNodeWithContentDescription(Localize.cigar_details_top_bar_history_desc).assertExists().performClick()
            waitForScreen(CigarHistoryRoute)

            assertListOrder(screenListContentDescription(CigarHistoryRoute), listOf("Added 1 cigar", "Added 10 cigars"))

            pressBackButton()
            waitForScreen(CigarsDetailsRoute)

            changeCigarsCount("Case Elegance Renzo Humidor", 1, 11, 10)

            onNodeWithContentDescription(Localize.cigar_details_top_bar_history_desc).assertExists().performClick()
            waitForScreen(CigarHistoryRoute)

            assertListOrder(screenListContentDescription(CigarHistoryRoute), listOf("Removed 1 cigar", "Added 1 cigar", "Added 10 cigars"))

            pressBackButton()
            waitForScreen(CigarsDetailsRoute)

            moveCigars(
                "Case Elegance Renzo Humidor",
                10,
                "Second",
                4,
                listOf("Case Elegance Renzo Humidor"),
                listOf("Second"),
                listOf("Total number of cigars: 10", "Case Elegance Renzo Humidor", "Second"),
                listOf("6", "4"),
            )

            onNodeWithContentDescription(Localize.cigar_details_top_bar_history_desc).assertExists().performClick()
            waitForScreen(CigarHistoryRoute)

            assertListOrder(
                screenListContentDescription(CigarHistoryRoute),
                listOf("Moved 4 cigars", "Removed 1 cigar", "Added 1 cigar", "Added 10 cigars")
            )
        }
    }

    @Test
    fun test7_cigarImages() {
        with(composeTestRule) {
            onNodeWithContentDescription(Localize.cigar_details_images_block_desc).assertExists()
            assertNodeState(Localize.cigar_details_images_block_desc, Localize.images_pager_list_desc, "0:1")
        }
    }

}