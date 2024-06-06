/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/5/24, 8:43 PM
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
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.printToLog
import assertListOrder
import com.akellolcc.cigars.databases.models.CigarShapes
import com.akellolcc.cigars.databases.models.CigarStrength
import com.akellolcc.cigars.screens.CigarDetailsScreen
import com.akellolcc.cigars.screens.CigarDetailsScreen.Companion.DIALOG_CIGAR_RATING
import com.akellolcc.cigars.screens.CigarDetailsScreen.Companion.DIALOG_MOVE_CIGARS
import com.akellolcc.cigars.screens.CigarDetailsScreen.Companion.HUMIDORS_BLOCK
import com.akellolcc.cigars.screens.CigarDetailsScreen.Companion.HUMIDORS_BLOCK_LIST
import com.akellolcc.cigars.screens.CigarDetailsScreen.Companion.ORIGIN_BLOCK
import com.akellolcc.cigars.screens.CigarDetailsScreen.Companion.RATINGS_BLOCK
import com.akellolcc.cigars.screens.CigarDetailsScreen.Companion.RATINGS_FAVORITE
import com.akellolcc.cigars.screens.CigarDetailsScreen.Companion.SIZE_BLOCK
import com.akellolcc.cigars.screens.CigarDetailsScreen.Companion.TOBACCO_BLOCK
import com.akellolcc.cigars.screens.base.BaseTabListScreen.Companion.LIST_TAG
import com.akellolcc.cigars.screens.components.INFO_IMAGE_DIALOG_TAG
import com.akellolcc.cigars.screens.components.ValuesCardTags
import com.akellolcc.cigars.screens.navigation.CigarHistoryRoute
import com.akellolcc.cigars.screens.navigation.CigarImagesViewRoute
import com.akellolcc.cigars.screens.navigation.CigarsDetailsRoute
import com.akellolcc.cigars.screens.navigation.CigarsRoute
import com.akellolcc.cigars.screens.navigation.FavoritesRoute
import com.akellolcc.cigars.theme.Localize
import com.akellolcc.cigars.utils.BaseUiTest
import com.akellolcc.cigars.utils.assertHasChildWithText
import com.akellolcc.cigars.utils.assertNodeText
import com.akellolcc.cigars.utils.assertPickerValues
import com.akellolcc.cigars.utils.assertValuesCard
import com.akellolcc.cigars.utils.assertValuesCardValues
import com.akellolcc.cigars.utils.childButtonWithText
import com.akellolcc.cigars.utils.childWithTag
import com.akellolcc.cigars.utils.childWithText
import com.akellolcc.cigars.utils.childWithTextLabel
import com.akellolcc.cigars.utils.dialogWithTag
import com.akellolcc.cigars.utils.getListRow
import com.akellolcc.cigars.utils.replaceText
import pressBackButton
import pressButton
import sleep
import textIsDisplayed
import waitForDialog
import waitForTag
import waitForText
import kotlin.test.Test

@Suppress("SameParameterValue")
@OptIn(androidx.compose.ui.test.ExperimentalTestApi::class)
open class CigarDetailsTests : BaseUiTest() {

    override fun setUp() {
        route = CigarsRoute
        with(composeTestRule) {
            waitForTag(tag())
            onNodeWithText("#1", true).performClick()
            route = CigarsDetailsRoute
            waitForTag(tag())
        }
    }

