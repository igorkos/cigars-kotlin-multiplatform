/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/10/24, 12:28 PM
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
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.printToLog
import assertListNode
import assertListOrder
import assertNodeState
import com.akellolcc.cigars.databases.models.CigarShapes
import com.akellolcc.cigars.databases.models.CigarStrength
import com.akellolcc.cigars.screens.navigation.CigarHistoryRoute
import com.akellolcc.cigars.screens.navigation.CigarsDetailsRoute
import com.akellolcc.cigars.screens.navigation.CigarsRoute
import com.akellolcc.cigars.screens.navigation.NavRoute
import com.akellolcc.cigars.theme.Localize
import com.akellolcc.cigars.utils.BaseUiTest
import com.akellolcc.cigars.utils.assertHasChildWithText
import com.akellolcc.cigars.utils.assertHasNodes
import com.akellolcc.cigars.utils.assertNoNodes
import com.akellolcc.cigars.utils.assertPickerValues
import com.akellolcc.cigars.utils.assertValuePicker
import com.akellolcc.cigars.utils.assertValuesCard
import com.akellolcc.cigars.utils.assertValuesCardValues
import com.akellolcc.cigars.utils.childWithTextLabel
import com.akellolcc.cigars.utils.getListRow
import com.akellolcc.cigars.utils.performSelectValuePicker
import com.akellolcc.cigars.utils.performValuesCardAction
import com.akellolcc.cigars.utils.screenListContentDescription
import org.junit.FixMethodOrder
import org.junit.runners.MethodSorters
import pressBackButton
import pressButton
import replaceText
import waitForDialog
import waitForScreen
import kotlin.test.Test

