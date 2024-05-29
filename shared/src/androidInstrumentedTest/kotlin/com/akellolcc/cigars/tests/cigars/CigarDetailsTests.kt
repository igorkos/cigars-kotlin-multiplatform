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
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.printToLog
import com.akellolcc.cigars.screens.navigation.CigarsDetailsRoute
import com.akellolcc.cigars.screens.navigation.CigarsRoute
import com.akellolcc.cigars.theme.Localize
import com.akellolcc.cigars.utils.BaseUiTest
import com.akellolcc.cigars.utils.assertHasChildWithText
import com.akellolcc.cigars.utils.assertListOrder
import com.akellolcc.cigars.utils.assertNodeText
import com.akellolcc.cigars.utils.childButtonWithText
import com.akellolcc.cigars.utils.childWithTag
import com.akellolcc.cigars.utils.childWithText
import com.akellolcc.cigars.utils.childWithTextLabel
import com.akellolcc.cigars.utils.dialogWithTag
import com.akellolcc.cigars.utils.getListRow
import com.akellolcc.cigars.utils.pressBackButton
import com.akellolcc.cigars.utils.pressButton
import com.akellolcc.cigars.utils.replaceText
import com.akellolcc.cigars.utils.sleep
import com.akellolcc.cigars.utils.textIsDisplayed
import com.akellolcc.cigars.utils.waitForText
import org.junit.Rule
import kotlin.test.Test

@OptIn(androidx.compose.ui.test.ExperimentalTestApi::class)
class CigarDetailsTests : BaseUiTest() {

    @get:Rule(order = 0)
    val composeTestRule = createAndroidComposeRule(ComponentActivity::class.java)

    private fun tag(tag: String): String {
        return "${CigarsDetailsRoute.route}-$tag"
    }