    @Test
    fun displayCigarDetailsTest() {
        with(composeTestRule) {

            //Check images carousel
            onNodeWithTag("ImagesCarousel").assertExists()
            onNodeWithTag("ImagesCarousel").onChildren().assertCountEquals(1)
            onNodeWithTag("ImagesCarouselItem--1").assertDoesNotExist()
            onNodeWithTag("ImagesCarouselItem-0").assertExists()

            //Check cigar details
            childWithText(tag(ORIGIN_BLOCK), "#1 Fuente Fuente OpusX Reserva d’Chateau").assertExists()
            childWithText(tag(ORIGIN_BLOCK), "Fabrica de Tabacos Raices Cubanas S. de R.L.").assertExists()
            childWithText(tag(ORIGIN_BLOCK), "Dominican").assertExists()

            //Check cigar size
            assertValuesCard(tag(SIZE_BLOCK), Localize.cigar_details_cigars)
            childWithTag(tag(SIZE_BLOCK), ValuesCardTags.VALUES_CARD_ACTION_TAG).performClick()
            waitForDialog(INFO_IMAGE_DIALOG_TAG).performClick()
            waitForTag(tag())
            assertValuesCardValues(
                tag(SIZE_BLOCK),
                mapOf(
                    Localize.cigar_details_shape to "Churchill",
                    Localize.cigar_details_length to "7'",
                    Localize.cigar_details_gauge to "48"
                )
            )

            //Check cigar tobacco
            assertValuesCard(tag(TOBACCO_BLOCK), Localize.cigar_details_tobacco, true)
            childWithTag(tag(TOBACCO_BLOCK), ValuesCardTags.VALUES_CARD_ACTION_TAG).performClick()
            waitForDialog(INFO_IMAGE_DIALOG_TAG).performClick()
            waitForTag(tag())

            childWithTextLabel(
                tag(TOBACCO_BLOCK),
                Localize.cigar_details_wrapper, "Dominican",
                substring = true
            ).assertExists()
            childWithTextLabel(
                tag(TOBACCO_BLOCK),
                Localize.cigar_details_binder, "Dominican",
                substring = true
            ).assertExists()
            childWithTextLabel(
                tag(TOBACCO_BLOCK),
                Localize.cigar_details_filler, "Dominican",
                substring = true
            ).assertExists()
            childWithTextLabel(
                tag(TOBACCO_BLOCK),
                Localize.cigar_details_strength, "Medium-Full",
                substring = true
            ).assertExists()

            //Check cigar ratings
            assertValuesCard(tag(RATINGS_BLOCK), Localize.cigar_details_ratings)
            childWithTag(tag(RATINGS_BLOCK), ValuesCardTags.VALUES_CARD_ACTION_TAG).performClick()
            waitForDialog(INFO_IMAGE_DIALOG_TAG).performClick()
            waitForTag(tag())
            assertValuesCardValues(
                tag(RATINGS_BLOCK),
                mapOf(
                    Localize.cigar_details_rating to "97",
                    Localize.cigar_details_myrating to "0"
                )
            )

            //Check cigar Humidors
            assertValuesCard(tag(HUMIDORS_BLOCK), Localize.cigar_details_humidors)
            assertListOrder(tag(HUMIDORS_BLOCK_LIST), listOf("Total number of cigars: 10", "Case"))

            //Check cigar notes
            onNodeWithTag(tag(CigarDetailsScreen.NOTES_BLOCK)).assertExists().onChildren().assertCountEquals(3)

        }
    }

    @Test
    fun cigarRatingTest() {
        //Check cigar ratings
        setRating(0, 50)
        setRating(50, 0)
    }

    private fun setRating(current: Int, rating: Int) {
        with(composeTestRule) {
            childWithTextLabel(
                tag(RATINGS_BLOCK),
                Localize.cigar_details_myrating, current.toString(),
                substring = true
            ).assertExists().performClick()

            waitForText(Localize.cigar_details_rating_dialog)
            dialogWithTag(tag(DIALOG_CIGAR_RATING)).assertExists()
            childWithText(tag(DIALOG_CIGAR_RATING), Localize.cigar_details_rating_dialog).assertExists()
            childWithText(tag(DIALOG_CIGAR_RATING), current.toString()).assertExists()
            childButtonWithText(tag(DIALOG_CIGAR_RATING), Localize.button_cancel).assertExists()
            childButtonWithText(tag(DIALOG_CIGAR_RATING), Localize.button_save).assertExists()

            childWithText(tag(DIALOG_CIGAR_RATING), current.toString()).performClick()
            sleep(500)
            childWithText(tag(DIALOG_CIGAR_RATING), current.toString()).replaceText(rating.toString())
            childButtonWithText(tag(DIALOG_CIGAR_RATING), Localize.button_save).performClick()
            childWithTextLabel(
                tag(RATINGS_BLOCK),
                Localize.cigar_details_myrating, rating.toString(),
                substring = true
            ).assertExists()
        }
    }

    @Test
    fun cigarFavoriteTest() {
        //Check cigar favorite
        setFavorite(true)
        setFavorite(false)
    }

    private fun setFavorite(favorite: Boolean) {
        with(composeTestRule) {
            childWithTag(
                tag(RATINGS_BLOCK),
                tag("$RATINGS_FAVORITE${!favorite}"),
                useUnmergedTree = true
            ).assertExists().performClick()

            childWithTag(
                tag(RATINGS_BLOCK),
                tag("$RATINGS_FAVORITE${favorite}")
            ).assertExists()

            pressBackButton()

            waitForTag(tag(route = CigarsRoute))

            pressButton(FavoritesRoute.title)
            route = FavoritesRoute
            waitForTag(tag())

            if (favorite) {
                assertListOrder(tag(LIST_TAG), listOf("#1"))
            } else {
                onNodeWithTag(tag(LIST_TAG)).assertDoesNotExist()
                textIsDisplayed(Localize.list_is_empty)
            }

            pressButton(CigarsRoute.title)
            waitForTag(tag(route = CigarsRoute))

            onNodeWithText("#1", true).performClick()
            route = CigarsDetailsRoute
            waitForTag(tag())
        }
    }

