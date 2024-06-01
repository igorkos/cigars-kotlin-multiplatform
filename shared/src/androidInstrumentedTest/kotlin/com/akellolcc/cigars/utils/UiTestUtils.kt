/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/1/24, 3:27 PM
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

import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionCollection
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasAnySibling
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.hasTextExactly
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onAncestors
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.test.platform.app.InstrumentationRegistry
import com.akellolcc.cigars.logging.Log
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import junit.framework.TestCase.assertEquals
import org.junit.Assert.assertTrue

object UiTestUtils {
    // discussed later
    val context = InstrumentationRegistry.getInstrumentation().context
    val testContext = InstrumentationRegistry.getInstrumentation().context
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
) {
    waitUntilAtLeastOneExists(hasTestTag(tag), timeoutMillis = timeoutMillis)
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
        assertEquals(onAllNodesWithText(text, substring = substring).fetchSemanticsNodes().size, expectedOccurrences)
    }
}

fun SemanticsNodeInteractionsProvider.childWithTag(
    parentTag: String,
    tag: String,
    useUnmergedTree: Boolean = false
): SemanticsNodeInteraction {
    val matcher = hasTestTag(tag).and(hasAnyAncestor(hasTestTag(parentTag)))
    return onNode(
        matcher,
        useUnmergedTree = useUnmergedTree
    )
}

fun SemanticsNodeInteractionsProvider.assertHasNodesWithTag(expected: List<String>) {
    for (tag in expected) {
        onNodeWithTag(tag).assertExists()
    }
}

fun SemanticsNodeInteractionsProvider.assertNoNodesWithTag(expected: List<String>) {
    for (tag in expected) {
        onNodeWithTag(tag).assertDoesNotExist()
    }
}


fun SemanticsNodeInteractionsProvider.assertNodeText(
    tag: String,
    text: String,
    substring: Boolean = false,
    useUnmergedTree: Boolean = false
): SemanticsNodeInteraction {
    val matcher = hasTestTag(tag).and(if (substring) hasText(text) else hasTextExactly(text))
    return onNode(
        matcher,
        useUnmergedTree = useUnmergedTree
    ).assertExists()
}

internal fun textMatcher(node: SemanticsNode, text: String, substring: Boolean): Boolean {
    val nodeText = node.config.getOrNull(SemanticsProperties.EditableText)?.text
    var isFindText = if (substring) {
        nodeText?.contains(text, false) ?: false
    } else {
        nodeText.equals(text, false)
    }
    if (!isFindText) {
        isFindText = node.config.getOrNull(SemanticsProperties.Text)
            ?.any { item ->
                if (substring) {
                    item.text.contains(text, false)
                } else {
                    item.text.equals(text, false)
                }
            } ?: false
    }
    return isFindText
}

fun SemanticsNodeInteractionsProvider.childWithText(
    parentTag: String,
    text: String,
    substring: Boolean = false,
    useUnmergedTree: Boolean = false
): SemanticsNodeInteraction {
    Log.debug("-------------childWithText  '${text}' -------------")
    val matcher = SemanticsMatcher("Search node with text: $text and parent tag: $parentTag") {
        if (textMatcher(it, text, substring)) {
            return@SemanticsMatcher hasAnyAncestor(hasTestTag(parentTag)).matches(it)
        }
        return@SemanticsMatcher false
    }
    return onNode(matcher, useUnmergedTree = useUnmergedTree)
}

fun SemanticsNodeInteractionsProvider.childWithTextLabel(
    parentTag: String,
    label: String,
    text: String,
    substring: Boolean = false,
    useUnmergedTree: Boolean = false
): SemanticsNodeInteraction {
    // Log.debug("-------------childWithTextLabel  '${label}'  '${text}' -------------")
    val matcher = SemanticsMatcher("Search node with text: $text, label $label  and parent tag: $parentTag") { node ->
        if (textMatcher(node, text, substring)) {
            //  Log.debug("nodeText matches '${text}'")
            if (!hasAnySibling(SemanticsMatcher("") {
                    textMatcher(it, label, true)
                }).matches(node)) {
                if (hasAnyDescendant(SemanticsMatcher("") {
                        Log.debug("Match Descendant '$label'")
                        textMatcher(it, label, true)
                    }).matches(node)) {
                    //       Log.debug("nodeLabel child matches '${label}'")
                    return@SemanticsMatcher hasAnyAncestor(hasTestTag(parentTag)).matches(node)
                }
                return@SemanticsMatcher false
            }
            //    Log.debug("nodeLabel sibling matches '${label}'")
            return@SemanticsMatcher hasAnyAncestor(hasTestTag(parentTag)).matches(node)
        }
        return@SemanticsMatcher false
    }
    return onNode(matcher, useUnmergedTree = useUnmergedTree)
}

fun SemanticsNodeInteractionsProvider.assertPickerValues(
    parentTag: String,
    label: String,
    text: String,
    values: List<String>,
    sleep: (Long) -> Unit,
    substring: Boolean = false,
    useUnmergedTree: Boolean = false
) {
    var node = childWithTextLabel(parentTag, label, text, substring, useUnmergedTree).assertExists()
    node = node.onAncestors().filterToOne(hasTestTag("value_picker")).assertExists()
    node = node.onChildren().filterToOne(hasTestTag("value_picker_drop_down")).assertExists().performClick()
    sleep(500)
    onNodeWithTag("value_picker_list").assertExists().onChildren().assertListOrder(values.size, values)
    node.performClick()
    sleep(500)
}

fun SemanticsNodeInteractionsProvider.dialogWithTag(tag: String): SemanticsNodeInteraction {
    val matcher = hasTestTag(tag).and(hasAnyAncestor(isDialog()))
    return onNode(matcher)
}

fun SemanticsNodeInteractionsProvider.childButtonWithText(
    parentTag: String,
    label: String,
    useUnmergedTree: Boolean = false
): SemanticsNodeInteraction {
    val matcher = (hasTextExactly(label).and(hasClickAction())).and(hasAnyAncestor(hasTestTag(parentTag)))
    return onNode(
        matcher,
        useUnmergedTree = useUnmergedTree
    )
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
    assertTrue(this.onAllNodesWithText(text).fetchSemanticsNodes().size == minOccurrences)
}

fun ComposeTestRule.pressButton(text: String) {
    onNodeWithText(text).performClick()
}

fun ComposeTestRule.pressBackButton() {
    onNodeWithTag("back_button").performClick()
}


fun ComposeTestRule.replaceText(inputLabel: String, text: String) {
    onNodeWithText(inputLabel).performTextClearance()
    onNodeWithText(inputLabel).performTextInput(text)
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

fun SemanticsNodeInteractionCollection.assertListOrder(count: Int, expected: List<String>) {
    val nodes = fetchSemanticsNodes()
    val active = nodes.filter { !it.layoutInfo.isDeactivated }
    active.size shouldBe count
    for (i in active.indices) {
        val node = getOrNull(i)
        node?.assertTextContains(expected[i], true)
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