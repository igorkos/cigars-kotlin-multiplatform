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

import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import assertListOrder
import com.akellolcc.cigars.screens.navigation.CigarsDetailsRoute
import com.akellolcc.cigars.screens.navigation.CigarsRoute
import com.akellolcc.cigars.screens.navigation.FavoritesRoute
import com.akellolcc.cigars.utils.childWithTag
import pressBackButton
import pressButton
import waitForScreen
import waitForTag

class FavoriteCigarsListTest : CigarsListTests() {

    override fun setUp() {
        with(composeTestRule) {
            waitForScreen(CigarsRoute)
            assertListOrder(tag("List", CigarsRoute), listOf("#1", "#2", "#3", "#4", "#5"))
            setFavorite("#1")
            setFavorite("#2")
            setFavorite("#3")
            setFavorite("#4")
            setFavorite("#5")

            pressButton(FavoritesRoute.title)
            route = FavoritesRoute
            waitForTag(tag())
        }
    }

    private fun setFavorite(cigar: String) {

        with(composeTestRule) {
            onNodeWithText(cigar, true).performClick()
            route = CigarsDetailsRoute
            waitForTag(tag())
            onNodeWithTag(tag("cigar_ratings")).performScrollTo()
            childWithTag(
                tag("cigar_ratings"),
                tag("cigar_favorite_false")
            ).assertExists().performClick()

            childWithTag(
                tag("cigar_ratings"),
                tag("cigar_favorite_true")
            ).assertExists()

            pressBackButton()
            route = CigarsRoute
            waitForTag(tag())
        }

    }
}