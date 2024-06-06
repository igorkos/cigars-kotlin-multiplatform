/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/5/24, 8:16 PM
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

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionCollection
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.performTextReplacement
import androidx.test.platform.app.InstrumentationRegistry
import com.akellolcc.cigars.screens.navigation.NavRoute
import com.akellolcc.cigars.theme.Localize
import io.kotest.matchers.ints.shouldBeGreaterThan

object UiTestUtils {
    // discussed later
    val context = InstrumentationRegistry.getInstrumentation().context
    val testContext = InstrumentationRegistry.getInstrumentation().context
}

fun screenListContentDescription(route: NavRoute): String {
    return "${route.title} ${Localize.screen_list_descr}"
}

fun SemanticsNodeInteraction.replaceText(text: String) {
    performTextReplacement(text)
}


fun SemanticsNodeInteractionCollection.getOrNull(index: Int): SemanticsNodeInteraction? {
    return try {
        val node = get(index)
        node.assertExists()
        node
    } catch (e: AssertionError) {
        null
    }
}


fun SemanticsNodeInteractionCollection.getListRow(index: Int): SemanticsNodeInteraction {
    val nodes = fetchSemanticsNodes()
    val active = nodes.filter { !it.layoutInfo.isDeactivated }
    active.size shouldBeGreaterThan index
    return get(index).assertExists()
}

fun SemanticsNodeInteraction.assertHasChildWithText(text: String, substring: Boolean = false) {
    assert(hasAnyDescendant(hasText(text, substring = substring)))
}

