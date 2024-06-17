/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/15/24, 7:09 PM
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

package com.akellolcc.cigars.utils

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import assertListOrder
import assertNodeState
import assertPickerValues
import assertValuePicker
import com.akellolcc.cigars.databases.models.Cigar
import com.akellolcc.cigars.databases.models.CigarShapes
import com.akellolcc.cigars.databases.models.CigarStrength
import com.akellolcc.cigars.databases.models.Humidor
import com.akellolcc.cigars.theme.Localize
import performSelectValuePicker
import pressButton
import replaceText
import waitForDialog
import waitForScreen

open class CigarDetailsUtils : BaseUiTest() {
    protected open fun displayCigarDetails(cigar: Cigar, images: Int) {
        with(composeTestRule) {

            //Check images carousel
            onNodeWithContentDescription(Localize.cigar_details_images_block_desc)
            assertNodeState(Localize.cigar_details_images_block_desc, Localize.images_pager_list_desc, "0:$images")

            //Check cigar details
            childWithTextLabel(
                Localize.cigar_details_origin_block_desc,
                Localize.cigar_details_name,
                cigar.name
            ).assertExists()
            childWithTextLabel(
                Localize.cigar_details_origin_block_desc,
                Localize.cigar_details_company,
                cigar.brand ?: ""
            ).assertExists()
            childWithTextLabel(
                Localize.cigar_details_origin_block_desc,
                Localize.cigar_details_country,
                cigar.country ?: ""
            ).assertExists()


            //Check cigar size
            assertValuesCard(Localize.cigar_details_size_block_desc, Localize.cigar_details_cigars)
            performValuesCardAction(Localize.cigar_details_size_block_desc)

            waitForDialog(Localize.cigar_details_size_info_dialog_desc).performClick()
            waitForScreen(route)
            assertValuesCardValues(
                Localize.cigar_details_size_block_desc,
                mapOf(
                    Localize.cigar_details_shape to cigar.cigar,
                    Localize.cigar_details_length to cigar.length,
                    Localize.cigar_details_gauge to cigar.gauge.toString()
                )
            )

            //Check cigar tobacco
            assertValuesCard(Localize.cigar_details_tobacco_block_desc, Localize.cigar_details_tobacco, true)
            performValuesCardAction(Localize.cigar_details_tobacco_block_desc)

            waitForDialog(Localize.cigar_details_tobacco_info_dialog_desc).performClick()
            waitForScreen(route)


            childWithTextLabel(
                Localize.cigar_details_tobacco_block_desc,
                Localize.cigar_details_wrapper, cigar.wrapper,
            ).assertExists()
            childWithTextLabel(
                Localize.cigar_details_tobacco_block_desc,
                Localize.cigar_details_binder, cigar.binder,
            ).assertExists()
            childWithTextLabel(
                Localize.cigar_details_tobacco_block_desc,
                Localize.cigar_details_filler, cigar.filler,
            ).assertExists()
            childWithTextLabel(
                Localize.cigar_details_tobacco_block_desc,
                Localize.cigar_details_strength, CigarStrength.localized(cigar.strength),
            ).assertExists()

            //Check cigar ratings
            assertValuesCard(Localize.cigar_details_ratings_block_desc, Localize.cigar_details_ratings)
            performValuesCardAction(Localize.cigar_details_ratings_block_desc)

            waitForDialog(Localize.cigar_details_ratings_info_dialog_desc).performClick()
            waitForScreen(route)
            assertValuesCardValues(
                Localize.cigar_details_ratings_block_desc,
                mapOf(
                    Localize.cigar_details_rating to cigar.rating.toString(),
                    Localize.cigar_details_myrating to cigar.myrating.toString()
                )
            )

            //Check cigar Humidors
            assertValuesCard(Localize.cigar_details_humidors_block_desc, Localize.cigar_details_humidors, true)
            assertListOrder(Localize.cigar_details_humidors_block_desc, listOf("Total number of cigars: 10", "Case"))

            //Check cigar notes
            onNodeWithContentDescription(Localize.cigar_details_notes_block_desc).assertExists().onChildren().assertCountEquals(2)
        }
    }

