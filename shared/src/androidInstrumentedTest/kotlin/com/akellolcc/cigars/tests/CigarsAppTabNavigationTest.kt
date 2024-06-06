/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/6/24, 2:30 PM
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
import assertListOrder
import com.akellolcc.cigars.screens.navigation.CigarsRoute
import com.akellolcc.cigars.screens.navigation.FavoritesRoute
import com.akellolcc.cigars.screens.navigation.HumidorsRoute
import com.akellolcc.cigars.screens.navigation.SearchCigarRoute
import com.akellolcc.cigars.theme.Localize
import com.akellolcc.cigars.utils.BaseUiTest
import com.akellolcc.cigars.utils.screenListContentDescription
import selectTab
import textIsDisplayed
import waitForScreen
import kotlin.test.Test


class CigarsAppTabNavigationTest() : BaseUiTest() {

    @Test
    fun tabNavigation() {
        with(composeTestRule) {
            waitForScreen(CigarsRoute)
            assertListOrder(screenListContentDescription(CigarsRoute), listOf("#1", "#2", "#3", "#4", "#5"))

            selectTab(HumidorsRoute)
            waitForScreen(HumidorsRoute)
            assertListOrder(screenListContentDescription(HumidorsRoute), listOf("Case Elegance Renzo Humidor", "Second"))

            selectTab(FavoritesRoute)
            waitForScreen(FavoritesRoute)
            onNodeWithTag(screenListContentDescription(FavoritesRoute)).assertDoesNotExist()
            textIsDisplayed(Localize.list_is_empty)

            selectTab(SearchCigarRoute)
            waitForScreen(SearchCigarRoute)
        }
    }
}