    @Test
    fun displayCigarDetailsTest() {
        with(composeTestRule) {
            setContent {
                CigarsAppContent()
            }
            //Wait for app to load
            waitForText("Cigars")
            sleep(500)
            //Check items displayed
            onNodeWithTag("${CigarsRoute.route}-List").onChildren().assertCountEquals(5)
            textIsDisplayed("#1", true)
            onNodeWithText("#1", true).performClick()
            sleep(500)
            onNodeWithTag(CigarsDetailsRoute.route).assertExists()

            //Check images carousel
            onNodeWithTag("ImagesCarousel").assertExists()
            onNodeWithTag("ImagesCarousel").onChildren().assertCountEquals(1)
            onNodeWithTag("ImagesCarouselItem--1").assertDoesNotExist()
            onNodeWithTag("ImagesCarouselItem-0").assertExists()

            //Check cigar details
            childWithText(tag("cigar_origin"), "#1 Fuente Fuente OpusX Reserva d’Chateau").assertExists()
            childWithText(tag("cigar_origin"), "Fabrica de Tabacos Raices Cubanas S. de R.L.").assertExists()
            childWithText(tag("cigar_origin"), "Dominican").assertExists()

            //Check cigar size
            childWithTag(tag("cigar_size"), "ValuesCard-Container").assertExists()
            childWithTag(tag("cigar_size"), "ValuesCard-Horizontal").assertExists()
            childWithText(tag("cigar_size"), "Cigar").assertExists()
            childWithTag(tag("cigar_size"), "ValuesCard-Action").assertExists().performClick()
            sleep(500)
            dialogWithTag("InfoImageDialog").assertExists().performClick()
            sleep(500)
            dialogWithTag("InfoImageDialog").assertDoesNotExist()
            childWithText(tag("cigar_size"), "Churchill", useUnmergedTree = true).assertExists()
            childWithText(tag("cigar_size"), "7'", useUnmergedTree = true).assertExists()
            childWithText(tag("cigar_size"), "48", useUnmergedTree = true).assertExists()

            //Check cigar size
            childWithTag(tag("cigar_tobacco"), "ValuesCard-Container").assertExists()
            childWithTag(tag("cigar_tobacco"), "ValuesCard-Vertical").assertExists()
            childWithText(tag("cigar_tobacco"), "Tobacco").assertExists()
            childWithTag(tag("cigar_tobacco"), "ValuesCard-Action").assertExists().performClick()
            sleep(500)
            dialogWithTag("InfoImageDialog").assertExists().performClick()
            sleep(500)
            dialogWithTag("InfoImageDialog").assertDoesNotExist()
            childWithTextLabel(
                tag("cigar_tobacco"),
                "Wrapper:", "Dominican",
                substring = true,
                useUnmergedTree = true
            ).assertExists()
            childWithTextLabel(
                tag("cigar_tobacco"),
                "Binder:", "Dominican",
                substring = true,
                useUnmergedTree = true
            ).assertExists()
            childWithTextLabel(
                tag("cigar_tobacco"),
                "Filler:", "Dominican",
                substring = true,
                useUnmergedTree = true
            ).assertExists()
            childWithTextLabel(
                tag("cigar_tobacco"),
                "Strength:", "Medium-Full",
                substring = true,
                useUnmergedTree = true
            ).assertExists()

            //Check cigar ratings
            childWithTag(tag("cigar_ratings"), "ValuesCard-Container").assertExists()
            childWithTag(tag("cigar_ratings"), "ValuesCard-Horizontal").assertExists()
            childWithText(tag("cigar_ratings"), "Ratings").assertExists()
            childWithTag(tag("cigar_ratings"), "ValuesCard-Action").assertExists().performClick()
            sleep(500)
            dialogWithTag("InfoImageDialog").assertExists().performClick()
            sleep(500)
            dialogWithTag("InfoImageDialog").assertDoesNotExist()
            childWithTextLabel(
                tag("cigar_ratings"),
                "Rating", "97",
                substring = true,
                useUnmergedTree = true
            ).assertExists()
            childWithTextLabel(
                tag("cigar_ratings"),
                Localize.cigar_details_myrating, "0",
                substring = true,
                useUnmergedTree = true
            ).assertExists()


            //Check cigar Humidors
            childWithTag(tag("cigar_humidors"), "ValuesCard-Container").assertExists()
            childWithTag(tag("cigar_humidors"), "ValuesCard-Vertical").assertExists()
            childWithText(tag("cigar_humidors"), "Humidors").assertExists()
            childWithTag(tag("cigar_humidors"), "ValuesCard-Action").assertExists()
            onNodeWithTag(tag("cigar_humidors_list")).assertExists().onChildren()
                .assertListOrder(2, listOf("Total number of cigars: 10", "Case"))

            //Check cigar notes
            onNodeWithTag(tag("cigar_notes")).assertExists().onChildren().assertCountEquals(3)

        }
    }

    @Test
    fun cigarRatingTest() {
        with(composeTestRule) {
            setContent {
                CigarsAppContent()
            }
            //Wait for app to load
            waitForText("Cigars")
            sleep(500)
            //Check items displayed
            onNodeWithTag("${CigarsRoute.route}-List").onChildren().assertCountEquals(5)
            textIsDisplayed("#1", true)
            onNodeWithText("#1", true).performClick()
            sleep(500)
            onNodeWithTag(CigarsDetailsRoute.route).assertExists()

            //Check cigar ratings
            setRating(0, 50)
            setRating(50, 0)
        }
    }