    protected fun setRating(current: Int, rating: Int) {
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

    protected fun moveCigars(
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
                moveCount.toString()
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

    protected fun changeCigarsCount(humidor: String, index: Int, from: Int, to: Int) {
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
                replaceText(Localize.cigar_details_count_dialog, Localize.cigar_details_count_dialog_price, "100")
                childWithTextLabel(Localize.cigar_details_count_dialog, Localize.cigar_details_count_dialog_price, "1.00")
            }

            pressButton(Localize.button_save)
            waitForScreen(route)

            assertListOrder(Localize.cigar_details_humidors_block_desc, listOf("Total number of cigars: $to", humidor))
            assertListOrder(Localize.cigar_details_humidors_block_desc, listOf("$to"), substring = false)
        }
    }

    protected fun checkEditCigarDetails(cigar: Cigar) {
        with(composeTestRule) {
            //Check images carousel
            onNodeWithContentDescription(Localize.cigar_details_images_block_desc).assertDoesNotExist()

            //Check cigar details
            childWithTextLabel(
                Localize.cigar_details_origin_block_desc,
                Localize.cigar_details_name,
                cigar.name
            ).assertExists()
            childWithTextLabel(
                Localize.cigar_details_origin_block_desc,
                Localize.cigar_details_company,
                cigar.brand ?: ""
            ).assertExists()
            childWithTextLabel(
                Localize.cigar_details_origin_block_desc,
                Localize.cigar_details_country,
                cigar.country ?: ""
            ).assertExists()

            if (cigar.rowid < 0) {
                childWithTextLabel(
                    Localize.cigar_details_origin_block_desc,
                    Localize.cigar_search_details_add_count_dialog,
                    cigar.count.toString()
                ).assertExists()
                childWithTextLabel(
                    Localize.cigar_details_origin_block_desc,
                    Localize.cigar_details_count_dialog_price,
                    cigar.price.toString()
                ).assertExists()
            }

            //Check cigar size
            onNodeWithContentDescription(Localize.cigar_details_size_block_desc).performScrollTo()
            assertValuePicker(Localize.cigar_details_shape, cigar.cigar, false)
            assertPickerValues(
                Localize.cigar_details_shape,
                cigar.cigar,
                CigarShapes.enumValues().map { it.second },
            )
            assertValuePicker(Localize.cigar_details_shape, cigar.cigar, false)

            childWithTextLabel(
                Localize.cigar_details_size_block_desc,
                Localize.cigar_details_length,
                cigar.length,
                true
            ).assertExists()

            childWithTextLabel(
                Localize.cigar_details_size_block_desc,
                Localize.cigar_details_gauge,
                cigar.gauge.toString(),
                true
            ).assertExists()

            //Check cigar tobacco
            onNodeWithContentDescription(Localize.cigar_details_ratings_block_desc).performScrollTo()
            childWithTextLabel(
                Localize.cigar_details_tobacco_block_desc,
                Localize.cigar_details_wrapper, cigar.wrapper,
                substring = true
            ).assertExists()
            childWithTextLabel(
                Localize.cigar_details_tobacco_block_desc,
                Localize.cigar_details_binder, cigar.binder,
                substring = true
            ).assertExists()
            childWithTextLabel(
                Localize.cigar_details_tobacco_block_desc,
                Localize.cigar_details_filler, cigar.filler,
                substring = true
            ).assertExists()

            assertValuePicker(Localize.cigar_details_strength, CigarStrength.localized(cigar.strength), false)
            assertPickerValues(
                Localize.cigar_details_strength,
                CigarStrength.localized(cigar.strength),
                CigarStrength.enumValues().map { it.second },
            )
            assertValuePicker(Localize.cigar_details_strength, CigarStrength.localized(cigar.strength), false)


            //Check cigar ratings
            onNodeWithContentDescription(Localize.cigar_details_notes_block_desc).performScrollTo()
            childWithTextLabel(
                Localize.cigar_details_ratings_block_desc,
                Localize.cigar_details_rating,
                cigar.rating.toString(),
                substring = true
            ).assertExists()
            childWithTextLabel(
                Localize.cigar_details_ratings_block_desc,
                Localize.cigar_details_myrating,
                cigar.myrating.toString(),
                substring = true
            ).assertExists()


            //Check cigar Humidors
            onNodeWithContentDescription(Localize.cigar_details_humidors_block_desc).assertDoesNotExist()

            //Check cigar notes
            onNodeWithContentDescription(Localize.cigar_details_notes_block_desc).assertExists().onChildren().assertCountEquals(2)
        }
    }

