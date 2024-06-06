/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/6/24, 1:58 PM
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
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.hasTextExactly
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.onAncestors
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.screens.components.ValuesCardTags

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
 * @param parentTag - tag of parent node
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
    onNode(hasContentDescription(parentTag)).printToLog("-------------childWithTextLabel  '${label}'  '${text}' -------------")
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


fun SemanticsNodeInteractionsProvider.assertPickerValues(
    parentTag: String,
    label: String,
    text: String,
    values: List<String>,
    sleep: (Long) -> Unit,
    substring: Boolean = false,
    useUnmergedTree: Boolean = false
) {
    var node = childWithTextLabel(parentTag, label, text, substring).assertExists()
    node = node.onAncestors().filterToOne(hasTestTag("value_picker")).assertExists()
    node = node.onChildren().filterToOne(hasTestTag("value_picker_drop_down")).assertExists().performClick()
    sleep(500)
    //assertListOrder("value_picker_list", values)
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

fun SemanticsNodeInteractionsProvider.assertValuesCard(
    tag: String,
    label: String,
    vertical: Boolean = false,
    action: Boolean = true,
    useUnmergedTree: Boolean = false
): SemanticsNodeInteraction {
    onNodeWithTag(tag).assertExists()
    childWithTag(
        tag,
        if (vertical) ValuesCardTags.VALUES_CARD_VERTICAL_TAG else ValuesCardTags.VALUES_CARD_HORIZONTAL_TAG,
        useUnmergedTree
    ).assertExists()
    childWithText(tag, label, useUnmergedTree).assertExists()
    val child = childWithTag(tag, ValuesCardTags.VALUES_CARD_ACTION_TAG)
    if (action) {
        child.assertExists()
    } else {
        child.assertDoesNotExist()
    }
    return onNodeWithTag(tag, useUnmergedTree)
}

fun SemanticsNodeInteractionsProvider.assertValuesCardValues(
    tag: String,
    values: Map<String, String>
) {
    val node = onNodeWithTag(tag, true).onChildren()
    values.forEach { (key, value) ->
        val matcher = hasAnyDescendant(
            hasTestTag(ValuesCardTags.VALUE_CARD_TAG).and(
                hasAnyDescendant(hasTestTag(ValuesCardTags.VALUE_CARD_LABEL_TAG).and(hasText(key))).and(
                    hasAnyDescendant(
                        hasTestTag(ValuesCardTags.VALUE_CARD_VALUE_TAG).and(hasText(value))
                    )
                )
            )
        )
        node.filterToOne(matcher).assertExists()
    }
}