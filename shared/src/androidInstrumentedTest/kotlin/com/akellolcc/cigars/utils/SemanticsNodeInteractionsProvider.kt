/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/10/24, 12:27 AM
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
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsMatcher.Companion.keyIsDefined
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasStateDescription
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.hasTextExactly
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.isHeading
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.theme.Localize

/**
 * Assert node has children with content description
 * @param parent - parent node content description
 * @param expected - list of content description of nodes
 */
fun SemanticsNodeInteractionsProvider.assertHasNodes(parent: String, expected: List<String>) {
    for (tag in expected) {
        onNode(hasContentDescription(parent).and(hasAnyDescendant(hasContentDescription(tag)))).assertExists()
    }
}

/**
 * Assert node has no children with content description
 * @param parent - parent node content description
 * @param expected - list of content description of nodes to look for
 */
fun SemanticsNodeInteractionsProvider.assertNoNodes(parent: String, expected: List<String>) {
    for (tag in expected) {
        onNode(hasContentDescription(parent).and(hasAnyDescendant(hasContentDescription(tag)))).assertDoesNotExist()
    }
}

/**
 *  Search for TextStyled node with label and text
 * @param parentTag - content description of parent node
 * @param label - label of child node
 * @param text - text of child node
 * @param substring - search for substring
 */
fun SemanticsNodeInteractionsProvider.childWithTextLabel(
    parentTag: String,
    label: String,
    text: String,
    substring: Boolean = false
): SemanticsNodeInteraction {
    //onNode(hasContentDescription(parentTag)).printToLog("childWithTextLabel  '${label}'  '${text}'")
    return onNode(
        (hasContentDescription(label).and(
            if (text.isNotEmpty()) {
                hasText(
                    text,
                    substring = substring
                )
            } else {
                SemanticsMatcher("True") {
                    true
                }
            }
        )).and(hasAnyAncestor(hasContentDescription(parentTag)))
    )
}

/**
 * Assert Values Card controls
 * @param tag - tag of Values Card
 * @param label - label of Values Card
 * @param vertical - vertical or horizontal
 * @param action - click action or not
 */
fun SemanticsNodeInteractionsProvider.assertValuesCard(
    tag: String,
    label: String,
    vertical: Boolean = false,
    action: Boolean = true,
): SemanticsNodeInteraction {
    val node = onNodeWithContentDescription(tag).assertExists()
    onNode((hasContentDescription(tag).and(hasStateDescription(vertical.toString()))).and(hasAnyDescendant(hasContentDescription(label)))).assertExists()
    onNode(
        hasContentDescription(tag).and(
            hasAnyDescendant(
                if (action) {
                    hasClickAction()
                } else {
                    SemanticsMatcher("True") { true }
                }))).assertExists()
    return node
}

/**
 * Perform action on Values Card
 * @param tag - Content description of Values Card
 */
fun SemanticsNodeInteractionsProvider.performValuesCardAction(
    tag: String
) {
    onNode((hasClickAction().and(hasAnyAncestor(isHeading()))).and(hasAnyAncestor(hasContentDescription(tag)))).performClick()
}

/**
 * Assert Values Card values
 * @param tag - content description of Values Card
 * @param values - map of values
 */
fun SemanticsNodeInteractionsProvider.assertValuesCardValues(
    tag: String,
    values: Map<String, String>
) {
    val node = onNodeWithContentDescription(tag).onChildren()
    values.forEach { (key, value) ->
        val matcher = hasAnyDescendant(hasContentDescription("$key $value"))
        node.filterToOne(matcher).assertExists()
    }
}

/**
 * Assert Value Picker controls
 * @param description - Content description of Values Card
 * @param value - Selected value
 * @param expanded - Expanded or not
 */
fun SemanticsNodeInteractionsProvider.assertValuePicker(
    description: String,
    value: String,
    expanded: Boolean
): SemanticsNodeInteraction {
    return onNode(hasContentDescription(description).and(hasStateDescription("${value}:${expanded}"))).assertExists()
}

/**
 * Expand Value Picker
 */
fun SemanticsNodeInteractionsProvider.expandValuePicker(
    description: String
) {
    onNode(
        hasContentDescription(Localize.value_picker_drop_down_action)
            .and(
                hasAnyAncestor(
                    hasContentDescription(description).and(keyIsDefined(SemanticsProperties.SelectableGroup))
                )
            )
    ).performClick()
}

/**
 * Select item from Value Picker
 * @param description - Content description of Values Card
 * @param value - Selected value
 */
fun SemanticsNodeInteractionsProvider.performSelectValuePicker(
    description: String,
    value: String
) {
    onNode(
        hasContentDescription(Localize.value_picker_drop_down_action)
            .and(hasAnyAncestor(hasContentDescription(description)))
    ).performClick()
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
fun SemanticsNodeInteractionsProvider.assertPickerValues(
    label: String,
    selected: String,
    values: List<String>,
) {
    assertValuePicker(label, selected, false)
    expandValuePicker(label)
    assertValuePicker(label, selected, true)
    val root = onNodeWithContentDescription(Localize.value_picker_drop_down_menu)
    root.printToLog("")
    val node = root.onChildren()
    values.forEach {
        node.filterToOne(hasAnyDescendant(hasContentDescription(it))).assertExists()
    }
    expandValuePicker(label)
    assertValuePicker(label, selected, false)
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