    @Test
    fun cigarHumidorsTest() {
        with(composeTestRule) {

            onNodeWithTag(tag(HUMIDORS_BLOCK)).performScrollTo()
            //Check cigar humidors
            assertListOrder(tag(HUMIDORS_BLOCK_LIST), listOf("Total number of cigars: 10", "Case"))

            //Move 5 cigars from Case to Second
            moveCigars(
                "Case Elegance Renzo Humidor",
                10,
                "Second",
                5,
                listOf("Case Elegance Renzo Humidor"),
                listOf("Second"),
                listOf("Total number of cigars: 10", "Case Elegance Renzo Humidor", "Second"),
                listOf("5", "5"),
            )
            moveCigars(
                "Second",
                5,
                "Case Elegance Renzo Humidor",
                5,
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
            childWithTag(tag(HUMIDORS_BLOCK), "ValuesCard-Action").assertExists().performClick()
            sleep(500)
            //Check dialog content
            childWithTag(tag(DIALOG_MOVE_CIGARS), tag("move_from_humidor")).assertExists()
            childWithTag(tag(DIALOG_MOVE_CIGARS), tag("move_to_humidor")).assertExists()
            childWithTag(tag(DIALOG_MOVE_CIGARS), tag("move_count")).assertExists()
            childButtonWithText(tag(DIALOG_MOVE_CIGARS), Localize.button_cancel).assertExists().assertIsEnabled()

            if (fromHumidors.size == 1 && toHumidors.size == 1) {
                childWithText(tag("move_from_humidor"), from).assertExists()
                assertNodeText(tag("move_count"), fromCount.toString())
                childWithText(tag("move_to_humidor"), to).assertExists()
            } else if (fromHumidors.size == 1 && toHumidors.size > 1) {
                childWithText(tag("move_from_humidor"), from).assertExists()
                assertNodeText(tag("move_count"), fromCount.toString())
                childWithText(tag("move_to_humidor"), "Select Humidor").assertExists()
            } else {
                childWithText(tag("move_from_humidor"), "Select Humidor").assertExists()
                assertNodeText(tag("move_count"), "1")
                childWithText(tag("move_to_humidor"), "Select Humidor").assertExists()
            }


            //Move cigars
            if (fromHumidors.size > 1) {
                //Open dropdown from
                childWithTag(tag("move_from_humidor"), "value_picker_drop_down").assertExists().performClick()
                //Select from
                assertListOrder("value_picker_list", fromHumidors)
                childWithText("value_picker_list", from, true).assertExists().performClick()
                childWithText(tag("move_from_humidor"), from).assertExists()
                onNodeWithTag("value_picker_list").assertDoesNotExist()
            }
            sleep(500)
            if (toHumidors.size - fromHumidors.size > 1) {
                //Open dropdown from
                childWithTag(tag("move_to_humidor"), "value_picker_drop_down").assertExists().performClick()
                //Select from
                assertListOrder("value_picker_list", toHumidors)
                childWithText("value_picker_list", to, true).assertExists().performClick()
                childWithText(tag("move_to_humidor"), to).assertExists()
                onNodeWithTag("value_picker_list").assertDoesNotExist()
            } else {
                childWithText(tag("move_to_humidor"), to).assertExists()
            }
            //Set move count
            onNodeWithTag(tag("move_count")).replaceText(moveCount.toString())
            assertNodeText(tag("move_count"), moveCount.toString())

            //Move cigars
            childButtonWithText(tag(DIALOG_MOVE_CIGARS), Localize.button_save).assertExists().assertIsEnabled().performClick()
            sleep(1000)
            onNodeWithTag(tag(HUMIDORS_BLOCK)).performScrollTo()
            //Check dialog disappears
            onNodeWithTag(tag(DIALOG_MOVE_CIGARS)).assertDoesNotExist()
            //Check cigars moved
            sleep(500)
            assertListOrder(tag(HUMIDORS_BLOCK_LIST), result)
            val list = onNodeWithTag(tag(HUMIDORS_BLOCK_LIST), true).assertExists().onChildren()
            for (i in 1..<result.size) {
                val node = list.getListRow(i)
                node.printToLog("Test", 10)
                node.assertHasChildWithText(resultCount[i - 1])
            }
        }
    }

    @Test
    fun addCigarsToHumidorTest() {
        with(composeTestRule) {
            //Check cigar humidors
            assertListOrder(tag(HUMIDORS_BLOCK_LIST), listOf("Total number of cigars: 10", "Case"))

            changeCigarsCount("Case Elegance Renzo Humidor", 1, 10, 5)
            changeCigarsCount("Case Elegance Renzo Humidor", 1, 5, 10)
        }

    }

    private fun changeCigarsCount(humidor: String, index: Int, from: Int, to: Int) {
        with(composeTestRule) {
            onNodeWithTag(tag(HUMIDORS_BLOCK)).performScrollTo()
            onNodeWithTag(tag(HUMIDORS_BLOCK_LIST), true).assertExists().onChildren().getListRow(index).let {
                it.assertHasChildWithText(humidor)
                it.assertHasChildWithText(from.toString())
                it.onChildren().filterToOne(hasAnyDescendant(hasText(from.toString()))).performClick()
                sleep(500)
                onNodeWithTag(tag("humidor_cigar_count_dialog")).assertExists()
                onNodeWithTag(tag("cigar_count")).assertExists().assertTextContains(from.toString())

                if (from > to) {
                    //Minus
                    for (i in to..<from) {
                        onNodeWithTag(tag("cigar_count_minus")).assertExists().performClick()
                    }
                    onNodeWithTag(tag("cigar_count")).assertExists().assertTextContains(to.toString())
                    onNodeWithTag(tag("cigar_count_price")).assertDoesNotExist()
                } else {
                    //Plus
                    for (i in from..<to) {
                        onNodeWithTag(tag("cigar_count_plus")).assertExists().performClick()
                    }
                    onNodeWithTag(tag("cigar_count")).assertExists().assertTextContains(to.toString())
                    onNodeWithTag(tag("cigar_count_price")).assertExists().assertTextContains("0.00")
                    onNodeWithTag(tag("cigar_count_price")).replaceText("100")
                    onNodeWithTag(tag("cigar_count_price")).assertExists().assertTextContains("1.00")
                }

                childButtonWithText(tag("humidor_cigar_count_dialog"), Localize.button_save).assertExists().assertIsEnabled().performClick()
                sleep(500)
                onNodeWithTag(tag("humidor_cigar_count_dialog")).assertDoesNotExist()
            }

            onNodeWithTag(tag(HUMIDORS_BLOCK_LIST), true).assertExists().onChildren().getListRow(index).let {
                it.assertHasChildWithText(humidor)
                it.assertHasChildWithText(to.toString())
            }
            assertListOrder(tag(HUMIDORS_BLOCK_LIST), listOf("Total number of cigars: $to", humidor))
        }
    }

    @Test
    fun editCigarDetailsTest() {
        with(composeTestRule) {
            onNodeWithTag(tag(CigarDetailsScreen.TOP_BAR_EDIT)).assertExists().performClick()

            //Check images carousel
            onNodeWithTag("ImagesCarousel").assertDoesNotExist()

            //Check cigar details
            childWithText(
                tag(CigarDetailsScreen.ORIGIN_BLOCK),
                "#1 Fuente Fuente OpusX Reserva d’Chateau",
                useUnmergedTree = true
            ).assertExists()
            childWithText(
                tag(CigarDetailsScreen.ORIGIN_BLOCK),
                "Fabrica de Tabacos Raices Cubanas S. de R.L.",
                useUnmergedTree = true
            ).assertExists()
            childWithText(tag(CigarDetailsScreen.ORIGIN_BLOCK), "Dominican", useUnmergedTree = true).assertExists()

            //Check cigar size
            onNodeWithTag(tag(SIZE_BLOCK)).performScrollTo()
            childWithText(tag(SIZE_BLOCK), "Churchill", useUnmergedTree = true).assertExists()
            assertPickerValues(
                tag(SIZE_BLOCK),
                Localize.cigar_details_shape,
                "Churchill",
                CigarShapes.enumValues().map { it.second },
                ::sleep,
                true
            )

            childWithText(tag(SIZE_BLOCK), "48", useUnmergedTree = true).assertExists()
            childWithText(tag(SIZE_BLOCK), "7'", true, useUnmergedTree = true).assertExists()

            //Check cigar tobacco
            onNodeWithTag(tag(RATINGS_BLOCK)).performScrollTo()
            childWithTextLabel(
                tag(SIZE_BLOCK),
                "Wrapper", "Dominican",
                substring = true
            ).assertExists()
            childWithTextLabel(
                tag(SIZE_BLOCK),
                "Binder", "Dominican",
                substring = true
            ).assertExists()
            childWithTextLabel(
                tag(SIZE_BLOCK),
                "Filler", "Dominican",
                substring = true
            ).assertExists()
            childWithTextLabel(
                tag(SIZE_BLOCK),
                Localize.cigar_details_strength, "Medium-Full",
                substring = true
            ).assertExists()
            assertPickerValues(
                tag(SIZE_BLOCK),
                Localize.cigar_details_strength,
                CigarStrength.localized(CigarStrength.MediumToFull),
                CigarStrength.enumValues().map { it.second },
                ::sleep,
                true,
                useUnmergedTree = true
            )
            //Check cigar ratings
            onNodeWithTag(tag("cigar_notes")).performScrollTo()
            childWithTextLabel(
                tag(RATINGS_BLOCK),
                "Rating", "97",
                substring = true
            ).assertExists()
            childWithTextLabel(
                tag(RATINGS_BLOCK),
                Localize.cigar_details_myrating, "0",
                substring = true
            ).assertExists()


            //Check cigar Humidors
            onNodeWithTag(tag(HUMIDORS_BLOCK)).assertDoesNotExist()

            //Check cigar notes
            onNodeWithTag(tag("cigar_notes")).assertExists().onChildren().assertCountEquals(2)

            //Change wrapper
            childWithTextLabel(
                tag(SIZE_BLOCK),
                "Wrapper", "Dominican",
                substring = true
            ).assertExists().replaceText("Nicaragua")

            pressButton("Save")

            sleep(1000)

            childWithTextLabel(
                tag(SIZE_BLOCK),
                "Wrapper", "Nicaragua",
                substring = true
            ).assertExists()

        }
    }

    @Test
    fun cigarHistoryTest() {
        with(composeTestRule) {
            onNodeWithTag(tag(CigarDetailsScreen.TOP_BAR_HISTORY)).assertExists().performClick()
            sleep(500)
            var nodes = onNodeWithTag("${CigarHistoryRoute.route}-List").assertExists().onChildren()
            nodes.assertCountEquals(1)
            nodes.getListRow(0).assertHasChildWithText("Added 10 cigars")

            pressBackButton()
            sleep(1500)
            changeCigarsCount("Case Elegance Renzo Humidor", 1, 10, 11)
            onNodeWithTag(tag(CigarDetailsScreen.TOP_BAR_HISTORY)).assertExists().performClick()
            sleep(500)
            nodes = onNodeWithTag("${CigarHistoryRoute.route}-List").assertExists().onChildren()
            nodes.assertCountEquals(2)
            nodes.getListRow(0).assertHasChildWithText("Added 1 cigar")

            pressBackButton()
            sleep(1500)
            changeCigarsCount("Case Elegance Renzo Humidor", 1, 11, 10)
            onNodeWithTag(tag(CigarDetailsScreen.TOP_BAR_HISTORY)).assertExists().performClick()
            sleep(500)
            nodes = onNodeWithTag("${CigarHistoryRoute.route}-List").assertExists().onChildren()
            nodes.assertCountEquals(3)
            nodes.getListRow(0).assertHasChildWithText("Removed 1 cigar")

            pressBackButton()
            sleep(1500)
            moveCigars(
                "Case Elegance Renzo Humidor",
                10,
                "Second",
                5,
                listOf("Case Elegance Renzo Humidor"),
                listOf("Second"),
                listOf("Total number of cigars: 10", "Case Elegance Renzo Humidor", "Second"),
                listOf("5", "5"),
            )
            onNodeWithTag(tag(CigarDetailsScreen.TOP_BAR_HISTORY)).assertExists().performClick()
            sleep(500)
            nodes = onNodeWithTag("${CigarHistoryRoute.route}-List").assertExists().onChildren()
            nodes.assertCountEquals(4)
            nodes.getListRow(0).assertHasChildWithText("Moved 5 cigars")
        }
    }

    @Test
    fun cigarImagesTest() {
        with(composeTestRule) {
            onNodeWithTag("ImagesCarousel").assertExists().performClick()
            sleep(500)
            onNodeWithTag(CigarImagesViewRoute.route).assertExists()
        }
    }

}