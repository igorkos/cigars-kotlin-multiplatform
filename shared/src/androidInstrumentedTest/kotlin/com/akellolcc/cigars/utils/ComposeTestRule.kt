/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/24/24, 5:57 PM
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
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasContentDescriptionExactly
import androidx.compose.ui.test.hasStateDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import com.akellolcc.cigars.screens.navigation.NavRoute
import com.akellolcc.cigars.theme.Localize
import com.akellolcc.cigars.utils.expandValuePicker
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.ints.shouldBeLessThan
import io.kotest.matchers.shouldBe

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
 *
 */
@OptIn(ExperimentalTestApi::class)
fun ComposeTestRule.waitForScreen(
    route: NavRoute,
    timeoutMillis: Long = 10000
): SemanticsNodeInteraction {
    try {
        waitUntilAtLeastOneExists(
            hasContentDescriptionExactly(route.semantics).and(hasStateDescription("false")),
            timeoutMillis = timeoutMillis
        )
    } catch (t: Throwable) {
        assert(false) { "Screen ${route.route} not found" }
    }
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
        node.performScrollTo()
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
    onNode(hasText(menu).and(hasAnyAncestor(hasContentDescriptionExactly(parent)))).assertExists().performClick()
}

/**
 * Replaces the text in a text field.
 * @param parent The content description of the parent node of the text field.
 * @param inputLabel The label of the text field.
 * @param text The text to replace the current text with.
 */
fun ComposeTestRule.replaceText(parent: String, inputLabel: String, text: String) {
    onNode(
        (hasContentDescription(Localize.styled_text_field_edit).and(hasAnyAncestor(hasContentDescription(inputLabel)))).and(
            hasAnyAncestor(
                hasContentDescription(parent)
            )
        )
    ).performTextReplacement(text)
}

/**
 * Perform the text input in a text field.
 * @param parent The content description of the parent node of the text field.
 * @param inputLabel The label of the text field.
 * @param text The text to input the current text with.
 */
fun ComposeTestRule.performTextInput(parent: String, inputLabel: String, text: String) {
    onNode(
        (hasContentDescription(Localize.styled_text_field_edit).and(hasAnyAncestor(hasContentDescription(inputLabel)))).and(
            hasAnyAncestor(
                hasContentDescription(parent)
            )
        )
    ).performTextInput(text)
}

/**
 * Perform the text input in a text field.
 * @param parent The content description of the parent node of the text field.
 * @param inputLabel The label of the text field.
 * @param text The text to input the current text with.
 */
fun ComposeTestRule.textInputState(parent: String, inputLabel: String, text: String) {
    val state = onNode(
        (hasContentDescription(Localize.styled_text_field_edit).and(hasAnyAncestor(hasContentDescription(inputLabel)))).and(
            hasAnyAncestor(
                hasContentDescription(parent)
            )
        )
    ).assertExists().fetchSemanticsNode().config.getOrNull(SemanticsProperties.StateDescription)
    state shouldBe text
}

/**
 * Perform the text input in a text field.
 * @param parent The content description of the parent node of the text field.
 * @param inputLabel The label of the text field.
 * @param text The text to input the current text with.
 */
fun ComposeTestRule.clearText(parent: String, inputLabel: String) {
    val state = onNode(
        (hasContentDescription(Localize.styled_text_field_edit).and(hasAnyAncestor(hasContentDescription(inputLabel)))).and(
            hasAnyAncestor(
                hasContentDescription(parent)
            )
        )
    ).performTextClearance()
}

/**
 * Perform the text input in a text field.
 * @param parent The content description of the parent node of the text field.
 * @param inputLabel The label of the text field.
 * @param text The text to input the current text with.
 */
fun ComposeTestRule.performTextClearance(parent: String, inputLabel: String) {
    onNode(
        (hasContentDescription(Localize.styled_text_field_action).and(hasAnyAncestor(hasContentDescription(inputLabel)))).and(
            hasAnyAncestor(
                hasContentDescription(parent)
            )
        )
    ).performClick()
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

/**
 * Assert Value Picker controls
 * @param description - Content description of Values Card
 * @param value - Selected value
 * @param expanded - Expanded or not
 */
@OptIn(ExperimentalTestApi::class)
fun ComposeTestRule.assertValuePicker(
    description: String,
    value: String,
    expanded: Boolean
): SemanticsNodeInteraction {
    val mather = hasContentDescription(description).and(hasStateDescription("${value}:${expanded}"))
    try {
        waitUntilAtLeastOneExists(mather, 5000)
    } catch (t: Throwable) {
        // swallow this exception
    }
    return onNode(mather).assertExists()
}

/**
 * Select item from Value Picker
 * @param description - Content description of Values Card
 * @param value - Selected value
 */
fun ComposeTestRule.performSelectValuePicker(
    description: String,
    value: String
) {
    onNode(
        hasContentDescription(Localize.value_picker_drop_down_action)
            .and(hasAnyAncestor(hasContentDescription(description)))
    ).performClick()
    onNodeWithContentDescription(Localize.value_picker_drop_down_menu).performScrollToNode(hasContentDescription(value))
    onNode(
        hasContentDescription(value).and(
            hasAnyAncestor(
                hasContentDescription(Localize.value_picker_drop_down_menu)
            )
        )
    ).performClick()
    assertValuePicker(description, value, false)
}

/**
 * Assert Picker values
 * @param label - Content description of Picker Values
 * @param selected - Selected value
 * @param values - List of values
 */
fun ComposeTestRule.assertPickerValues(
    label: String,
    selected: String,
    values: List<String>,
) {
    assertValuePicker(label, selected, false)
    expandValuePicker(label)
    assertValuePicker(label, selected, true)
    val root = onNodeWithContentDescription(Localize.value_picker_drop_down_menu)
    val node = root.onChildren()
    values.forEach {
        node.filterToOne(hasAnyDescendant(hasContentDescription(it))).assertExists()
    }
    expandValuePicker(label)
    assertValuePicker(label, selected, false)
}