    protected fun editCigarDetails(cigar: Cigar, updated: Cigar) {
        with(composeTestRule) {
            //Update cigar details
            replaceText(
                Localize.cigar_details_origin_block_desc,
                Localize.cigar_details_name,
                updated.name
            )
            replaceText(
                Localize.cigar_details_origin_block_desc,
                Localize.cigar_details_company,
                updated.brand!!
            )
            replaceText(
                Localize.cigar_details_origin_block_desc,
                Localize.cigar_details_country,
                updated.country!!
            )

            if (cigar.rowid < 0) {
                replaceText(
                    Localize.cigar_details_origin_block_desc,
                    Localize.cigar_search_details_add_count_dialog,
                    updated.count.toString()
                )
                val price = updated.price.toString().replace(".", "")
                replaceText(
                    Localize.cigar_details_origin_block_desc,
                    Localize.cigar_details_count_dialog_price,
                    price
                )
            }


            //Check cigar size
            onNodeWithContentDescription(Localize.cigar_details_size_block_desc).performScrollTo()
            performSelectValuePicker(Localize.cigar_details_shape, updated.cigar)
            val length = updated.length.replace("'", "")
            replaceText(
                Localize.cigar_details_size_block_desc,
                Localize.cigar_details_length,
                length
            )

            replaceText(
                Localize.cigar_details_size_block_desc,
                Localize.cigar_details_gauge,
                updated.gauge.toString()
            )

            //Check cigar tobacco
            onNodeWithContentDescription(Localize.cigar_details_ratings_block_desc).performScrollTo()
            replaceText(
                Localize.cigar_details_tobacco_block_desc,
                Localize.cigar_details_wrapper,
                updated.wrapper
            )
            replaceText(
                Localize.cigar_details_tobacco_block_desc,
                Localize.cigar_details_binder,
                updated.binder
            )
            replaceText(
                Localize.cigar_details_tobacco_block_desc,
                Localize.cigar_details_filler,
                updated.filler
            )
            performSelectValuePicker(Localize.cigar_details_strength, CigarStrength.localized(updated.strength))

            //Check cigar ratings
            onNodeWithContentDescription(Localize.cigar_details_ratings_block_desc).performScrollTo()
            replaceText(
                Localize.cigar_details_ratings_block_desc,
                Localize.cigar_details_rating,
                updated.rating.toString()
            )
            replaceText(
                Localize.cigar_details_ratings_block_desc,
                Localize.cigar_details_myrating,
                updated.myrating.toString()
            )

            onNodeWithContentDescription(Localize.cigar_details_notes_block_desc).performScrollTo()
            replaceText(
                Localize.cigar_details_notes_block_desc,
                Localize.cigar_details_notes,
                updated.notes!!
            )
            replaceText(
                Localize.cigar_details_notes_block_desc,
                Localize.cigar_details_link,
                updated.link!!
            )
        }
    }

    fun assertDisplayHumidorDetails(humidor: Humidor, images: Int) {
        with(composeTestRule) {
            //Check images carousel
            assertNodeState(Localize.cigar_details_images_block_desc, Localize.images_pager_list_desc, "0:$images")

            //Check humidor details
            childWithTextLabel(
                Localize.humidor_details_origin_block_desc,
                Localize.cigar_details_name,
                humidor.name
            ).assertExists()

            childWithTextLabel(
                Localize.humidor_details_origin_block_desc,
                Localize.cigar_details_company,
                humidor.brand
            ).assertExists()

            //Check humidor capacity
            assertValuesCard(Localize.humidor_details_size_block_desc, Localize.humidor_details_cigars)
            assertValuesCardValues(
                Localize.humidor_details_size_block_desc,
                mapOf(
                    Localize.humidor_details_holds to humidor.holds.toString(),
                    Localize.humidor_details_count to humidor.count.toString(),
                )
            )

            //Check humidor details
            assertValuesCard(Localize.humidor_details_params_block_desc, Localize.humidor_details_humidor)
            assertValuesCardValues(
                Localize.humidor_details_params_block_desc,
                mapOf(
                    Localize.humidor_details_temperature to humidor.temperature.toString(),
                    Localize.humidor_details_humidity to humidor.humidity.toString(),
                )
            )

            //Check cigar notes
            onNodeWithContentDescription(Localize.humidor_details_notes_block_desc).assertExists().onChildren().assertCountEquals(2)
        }
    }