    private fun setRating(current: Int, rating: Int) {
        with(composeTestRule) {
            childWithTextLabel(
                tag("cigar_ratings"),
                Localize.cigar_details_myrating, current.toString(),
                substring = true,
                useUnmergedTree = true
            ).assertExists().performClick()

            waitForText(Localize.cigar_details_rating_dialog)
            dialogWithTag(tag("cigar_rating_dialog")).assertExists()
            childWithText(tag("cigar_rating_dialog"), Localize.cigar_details_rating_dialog).assertExists()
            childWithText(tag("cigar_rating_dialog"), current.toString()).assertExists()
            childButtonWithText(tag("cigar_rating_dialog"), Localize.button_cancel).assertExists()
            childButtonWithText(tag("cigar_rating_dialog"), Localize.button_save).assertExists()

            childWithText(tag("cigar_rating_dialog"), current.toString()).performClick()
            sleep(500)
            childWithText(tag("cigar_rating_dialog"), current.toString()).replaceText(rating.toString())
            childButtonWithText(tag("cigar_rating_dialog"), Localize.button_save).performClick()
            childWithTextLabel(
                tag("cigar_ratings"),
                Localize.cigar_details_myrating, rating.toString(),
                substring = true,
                useUnmergedTree = true
            ).assertExists()
        }
    }

    @Test
    fun cigarFavoriteTest() {
        with(composeTestRule) {
            setContent {
                CigarsAppContent()
            }
            //Wait for app to load
            waitForText("Cigars")
            sleep(500)
            //Check items displayed
            onNodeWithTag("${CigarsRoute.route}-List").onChildren().assertCountEquals(5)
            textIsDisplayed("#1", true)
            onNodeWithText("#1", true).performClick()
            sleep(500)
            onNodeWithTag(CigarsDetailsRoute.route).assertExists()

            //Check cigar favorite
            setFavorite(true)
            sleep(500)
            setFavorite(false)
        }
    }

    private fun setFavorite(favorite: Boolean) {
        with(composeTestRule) {
            childWithTag(
                tag("cigar_ratings"),
                tag("cigar_favorite_${!favorite}")
            ).assertExists().performClick()

            childWithTag(
                tag("cigar_ratings"),
                tag("cigar_favorite_${favorite}")
            ).assertExists()

            pressBackButton()

            pressButton("Favorites")
            waitForText("Favorites")

            if (favorite) {
                onNodeWithTag("FavoritesScreen-List").assertExists().onChildren()
                    .assertListOrder(1, listOf("#1"))
            } else {
                onNodeWithTag("FavoritesScreen-List").assertDoesNotExist()
                textIsDisplayed("Nothing to show")
            }

            pressButton("Cigars")
            waitForText("Cigars")

            onNodeWithText("#1", true).performClick()
        }
    }

