/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/10/24, 5:08 PM
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


package com.akellolcc.cigars.tests

import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import assertListNode
import assertListOrder
import com.akellolcc.cigars.screens.navigation.CigarsDetailsRoute
import com.akellolcc.cigars.screens.navigation.CigarsRoute
import com.akellolcc.cigars.screens.navigation.FavoritesRoute
import com.akellolcc.cigars.screens.navigation.HumidorsRoute
import com.akellolcc.cigars.screens.navigation.SearchCigarRoute
import com.akellolcc.cigars.theme.Localize
import com.akellolcc.cigars.utils.BaseUiTest
import com.akellolcc.cigars.utils.childWithTextLabel
import com.akellolcc.cigars.utils.screenListContentDescription
import org.junit.FixMethodOrder
import org.junit.runners.MethodSorters
import pressBackButton
import selectTab
import waitForScreen
import kotlin.test.Test

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class CigarsAppTabNavigationTest() : BaseUiTest() {

    @Test
    fun test1_tabNavigation() {
        with(composeTestRule) {
            waitForScreen(CigarsRoute)
            assertListOrder(screenListContentDescription(CigarsRoute), listOf("#1", "#2", "#3", "#4", "#5"))

            selectTab(HumidorsRoute)
            waitForScreen(HumidorsRoute)
            assertListOrder(screenListContentDescription(HumidorsRoute), listOf("Case Elegance Renzo Humidor", "Second"))

            selectTab(FavoritesRoute)
            waitForScreen(FavoritesRoute)
            onNodeWithTag(screenListContentDescription(FavoritesRoute)).assertDoesNotExist()
            childWithTextLabel(screenListContentDescription(FavoritesRoute), Localize.list_is_empty, Localize.list_is_empty).assertExists()

            selectTab(SearchCigarRoute)
            waitForScreen(SearchCigarRoute)
        }
    }

    @Test
    fun test2_screenNavigation() {
        with(composeTestRule) {
            waitForScreen(CigarsRoute)
            assertListOrder(screenListContentDescription(CigarsRoute), listOf("#1", "#2", "#3", "#4", "#5"))

            assertListNode(screenListContentDescription(CigarsRoute), "#1").performClick()
            waitForScreen(CigarsDetailsRoute)
            pressBackButton()

            waitForScreen(CigarsRoute)
            selectTab(HumidorsRoute)
            waitForScreen(HumidorsRoute)

            selectTab(CigarsRoute)
            waitForScreen(CigarsRoute)

            assertListNode(screenListContentDescription(CigarsRoute), "#1").performClick()
            waitForScreen(CigarsDetailsRoute)
            pressBackButton()
        }
    }
}