    fun assertHumidorDetailsEditing(humidor: Humidor) {
        with(composeTestRule) {

            //Check images carousel
            onNodeWithContentDescription(Localize.cigar_details_images_block_desc).assertDoesNotExist()

            //Check humidor details
            childWithTextLabel(
                Localize.humidor_details_origin_block_desc,
                Localize.cigar_details_name,
                humidor.name
            ).assertExists()

            childWithTextLabel(
                Localize.humidor_details_origin_block_desc,
                Localize.cigar_details_company,
                humidor.brand
            ).assertExists()


            //Check humidor capacity
            assertValuesCard(Localize.humidor_details_size_block_desc, Localize.humidor_details_cigars, true)
            childWithTextLabel(
                Localize.humidor_details_size_block_desc,
                Localize.humidor_details_holds,
                humidor.holds.toString()
            ).assertExists()

            childWithTextLabel(
                Localize.humidor_details_size_block_desc,
                Localize.humidor_details_count,
                humidor.count.toString()
            ).assertExists()

            //Check humidor details
            assertValuesCard(Localize.humidor_details_params_block_desc, Localize.humidor_details_humidor, true)
            childWithTextLabel(
                Localize.humidor_details_params_block_desc,
                Localize.humidor_details_temperature,
                humidor.temperature.toString()
            ).assertExists()

            childWithTextLabel(
                Localize.humidor_details_params_block_desc,
                Localize.humidor_details_humidity,
                humidor.humidity.toString()
            ).assertExists()

            //Check humidor notes
            onNodeWithContentDescription(Localize.humidor_details_notes_block_desc).assertExists().onChildren().assertCountEquals(2)
        }
    }

    fun editHumidorDetails(humidor: Humidor, updated: Humidor) {
        with(composeTestRule) {
            //Check images carousel
            onNodeWithContentDescription(Localize.cigar_details_images_block_desc).assertDoesNotExist()

            //Check humidor details
            replaceText(
                Localize.humidor_details_origin_block_desc,
                Localize.cigar_details_name,
                updated.name
            )

            replaceText(
                Localize.humidor_details_origin_block_desc,
                Localize.cigar_details_company,
                updated.brand
            )


            //Check humidor capacity
            assertValuesCard(Localize.humidor_details_size_block_desc, Localize.humidor_details_cigars, true)
            replaceText(
                Localize.humidor_details_size_block_desc,
                Localize.humidor_details_holds,
                updated.holds.toString()
            )

            replaceText(
                Localize.humidor_details_size_block_desc,
                Localize.humidor_details_count,
                updated.count.toString()
            )

            //Check humidor details
            assertValuesCard(Localize.humidor_details_params_block_desc, Localize.humidor_details_humidor, true)
            replaceText(
                Localize.humidor_details_params_block_desc,
                Localize.humidor_details_temperature,
                updated.temperature.toString()
            )

            replaceText(
                Localize.humidor_details_params_block_desc,
                Localize.humidor_details_humidity,
                updated.humidity.toString()
            )

            //Check cigar notes
            replaceText(
                Localize.humidor_details_notes_block_desc,
                Localize.cigar_details_notes,
                updated.notes!!
            )

            replaceText(
                Localize.humidor_details_notes_block_desc,
                Localize.cigar_details_link,
                updated.link!!
            )

            assertHumidorDetailsEditing(updated)
        }
    }

}