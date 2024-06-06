/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/6/24, 2:27 PM
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

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasContentDescriptionExactly
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToLog
import com.akellolcc.cigars.screens.navigation.NavRoute
import com.akellolcc.cigars.theme.Localize
import com.akellolcc.cigars.utils.childWithTextLabel
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.ints.shouldBeLessThan
import junit.framework.TestCase
import org.junit.Assert

/**
 * Waits for a screen to be displayed.
 * @param route The navigation route of screen to wait for.
 * @param timeoutMillis The timeout in milliseconds.
 */
@OptIn(ExperimentalTestApi::class)
fun ComposeTestRule.waitForScreen(
    route: NavRoute,
    timeoutMillis: Long = 10000
): SemanticsNodeInteraction {
    waitUntilAtLeastOneExists(hasContentDescriptionExactly(route.semantics), timeoutMillis = timeoutMillis)
    return onNode(hasContentDescriptionExactly(route.semantics)).assertExists()
}

/**
 * Asserts that a list is displayed in order.
 * @param description The content description of the list node.
 * @param expected The expected order of the list items.
 */
fun ComposeTestRule.assertListOrder(description: String, expected: List<String>, reverse: Boolean = false) {
    fun node(text: String): SemanticsNodeInteraction {
        return onNode(hasText(text, substring = true).and(hasAnyAncestor(hasContentDescriptionExactly(description)))).assertExists()
    }

    var id = if (reverse) Int.MAX_VALUE else Int.MIN_VALUE
    for (text in expected) {
        val node = node(text)
        node.printToLog("node $text")
        val nodeId = node.fetchSemanticsNode().id
        if (reverse) {
            nodeId shouldBeLessThan id
        } else {
            nodeId shouldBeGreaterThan id
        }
        id = nodeId
    }
}

/**
 * Selects a menu item in the menu.
 * @param parent The content description of the menu.
 * @param index The index of the menu item to select.
 * @param menu The text of the menu item to select.
 */
fun ComposeTestRule.selectMenuItem(parent: String, index: Int, menu: String) {
    val menuItem = onNode(hasContentDescriptionExactly(parent)).assertExists().onChildren()[index]
    if (menuItem.fetchSemanticsNode().config.getOrNull(SemanticsProperties.ContentDescription)?.any {
            it.contains(menu)
        } != true) {
        menuItem.performClick()
    } else {
        assert(false) { "Menu item not found" }
    }
}

/**
 * Replaces the text in a text field.
 * @param parent The content description of the parent node of the text field.
 * @param inputLabel The label of the text field.
 * @param text The text to replace the current text with.
 */
fun ComposeTestRule.replaceText(parent: String, inputLabel: String, text: String) {
    val node = childWithTextLabel(parent, inputLabel, "")
    node.performTextClearance()
    node.performTextInput(text)
}

/**
 * Selects a tab in the bottom navigation bar.
 * @param route The navigation route of the tab to select.
 */
fun ComposeTestRule.selectTab(route: NavRoute) {
    onNode(hasContentDescription(Localize.nav_tab_title_desc).and(hasText(route.title))).performClick()
}

@OptIn(ExperimentalTestApi::class)
fun ComposeTestRule.waitForText(
    text: String,
    substring: Boolean = false,
    timeoutMillis: Long = 10000
) {
    waitUntilAtLeastOneExists(hasText(text, substring = substring), timeoutMillis = timeoutMillis)
}

@OptIn(ExperimentalTestApi::class)
fun ComposeTestRule.waitForTag(
    tag: String,
    timeoutMillis: Long = 10000
): SemanticsNodeInteraction {
    waitUntilAtLeastOneExists(hasTestTag(tag), timeoutMillis = timeoutMillis)
    return onNodeWithTag(tag).assertExists()
}

@OptIn(ExperimentalTestApi::class)
fun ComposeTestRule.sleep(
    timeoutMillis: Long
) {
    @Suppress("SwallowedException")
    try {
        waitUntilAtLeastOneExists(hasText("NeverFound!"), timeoutMillis = timeoutMillis)
    } catch (t: Throwable) {
        // swallow this exception
    }
}

fun ComposeTestRule.textIsDisplayed(
    text: String,
    substring: Boolean = false,
    expectedOccurrences: Int = 1
) {
    if (expectedOccurrences == 1) {
        onNodeWithText(text, substring = substring).assertIsDisplayed()
    } else {
        TestCase.assertEquals(onAllNodesWithText(text, substring = substring).fetchSemanticsNodes().size, expectedOccurrences)
    }
}


@OptIn(ExperimentalTestApi::class)
fun ComposeTestRule.waitForDialog(tag: String, timeoutMillis: Long = 10000): SemanticsNodeInteraction {
    val matcher = hasTestTag(tag).and(hasAnyAncestor(isDialog()))
    waitUntilAtLeastOneExists(matcher, timeoutMillis = timeoutMillis)
    return onNode(matcher).assertExists()
}


fun ComposeTestRule.textDoesNotExist(
    text: String
) {
    onNodeWithText(text).assertDoesNotExist()
}

fun ComposeTestRule.textIsDisplayedAtLeastOnce(
    text: String,
    minOccurrences: Int = 1
) {
    Assert.assertTrue(this.onAllNodesWithText(text).fetchSemanticsNodes().size == minOccurrences)
}

fun ComposeTestRule.pressButton(semantics: String) {
    onNodeWithContentDescription(semantics).performClick()
}

fun ComposeTestRule.pressBackButton() {
    onNodeWithTag("back_button").performClick()
}


