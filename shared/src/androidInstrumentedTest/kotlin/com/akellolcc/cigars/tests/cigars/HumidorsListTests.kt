/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/17/24, 2:53 PM
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
import assertListNode
import assertListOrder
import com.akellolcc.cigars.databases.loadDemoSet
import com.akellolcc.cigars.databases.models.Humidor
import com.akellolcc.cigars.databases.models.emptyHumidor
import com.akellolcc.cigars.screens.navigation.CigarsRoute
import com.akellolcc.cigars.screens.navigation.HumidorCigarsRoute
import com.akellolcc.cigars.screens.navigation.HumidorDetailsRoute
import com.akellolcc.cigars.screens.navigation.HumidorsRoute
import com.akellolcc.cigars.theme.AssetFiles
import com.akellolcc.cigars.theme.Localize
import com.akellolcc.cigars.utils.CigarDetailsUtils
import com.akellolcc.cigars.utils.screenListContentDescription
import org.junit.FixMethodOrder
import org.junit.runners.MethodSorters
import pressBackButton
import pressButton
import selectMenuItem
import selectTab
import sleep
import waitForScreen
import kotlin.test.Test

@OptIn(androidx.compose.ui.test.ExperimentalTestApi::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
open class HumidorsListTests : CigarDetailsUtils() {

    override fun setUp() {
        val humidors: List<Humidor> = loadDemoSet(AssetFiles.demo_humidors)
        with(composeTestRule) {
            waitForScreen(CigarsRoute)
            //Check items displayed
            selectTab(HumidorsRoute)
            waitForScreen(HumidorsRoute)

            assertListOrder(screenListContentDescription(HumidorsRoute), humidors.map { it.name })
            assertListOrder(
                screenListContentDescription(HumidorsRoute),
                humidors.map { Localize.humidor_cigars(it.holds, 0) }
            )
        }
    }

    @Test
    fun test1_humidorsListAddTest() {
        val newHumidor: Humidor = loadDemoSet<Humidor>(AssetFiles.test_humidors).first()
        val humidor: Humidor = humidors().first()

        with(composeTestRule) {
            pressButton(Localize.humidor_list_add_action_desc)
            waitForScreen(HumidorDetailsRoute)

            pressButton(Localize.button_cancel)
            waitForScreen(HumidorsRoute)

            assertListNode(screenListContentDescription(HumidorsRoute), humidor.name).performClick()
            waitForScreen(HumidorCigarsRoute)
            sleep(1000)
            assertListOrder(screenListContentDescription(HumidorCigarsRoute), listOf("#1", "#2", "#3", "#4", "#5"))
            pressBackButton()
            waitForScreen(HumidorsRoute)

            pressButton(Localize.humidor_list_add_action_desc)
            waitForScreen(HumidorDetailsRoute)

            editHumidorDetails(emptyHumidor, newHumidor)
            pressButton(Localize.button_save)
            waitForScreen(HumidorDetailsRoute)

            pressBackButton()
            waitForScreen(HumidorsRoute)

            assertListNode(screenListContentDescription(HumidorsRoute), newHumidor.name).performClick()
            waitForScreen(HumidorCigarsRoute)

            onNodeWithContentDescription(Localize.screen_list_sort_fields_action_descr).assertExists().performClick()
            selectMenuItem(Localize.screen_list_sort_fields_list_descr, Localize.title_humidor_details_menu_desc)
            waitForScreen(HumidorDetailsRoute)

            assertDisplayHumidorDetails(newHumidor, 0)

        }
    }
}