/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/11/24, 3:10 PM
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

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasContentDescriptionExactly
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasStateDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.printToLog
import com.akellolcc.cigars.screens.navigation.NavRoute
import com.akellolcc.cigars.theme.Localize
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.ints.shouldBeLessThan

/**
 * Sleeps for a specified amount of time.
 */
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
 * Presses the back button.
 */
fun ComposeTestRule.pressBackButton() {
    onNodeWithContentDescription(Localize.button_back).performClick()
}

/**
 * Asserts that a list contains a list item with the given text.
 * @param description The content description of the list node.
 * @param text The text of the list item to assert.
 */
fun ComposeTestRule.assertListNode(description: String, text: String, substring: Boolean = true): SemanticsNodeInteraction {
    return onNode(hasText(text, substring = substring).and(hasAnyAncestor(hasContentDescriptionExactly(description)))).assertExists()
}

/**
 * Asserts that a list is displayed in order.
 * @param description The content description of the list node.
 * @param expected The expected order of the list items.
 */
fun ComposeTestRule.assertListOrder(description: String, expected: List<String>, reverse: Boolean = false, substring: Boolean = true) {
    var id = if (reverse) Int.MAX_VALUE else Int.MIN_VALUE
    for (text in expected) {
        val node = assertListNode(description, text, substring)
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
fun ComposeTestRule.selectMenuItem(parent: String, menu: String) {
    onNode(hasContentDescriptionExactly(parent)).printToLog("")
    onNode(hasText(menu).and(hasAnyAncestor(hasContentDescriptionExactly(parent)))).assertExists().performClick()
}

/**
 * Replaces the text in a text field.
 * @param parent The content description of the parent node of the text field.
 * @param inputLabel The label of the text field.
 * @param text The text to replace the current text with.
 */
fun ComposeTestRule.replaceText(parent: String, inputLabel: String, text: String, currentText: String? = null) {
    onNode(hasContentDescription(parent)).printToLog("")
    onNode((hasContentDescription(inputLabel).and(hasSetTextAction())).and(hasAnyAncestor(hasContentDescription(parent))))
        .performTextReplacement(text)
}

/**
 * Selects a tab in the bottom navigation bar.
 * @param route The navigation route of the tab to select.
 */
fun ComposeTestRule.selectTab(route: NavRoute) {
    onNode(hasContentDescription(Localize.nav_tab_title_desc).and(hasText(route.title))).performClick()
}

/**
 * Presses a button in the UI.
 * @param semantics The content description of the button to press.
 */
fun ComposeTestRule.pressButton(semantics: String) {
    onNodeWithContentDescription(semantics).performClick()
}

/**
 * Asserts that a node with 'node' content description and parent with 'parent' content description exists.
 * @param parent The content description of parent node.
 * @param node The content description of the list node.
 */
fun ComposeTestRule.assertNode(parent: String, node: String): SemanticsNodeInteraction {
    return onNode(hasContentDescriptionExactly(node).and(hasAnyAncestor(hasContentDescriptionExactly(parent)))).assertExists()
}

/**
 * Asserts that a node with 'node' content description and parent with 'parent' content description with state exist.
 * @param parent The content description of parent node.
 * @param node The content description of the list node.
 * @param state The state of the node.
 */
fun ComposeTestRule.assertNodeState(parent: String, node: String, state: String): SemanticsNodeInteraction {
    return onNode(
        (hasContentDescription(node).and(hasStateDescription(state))).and(
            hasAnyAncestor(
                hasContentDescription(parent)
            )
        )
    ).assertExists()
}

/**
 * Waits for a dialog to be displayed.
 * @param tag The content description of the dialog.
 * @param timeoutMillis The timeout in milliseconds.
 */
@OptIn(ExperimentalTestApi::class)
fun ComposeTestRule.waitForDialog(tag: String, timeoutMillis: Long = 10000): SemanticsNodeInteraction {
    waitUntilAtLeastOneExists(hasContentDescription(tag), timeoutMillis = timeoutMillis)
    return onNode(hasContentDescription(tag)).assertExists()
}