    @Test
    fun cigarHumidorsTest() {
        with(composeTestRule) {
            setContent {
                CigarsAppContent()
            }
            //Wait for app to load
            waitForText("Cigars", 10000)
            sleep(500)
            //Check items displayed
            onNodeWithTag("${CigarsRoute.route}-List").onChildren().assertCountEquals(5)
            textIsDisplayed("#1", true)
            onNodeWithText("#1", true).performClick()
            sleep(500)
            onNodeWithTag(CigarsDetailsRoute.route).assertExists()

            onNodeWithTag(tag("cigar_humidors")).performScrollTo()
            //Check cigar humidors
            onNodeWithTag(tag("cigar_humidors_list")).assertExists().onChildren()
                .assertListOrder(2, listOf("Total number of cigars: 10", "Case"))

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
            childWithTag(tag("cigar_humidors"), "ValuesCard-Action").assertExists().performClick()
            sleep(500)
            //Check dialog content
            childWithTag(tag("move_cigars_dialog"), tag("move_from_humidor")).assertExists()
            childWithTag(tag("move_cigars_dialog"), tag("move_to_humidor")).assertExists()
            childWithTag(tag("move_cigars_dialog"), tag("move_count")).assertExists()
            childButtonWithText(tag("move_cigars_dialog"), Localize.button_cancel).assertExists().assertIsEnabled()

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
                onNodeWithTag("value_picker_list").assertExists().onChildren().assertListOrder(fromHumidors.size, fromHumidors)
                childWithText("value_picker_list", from, true).assertExists().performClick()
                childWithText(tag("move_from_humidor"), from).assertExists()
                onNodeWithTag("value_picker_list").assertDoesNotExist()
            }
            sleep(500)
            if (toHumidors.size - fromHumidors.size > 1) {
                //Open dropdown from
                childWithTag(tag("move_to_humidor"), "value_picker_drop_down").assertExists().performClick()
                //Select from
                onNodeWithTag("value_picker_list").assertExists().onChildren().assertListOrder(toHumidors.size, toHumidors)
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
            childButtonWithText(tag("move_cigars_dialog"), Localize.button_save).assertExists().assertIsEnabled().performClick()
            sleep(1000)
            onNodeWithTag(tag("cigar_humidors")).performScrollTo()
            //Check dialog disappears
            onNodeWithTag(tag("move_cigars_dialog")).assertDoesNotExist()
            //Check cigars moved
            sleep(500)
            onNodeWithTag(tag("cigar_humidors_list")).assertExists().onChildren().assertListOrder(result.size, result)
            val list = onNodeWithTag(tag("cigar_humidors_list"), true).assertExists().onChildren()
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
            setContent {
                CigarsAppContent()
            }
            //Wait for app to load
            waitForText("Cigars", 10000)
            sleep(500)
            //Check items displayed
            onNodeWithTag("${CigarsRoute.route}-List").onChildren().assertCountEquals(5)
            textIsDisplayed("#1", true)
            onNodeWithText("#1", true).performClick()
            sleep(500)
            onNodeWithTag(CigarsDetailsRoute.route).assertExists()
            onNodeWithTag(tag("cigar_humidors")).performScrollTo()

            //Check cigar humidors
            onNodeWithTag(tag("cigar_humidors_list")).assertExists().onChildren()
                .assertListOrder(2, listOf("Total number of cigars: 10", "Case"))

            //Move 5 cigars from Case to Second
            onNodeWithTag(tag("cigar_humidors_list"), true).assertExists().onChildren().getListRow(1).let {
                it.assertHasChildWithText("Case Elegance Renzo Humidor")
                it.assertHasChildWithText("10")
                it.onChildren().filterToOne(hasAnyDescendant(hasText("10"))).performClick()
                sleep(500)
                onNodeWithTag(tag("humidor_cigar_count_dialog")).assertExists()
                onNodeWithTag(tag("cigar_count")).assertExists().assertTextContains(10.toString())

                onNodeWithTag(tag("cigar_count_minus")).assertExists().performClick()
                onNodeWithTag(tag("cigar_count")).assertExists().assertTextContains(9.toString())
                onNodeWithTag(tag("cigar_count_price")).assertDoesNotExist()

                onNodeWithTag(tag("cigar_count_plus")).assertExists().performClick()
                onNodeWithTag(tag("cigar_count_plus")).assertExists().performClick()
                onNodeWithTag(tag("cigar_count")).assertExists().assertTextContains(11.toString())
                onNodeWithTag(tag("cigar_count_price")).assertExists().assertTextContains("0.00")

                onNodeWithTag(tag("cigar_count_price")).replaceText("100")
                onNodeWithTag(tag("cigar_count_price")).assertExists().assertTextContains("1.00")

                childButtonWithText(tag("humidor_cigar_count_dialog"), Localize.button_save).assertExists().assertIsEnabled().performClick()
                sleep(500)
                onNodeWithTag(tag("humidor_cigar_count_dialog")).assertDoesNotExist()
            }

            onNodeWithTag(tag("cigar_humidors_list"), true).assertExists().onChildren().getListRow(1).let {
                it.assertHasChildWithText("Case Elegance Renzo Humidor")
                it.assertHasChildWithText("11")
            }
            onNodeWithTag(tag("cigar_humidors_list")).assertExists().onChildren()
                .assertListOrder(2, listOf("Total number of cigars: 11", "Case"))
        }

    }
}