@Suppress("SameParameterValue")
@OptIn(androidx.compose.ui.test.ExperimentalTestApi::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
open class CigarDetailsTests : BaseUiTest() {
    override var route: NavRoute = CigarsDetailsRoute
    override fun setUp() {
        with(composeTestRule) {
            waitForScreen(CigarsRoute)
            //Check items displayed
            assertListOrder(screenListContentDescription(CigarsRoute), listOf("#1", "#2", "#3", "#4", "#5"))
            assertListNode(screenListContentDescription(CigarsRoute), "#1").performClick()
            waitForScreen(route)
        }
    }

    @Test
    fun test1_displayCigarDetails() {
        with(composeTestRule) {

            //Check images carousel
            assertNodeState(Localize.cigar_details_images_block_desc, Localize.images_pager_list_desc, "0:1")

            //Check cigar details
            childWithTextLabel(
                Localize.cigar_details_origin_block_desc,
                Localize.cigar_details_name,
                "#1 Fuente Fuente OpusX Reserva d’Chateau"
            ).assertExists()
            childWithTextLabel(
                Localize.cigar_details_origin_block_desc,
                Localize.cigar_details_company,
                "Fabrica de Tabacos Raices Cubanas S. de R.L."
            ).assertExists()
            childWithTextLabel(Localize.cigar_details_origin_block_desc, Localize.cigar_details_country, "Dominican").assertExists()


            //Check cigar size
            assertValuesCard(Localize.cigar_details_size_block_desc, Localize.cigar_details_cigars)
            performValuesCardAction(Localize.cigar_details_size_block_desc)

            waitForDialog(Localize.cigar_details_size_info_dialog_desc).performClick()
            waitForScreen(route)
            assertValuesCardValues(
                Localize.cigar_details_size_block_desc,
                mapOf(
                    Localize.cigar_details_shape to "Churchill",
                    Localize.cigar_details_length to "7'",
                    Localize.cigar_details_gauge to "48"
                )
            )

            //Check cigar tobacco
            assertValuesCard(Localize.cigar_details_tobacco_block_desc, Localize.cigar_details_tobacco, true)
            performValuesCardAction(Localize.cigar_details_tobacco_block_desc)

            waitForDialog(Localize.cigar_details_tobacco_info_dialog_desc).performClick()
            waitForScreen(route)


            childWithTextLabel(
                Localize.cigar_details_tobacco_block_desc,
                Localize.cigar_details_wrapper, "Dominican",
                substring = true
            ).assertExists()
            childWithTextLabel(
                Localize.cigar_details_tobacco_block_desc,
                Localize.cigar_details_binder, "Dominican",
                substring = true
            ).assertExists()
            childWithTextLabel(
                Localize.cigar_details_tobacco_block_desc,
                Localize.cigar_details_filler, "Dominican",
                substring = true
            ).assertExists()
            childWithTextLabel(
                Localize.cigar_details_tobacco_block_desc,
                Localize.cigar_details_strength, "Medium-Full",
                substring = true
            ).assertExists()

            //Check cigar ratings
            assertValuesCard(Localize.cigar_details_ratings_block_desc, Localize.cigar_details_ratings)
            performValuesCardAction(Localize.cigar_details_ratings_block_desc)

            waitForDialog(Localize.cigar_details_ratings_info_dialog_desc).performClick()
            waitForScreen(route)
            assertValuesCardValues(
                Localize.cigar_details_ratings_block_desc,
                mapOf(
                    Localize.cigar_details_rating to "97",
                    Localize.cigar_details_myrating to "0"
                )
            )

            //Check cigar Humidors
            assertValuesCard(Localize.cigar_details_humidors_block_desc, Localize.cigar_details_humidors, true)
            assertListOrder(Localize.cigar_details_humidors_block_desc, listOf("Total number of cigars: 10", "Case"))

            //Check cigar notes
            onNodeWithContentDescription(Localize.cigar_details_notes_block_desc).assertExists().onChildren().assertCountEquals(2)
        }
    }

    @Test
    fun test2_cigarRating() {
        //Check cigar ratings
        setRating(0, 50)
        setRating(50, 0)
    }

    private fun setRating(current: Int, rating: Int) {
        with(composeTestRule) {
            onNodeWithContentDescription(Localize.cigar_details_ratings_block_desc).performScrollTo()
            childWithTextLabel(
                Localize.cigar_details_ratings_block_desc,
                Localize.cigar_details_myrating,
                current.toString(),
                substring = true
            ).assertExists().performClick()

            waitForDialog(Localize.cigar_details_rating_dialog)

            childWithTextLabel(
                Localize.cigar_details_rating_dialog,
                Localize.cigar_details_myrating,
                current.toString(),
                substring = true
            ).assertExists()

            replaceText(
                Localize.cigar_details_rating_dialog,
                Localize.cigar_details_myrating,
                rating.toString(),
                current.toString()
            )

            pressButton(Localize.button_save)

            waitForScreen(route)

            assertValuesCardValues(
                Localize.cigar_details_ratings_block_desc,
                mapOf(
                    Localize.cigar_details_myrating to rating.toString()
                )
            )
        }
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

    private fun moveCigars(
        from: String,
        fromCount: Int,
        to: String,
        moveCount: Int,
        fromHumidors: List<String>,
        toHumidors: List<String>,
        result: List<String>,
        resultCount: List<String>
    ) {
        with(composeTestRule) {
            //Open dialog
            performValuesCardAction(Localize.cigar_details_humidors_block_desc)

            waitForDialog(Localize.cigar_details_move_dialog)

            //Check dialog content
            if (fromHumidors.size == 1 && toHumidors.size == 1) {
                assertValuePicker(Localize.cigar_details_humidors_move_dialog_from_desc, from, false)
                assertValuePicker(Localize.cigar_details_humidors_move_dialog_to_desc, to, false)
                childWithTextLabel(
                    Localize.cigar_details_move_dialog,
                    Localize.cigar_details_humidors_move_dialog_count_desc,
                    fromCount.toString(),
                    substring = true
                )
            } else if (fromHumidors.size == 1 && toHumidors.size > 1) {
                assertValuePicker(Localize.cigar_details_humidors_move_dialog_from_desc, from, false)
                assertValuePicker(Localize.cigar_details_humidors_move_dialog_to_desc, Localize.cigar_details_move_select, false)
                childWithTextLabel(
                    Localize.cigar_details_move_dialog,
                    Localize.cigar_details_humidors_move_dialog_count_desc,
                    fromCount.toString(),
                    substring = true
                )
            } else {
                assertValuePicker(Localize.cigar_details_humidors_move_dialog_from_desc, Localize.cigar_details_move_select, false)
                assertValuePicker(Localize.cigar_details_humidors_move_dialog_to_desc, Localize.cigar_details_move_select, false)
                childWithTextLabel(
                    Localize.cigar_details_move_dialog,
                    Localize.cigar_details_humidors_move_dialog_count_desc,
                    "1",
                    substring = true
                )
            }

            //Move cigars
            if (fromHumidors.size > 1) {
                //Select from
                performSelectValuePicker(Localize.cigar_details_humidors_move_dialog_from_desc, from)
            }

            if (toHumidors.size - fromHumidors.size > 1) {
                //Select to
                performSelectValuePicker(Localize.cigar_details_humidors_move_dialog_to_desc, to)
            }

            //Set move count
            replaceText(
                Localize.cigar_details_move_dialog,
                Localize.cigar_details_humidors_move_dialog_count_desc,
                moveCount.toString(),
                fromCount.toString()
            )

            //Move cigars
            pressButton(Localize.button_save)
            waitForScreen(route)
            onNodeWithContentDescription(Localize.cigar_details_humidors_block_desc).performScrollTo()

            //Check cigars moved
            assertListOrder(Localize.cigar_details_humidors_block_desc, result)
            assertListOrder(Localize.cigar_details_humidors_block_desc, resultCount, substring = false)
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

    private fun changeCigarsCount(humidor: String, index: Int, from: Int, to: Int) {
        with(composeTestRule) {
            onNodeWithContentDescription(Localize.cigar_details_humidors_block_desc).performScrollTo()

            val node = onNodeWithContentDescription(Localize.cigar_details_humidors_block_list_desc).onChildren().getListRow(index)
            node.assertHasChildWithText(humidor)
            node.assertHasChildWithText(from.toString())
            node.onChildren().filterToOne(hasAnyDescendant(hasText(from.toString()))).performClick()

            waitForDialog(Localize.cigar_details_count_dialog)

            childWithTextLabel(Localize.cigar_details_count_dialog, Localize.cigar_search_details_add_count_dialog, from.toString())

            if (from > to) {
                //Minus
                for (i in to..<from) {
                    onNodeWithContentDescription(Localize.cigar_details_humidors_count_dialog_minus_desc).performClick()
                }
                childWithTextLabel(Localize.cigar_details_count_dialog, Localize.cigar_search_details_add_count_dialog, to.toString())
                assertNoNodes(Localize.cigar_details_count_dialog, listOf(Localize.cigar_details_count_dialog_price))
            } else {
                //Plus
                for (i in from..<to) {
                    onNodeWithContentDescription(Localize.cigar_details_humidors_count_dialog_plus_desc).performClick()
                }
                childWithTextLabel(Localize.cigar_details_count_dialog, Localize.cigar_search_details_add_count_dialog, to.toString())
                assertHasNodes(Localize.cigar_details_count_dialog, listOf(Localize.cigar_details_count_dialog_price))
                childWithTextLabel(Localize.cigar_details_count_dialog, Localize.cigar_details_count_dialog_price, "0.00")
                replaceText(Localize.cigar_details_count_dialog, Localize.cigar_details_count_dialog_price, "100", "0.00")
                childWithTextLabel(Localize.cigar_details_count_dialog, Localize.cigar_details_count_dialog_price, "1.00")
            }

            pressButton(Localize.button_save)
            waitForScreen(route)

            assertListOrder(Localize.cigar_details_humidors_block_desc, listOf("Total number of cigars: $to", humidor))
            assertListOrder(Localize.cigar_details_humidors_block_desc, listOf("$to"), substring = false)
        }
    }

    @Test
    fun test5_editCigarDetails() {
        with(composeTestRule) {
            onNodeWithContentDescription(Localize.cigar_details_top_bar_edit_desc).performClick()

            //Check images carousel
            onNodeWithContentDescription(Localize.cigar_details_images_block_desc).assertDoesNotExist()

            //Check cigar details
            childWithTextLabel(
                Localize.cigar_details_origin_block_desc,
                Localize.cigar_details_name,
                "#1 Fuente Fuente OpusX Reserva d’Chateau"
            ).assertExists()
            childWithTextLabel(
                Localize.cigar_details_origin_block_desc,
                Localize.cigar_details_company,
                "Fabrica de Tabacos Raices Cubanas S. de R.L."
            ).assertExists()
            childWithTextLabel(
                Localize.cigar_details_origin_block_desc,
                Localize.cigar_details_country,
                "Dominican"
            ).assertExists()


            //Check cigar size
            onNodeWithContentDescription(Localize.cigar_details_size_block_desc).performScrollTo()
            assertValuePicker(Localize.cigar_details_shape, "Churchill", false)
            assertPickerValues(
                Localize.cigar_details_shape,
                "Churchill",
                CigarShapes.enumValues().map { it.second },
            )
            assertValuePicker(Localize.cigar_details_shape, "Churchill", false)

            childWithTextLabel(
                Localize.cigar_details_size_block_desc,
                Localize.cigar_details_length,
                "7'",
                true
            ).assertExists()

            childWithTextLabel(
                Localize.cigar_details_size_block_desc,
                Localize.cigar_details_gauge,
                "48",
                true
            ).assertExists()

            //Check cigar tobacco
            onNodeWithContentDescription(Localize.cigar_details_ratings_block_desc).performScrollTo()
            childWithTextLabel(
                Localize.cigar_details_tobacco_block_desc,
                Localize.cigar_details_wrapper, "Dominican",
                substring = true
            ).assertExists()
            childWithTextLabel(
                Localize.cigar_details_tobacco_block_desc,
                Localize.cigar_details_binder, "Dominican",
                substring = true
            ).assertExists()
            childWithTextLabel(
                Localize.cigar_details_tobacco_block_desc,
                Localize.cigar_details_filler, "Dominican",
                substring = true
            ).assertExists()

            assertValuePicker(Localize.cigar_details_strength, CigarStrength.localized(CigarStrength.MediumToFull), false).printToLog("")
            assertPickerValues(
                Localize.cigar_details_strength,
                CigarStrength.localized(CigarStrength.MediumToFull),
                CigarStrength.enumValues().map { it.second },
            )
            assertValuePicker(Localize.cigar_details_strength, CigarStrength.localized(CigarStrength.MediumToFull), false)


            //Check cigar ratings
            onNodeWithContentDescription(Localize.cigar_details_notes_block_desc).performScrollTo()
            childWithTextLabel(
                Localize.cigar_details_ratings_block_desc,
                Localize.cigar_details_rating,
                "97",
                substring = true
            ).assertExists()
            childWithTextLabel(
                Localize.cigar_details_ratings_block_desc,
                Localize.cigar_details_myrating,
                "0",
                substring = true
            ).assertExists()


            //Check cigar Humidors
            onNodeWithContentDescription(Localize.cigar_details_humidors_block_desc).assertDoesNotExist()

            //Check cigar notes
            onNodeWithContentDescription(Localize.cigar_details_notes_block_desc).assertExists().onChildren().assertCountEquals(2)

            //Change wrapper
            onNodeWithContentDescription(Localize.cigar_details_tobacco_block_desc).performScrollTo()

            replaceText(
                Localize.cigar_details_tobacco_block_desc,
                Localize.cigar_details_wrapper,
                "Nicaragua",
                "Dominican"
            )

            pressButton(Localize.button_save)

            childWithTextLabel(
                Localize.cigar_details_tobacco_block_desc,
                Localize.cigar_details_wrapper, "Nicaragua",
                substring = true
            ).assertExists()
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