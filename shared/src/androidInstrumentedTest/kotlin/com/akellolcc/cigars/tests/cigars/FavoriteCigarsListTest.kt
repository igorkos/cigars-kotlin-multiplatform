/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/6/24, 4:58 PM
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
import com.akellolcc.cigars.screens.navigation.CigarsDetailsRoute
import com.akellolcc.cigars.screens.navigation.CigarsRoute
import com.akellolcc.cigars.screens.navigation.FavoritesRoute
import com.akellolcc.cigars.screens.navigation.NavRoute
import com.akellolcc.cigars.theme.Localize
import com.akellolcc.cigars.utils.screenListContentDescription
import pressBackButton
import pressButton
import selectTab
import waitForScreen
import kotlin.test.Test

class FavoriteCigarsListTest : CigarsListTests() {
    override var route: NavRoute = FavoritesRoute
    override fun setUp() {
        with(composeTestRule) {
            waitForScreen(CigarsRoute)
            assertListOrder(screenListContentDescription(CigarsRoute), listOf("#1", "#2", "#3", "#4", "#5"))
            setFavorite("#1")
            setFavorite("#2")
            setFavorite("#3")
            setFavorite("#4")
            setFavorite("#5")

            selectTab(FavoritesRoute)
            waitForScreen(FavoritesRoute)
        }
    }

    @Test
    fun testFavoriteCigarsList() {
        with(composeTestRule) {
            selectTab(FavoritesRoute)
            waitForScreen(FavoritesRoute)
            assertListOrder(screenListContentDescription(FavoritesRoute), listOf("#1", "#2", "#3", "#4", "#5"))
        }
    }

    private fun setFavorite(cigar: String) {

        with(composeTestRule) {
            assertListNode(screenListContentDescription(CigarsRoute), cigar).performClick()
            waitForScreen(CigarsDetailsRoute)

            onNodeWithContentDescription(Localize.cigar_details_ratings_block_desc).performScrollTo()
            pressButton(Localize.cigar_details_ratings_block_favorite_add_desc)

            pressBackButton()
            waitForScreen(CigarsRoute)
        }

    }
}