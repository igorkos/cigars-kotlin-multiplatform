/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/11/24, 12:19 PM
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

import androidx.compose.ui.test.performClick
import assertListNode
import assertListOrder
import com.akellolcc.cigars.screens.navigation.CigarsRoute
import com.akellolcc.cigars.screens.navigation.HumidorCigarsRoute
import com.akellolcc.cigars.screens.navigation.HumidorDetailsRoute
import com.akellolcc.cigars.screens.navigation.HumidorsRoute
import com.akellolcc.cigars.screens.navigation.NavRoute
import com.akellolcc.cigars.theme.Localize
import com.akellolcc.cigars.utils.BaseUiTest
import com.akellolcc.cigars.utils.screenListContentDescription
import org.junit.FixMethodOrder
import org.junit.runners.MethodSorters
import pressBackButton
import pressButton
import selectTab
import waitForScreen
import kotlin.test.Test

@OptIn(androidx.compose.ui.test.ExperimentalTestApi::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
open class HumidorsListTests : BaseUiTest() {
    override var route: NavRoute = CigarsRoute
    private val LIST_TAG: String
        get() {
            return "${route.title} ${Localize.screen_list_descr}"
        }

    override fun setUp() {
        with(composeTestRule) {
            waitForScreen(CigarsRoute)
            //Check items displayed
            selectTab(HumidorsRoute)
            waitForScreen(HumidorsRoute)
        }
    }

    @Test
    fun test1_humidorsListTest() {
        with(composeTestRule) {
            assertListOrder(screenListContentDescription(HumidorsRoute), listOf("Case Elegance Renzo Humidor", "Second"))
            assertListOrder(
                screenListContentDescription(HumidorsRoute),
                listOf(Localize.humidor_cigars(50, 0), Localize.humidor_cigars(0, 150))
            )
            pressButton(Localize.humidor_list_add_action_desc)
            waitForScreen(HumidorDetailsRoute)

            pressButton(Localize.button_cancel)
            waitForScreen(HumidorsRoute)

            assertListNode(screenListContentDescription(HumidorsRoute), "Case").performClick()
            waitForScreen(HumidorCigarsRoute)
            pressBackButton()
            waitForScreen(HumidorsRoute)

            assertListNode(screenListContentDescription(HumidorsRoute), "Second").performClick()
            waitForScreen(HumidorCigarsRoute)
            pressBackButton()
            waitForScreen(HumidorsRoute)
        }
    }
}