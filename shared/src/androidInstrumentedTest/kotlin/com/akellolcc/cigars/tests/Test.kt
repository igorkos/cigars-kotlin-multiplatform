/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/1/24, 2:39 PM
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

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import com.akellolcc.cigars.CigarsApplication
import com.akellolcc.cigars.screens.navigation.CigarsRoute
import com.akellolcc.cigars.screens.navigation.FavoritesRoute
import com.akellolcc.cigars.screens.navigation.HumidorsRoute
import com.akellolcc.cigars.screens.navigation.SearchCigarRoute
import com.akellolcc.cigars.theme.Localize
import com.akellolcc.cigars.utils.BaseUiTest
import com.akellolcc.cigars.utils.assertListOrder
import com.akellolcc.cigars.utils.pressButton
import com.akellolcc.cigars.utils.setAppContext
import com.akellolcc.cigars.utils.textIsDisplayed
import com.akellolcc.cigars.utils.waitForTag
import org.junit.Rule
import kotlin.test.Test


class CigarsAppTest() : BaseUiTest() {

    @get:Rule(order = 0)
    val composeTestRule = createAndroidComposeRule(ComponentActivity::class.java)

    @Test
    @OptIn(androidx.compose.ui.test.ExperimentalTestApi::class)
    fun tabNavigation() {
        with(composeTestRule) {
            setContent {
                setAppContext(LocalContext.current)
                CigarsApplication()
            }

            waitForTag(tag(route = CigarsRoute))
            onNodeWithTag(tag("List", CigarsRoute)).onChildren()
                .assertListOrder(5, listOf("#1", "#2", "#3", "#4", "#5"))

            pressButton(HumidorsRoute.title)
            waitForTag(tag(route = HumidorsRoute))
            onNodeWithTag(tag("List", HumidorsRoute)).onChildren()
                .assertListOrder(2, listOf("Case Elegance Renzo Humidor", "Second"))

            pressButton(FavoritesRoute.title)
            waitForTag(tag(route = FavoritesRoute))
            onNodeWithTag(tag("List", FavoritesRoute)).assertDoesNotExist()
            textIsDisplayed(Localize.list_is_empty)

            pressButton(Localize.title_search)
            waitForTag(tag(route = SearchCigarRoute))
        }
    }
}