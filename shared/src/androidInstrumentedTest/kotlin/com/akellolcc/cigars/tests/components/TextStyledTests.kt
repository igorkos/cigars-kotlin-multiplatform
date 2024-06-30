/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/24/24, 6:22 PM
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

package com.akellolcc.cigars.tests.components

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasStateDescription
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.unit.dp
import clearText
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.screens.components.TextStyled
import com.akellolcc.cigars.screens.components.TextStyledState
import com.akellolcc.cigars.screens.components.transformations.InputMode
import com.akellolcc.cigars.theme.DefaultTheme
import com.akellolcc.cigars.theme.Localize
import com.akellolcc.cigars.theme.MaterialColors
import com.akellolcc.cigars.theme.TextStyles
import com.akellolcc.cigars.theme.materialColor
import com.akellolcc.cigars.utils.childWithTextLabel
import com.akellolcc.cigars.utils.setAppContext
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.runners.MethodSorters
import performTextClearance
import performTextInput
import replaceText
import textInputState
import kotlin.test.Test

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class TextStyledTests {

    @get:Rule(order = 0)
    val composeTestRule = createAndroidComposeRule(ComponentActivity::class.java)

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Test
    fun test1_DisplayTextNoLabel() {
        val semantics = "Test card"
        val text = "Test text"
        with(composeTestRule) {
            setContent {
                setAppContext(LocalContext.current)
                Log.initLog { }
                DefaultTheme {
                    Scaffold(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CompositionLocalProvider(LocalContentColor provides materialColor(MaterialColors.color_onPrimaryContainer)) {
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = materialColor(MaterialColors.color_primaryContainer),
                                ),
                                modifier = Modifier.fillMaxWidth().padding(24.dp).semantics {
                                    contentDescription = semantics
                                }
                            ) {
                                TextStyled(
                                    text = text,
                                    Localize.cigar_details_name,
                                    style = TextStyles.Headline,
                                    labelStyle = TextStyles.None,
                                )
                            }
                        }
                    }
                }
            }
            onNodeWithContentDescription(semantics)
            childWithTextLabel(semantics, Localize.cigar_details_name, text)
        }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Test
    fun test2_DisplayTextLabel() {
        val semantics = "Test card"
        val text = "Test text"
        with(composeTestRule) {
            setContent {
                setAppContext(LocalContext.current)
                Log.initLog { }
                DefaultTheme {
                    Scaffold(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CompositionLocalProvider(LocalContentColor provides materialColor(MaterialColors.color_onPrimaryContainer)) {
                            OutlinedCard(
                                colors = CardDefaults.cardColors(
                                    containerColor = materialColor(MaterialColors.color_primaryContainer),
                                ),
                                modifier = Modifier.fillMaxWidth().height(150.dp).padding(24.dp).semantics {
                                    contentDescription = semantics
                                }
                            ) {
                                TextStyled(
                                    text = text,
                                    Localize.cigar_details_name,
                                    style = TextStyles.Headline,
                                    modifier = Modifier.padding(16.dp),
                                )
                            }
                        }
                    }
                }
            }
            onNodeWithContentDescription(semantics)
            childWithTextLabel(semantics, Localize.cigar_details_name, text)
            onNode(
                hasContentDescription(semantics).and(
                    hasAnyDescendant(
                        hasStateDescription(
                            TextStyledState(
                                editable = false,
                                vertical = false,
                                labelVisible = true
                            ).toString()
                        )
                    )
                )
            ).assertExists()
        }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Test
    fun test3_DisplayTextLabelVertical() {
        val semantics = "Test card"
        val text = "Test text"
        with(composeTestRule) {
            setContent {
                setAppContext(LocalContext.current)
                Log.initLog { }
                DefaultTheme {
                    Scaffold(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CompositionLocalProvider(LocalContentColor provides materialColor(MaterialColors.color_onPrimaryContainer)) {
                            OutlinedCard(
                                colors = CardDefaults.cardColors(
                                    containerColor = materialColor(MaterialColors.color_primaryContainer),
                                ),
                                modifier = Modifier.fillMaxWidth().height(150.dp).padding(24.dp).semantics {
                                    contentDescription = semantics
                                }
                            ) {
                                TextStyled(
                                    text = text,
                                    Localize.cigar_details_name,
                                    vertical = true,
                                    style = TextStyles.Headline,
                                    modifier = Modifier.padding(16.dp),
                                )
                            }
                        }
                    }
                }
            }
            onNodeWithContentDescription(semantics)
            childWithTextLabel(semantics, Localize.cigar_details_name, text)
            onNode(
                hasContentDescription(semantics).and(
                    hasAnyDescendant(
                        hasStateDescription(
                            TextStyledState(
                                editable = false,
                                vertical = true,
                                labelVisible = true
                            ).toString()
                        )
                    )
                )
            ).assertExists()
        }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Test
    fun test4_EditTextStyledTextLabel() {
        val semantics = "Test card"
        with(composeTestRule) {
            setContent {
                setAppContext(LocalContext.current)
                Log.initLog { }
                val text = remember { mutableStateOf<String?>(null) }
                DefaultTheme {
                    Scaffold(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CompositionLocalProvider(LocalContentColor provides materialColor(MaterialColors.color_onPrimaryContainer)) {
                            OutlinedCard(
                                colors = CardDefaults.cardColors(
                                    containerColor = materialColor(MaterialColors.color_primaryContainer),
                                ),
                                modifier = Modifier.fillMaxWidth().height(150.dp).padding(24.dp).semantics {
                                    contentDescription = semantics
                                }
                            ) {
                                TextStyled(
                                    text = text.value,
                                    Localize.cigar_details_name,
                                    style = TextStyles.Headline,
                                    editable = true,
                                    modifier = Modifier.padding(16.dp),
                                    onValueChange = { text.value = it }
                                )
                            }
                        }
                    }
                }
            }
            childWithTextLabel(semantics, Localize.cigar_details_name, null)
            onNode(
                hasContentDescription(semantics).and(
                    hasAnyDescendant(
                        hasStateDescription(
                            TextStyledState(
                                editable = true,
                                vertical = false,
                                labelVisible = true
                            ).toString()
                        )
                    )
                )
            ).assertExists()

            performTextInput(semantics, Localize.cigar_details_name, "a")
            onNodeWithContentDescription(semantics)
            childWithTextLabel(semantics, Localize.cigar_details_name, "a").assertExists()

            performTextInput(semantics, Localize.cigar_details_name, "b")
            childWithTextLabel(semantics, Localize.cigar_details_name, "ab").assertExists()

            performTextClearance(semantics, Localize.cigar_details_name)
            childWithTextLabel(semantics, Localize.cigar_details_name, null).assertExists()
        }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Test
    fun test5_EditNumericStyledTextLabel() {
        val semantics = "Test card"
        with(composeTestRule) {
            setContent {
                setAppContext(LocalContext.current)
                Log.initLog { }
                val number = remember { mutableStateOf<Int?>(null) }
                DefaultTheme {
                    Scaffold(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CompositionLocalProvider(LocalContentColor provides materialColor(MaterialColors.color_onPrimaryContainer)) {
                            OutlinedCard(
                                colors = CardDefaults.cardColors(
                                    containerColor = materialColor(MaterialColors.color_primaryContainer),
                                ),
                                modifier = Modifier.fillMaxWidth().height(150.dp).padding(24.dp).semantics {
                                    contentDescription = semantics
                                }
                            ) {
                                TextStyled(
                                    text = number.value?.toString(),
                                    Localize.cigar_details_name,
                                    style = TextStyles.Headline,
                                    editable = true,
                                    modifier = Modifier.padding(16.dp),
                                    inputMode = InputMode.Number,
                                    onValueChange = { number.value = it.toIntOrNull() }
                                )
                            }
                        }
                    }
                }
            }
            childWithTextLabel(semantics, Localize.cigar_details_name, null)
            onNode(
                hasContentDescription(semantics).and(
                    hasAnyDescendant(
                        hasStateDescription(
                            TextStyledState(
                                editable = true,
                                vertical = false,
                                labelVisible = true
                            ).toString()
                        )
                    )
                )
            ).assertExists()

            performTextInput(semantics, Localize.cigar_details_name, "1")
            onNodeWithContentDescription(semantics)
            childWithTextLabel(semantics, Localize.cigar_details_name, "1").assertExists()

            performTextInput(semantics, Localize.cigar_details_name, "2")
            childWithTextLabel(semantics, Localize.cigar_details_name, "12").assertExists()

            replaceText(semantics, Localize.cigar_details_name, "")
            childWithTextLabel(semantics, Localize.cigar_details_name, null).assertExists()
        }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Test
    fun test6_EditInchesStyledTextLabel() {
        val semantics = "Test card"
        with(composeTestRule) {
            setContent {
                setAppContext(LocalContext.current)
                Log.initLog { }
                val number = remember { mutableStateOf<String?>("1'") }
                DefaultTheme {
                    Scaffold(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CompositionLocalProvider(LocalContentColor provides materialColor(MaterialColors.color_onPrimaryContainer)) {
                            OutlinedCard(
                                colors = CardDefaults.cardColors(
                                    containerColor = materialColor(MaterialColors.color_primaryContainer),
                                ),
                                modifier = Modifier.fillMaxWidth().height(150.dp).padding(24.dp).semantics {
                                    contentDescription = semantics
                                }
                            ) {
                                TextStyled(
                                    text = number.value,
                                    Localize.cigar_details_name,
                                    style = TextStyles.Headline,
                                    editable = true,
                                    modifier = Modifier.padding(16.dp),
                                    inputMode = InputMode.Inches,
                                    onValueChange = { number.value = it }
                                )
                            }
                        }
                    }
                }
            }
            childWithTextLabel(semantics, Localize.cigar_details_name, null)
            onNode(
                hasContentDescription(semantics).and(
                    hasAnyDescendant(
                        hasStateDescription(
                            TextStyledState(
                                editable = true,
                                vertical = false,
                                labelVisible = true
                            ).toString()
                        )
                    )
                )
            ).assertExists()

            textInputState(semantics, Localize.cigar_details_name, "1")
            clearText(semantics, Localize.cigar_details_name)

            replaceText(semantics, Localize.cigar_details_name, "2")
            textInputState(semantics, Localize.cigar_details_name, "2")
            childWithTextLabel(semantics, Localize.cigar_details_name, "2'", true).assertExists()

            replaceText(semantics, Localize.cigar_details_name, "3.")
            textInputState(semantics, Localize.cigar_details_name, "3.")
            childWithTextLabel(semantics, Localize.cigar_details_name, "3' ", true).assertExists()

            replaceText(semantics, Localize.cigar_details_name, "4.2.3“")
            textInputState(semantics, Localize.cigar_details_name, "4.2.3")
            childWithTextLabel(semantics, Localize.cigar_details_name, "4' 2/3“", true).assertExists()

            //sleep(1000000000000000L)
            Log.debug("Input '1'")
            performTextInput(semantics, Localize.cigar_details_name, "1")
            childWithTextLabel(semantics, Localize.cigar_details_name, "1'", true).assertExists()

            Log.debug("Input '2'")
            performTextInput(semantics, Localize.cigar_details_name, "2")
            childWithTextLabel(semantics, Localize.cigar_details_name, "12'", true).assertExists()

            Log.debug("Input ' '")
            performTextInput(semantics, Localize.cigar_details_name, " ")
            childWithTextLabel(semantics, Localize.cigar_details_name, "12' ", true).assertExists()

            Log.debug("Input '3'")
            performTextInput(semantics, Localize.cigar_details_name, "3")
            childWithTextLabel(semantics, Localize.cigar_details_name, "12' 3", true).assertExists()

            Log.debug("Input '.'")
            performTextInput(semantics, Localize.cigar_details_name, ".")
            childWithTextLabel(semantics, Localize.cigar_details_name, "12' 3/", true).assertExists()

            Log.debug("Input '4'")
            performTextInput(semantics, Localize.cigar_details_name, "4")
            childWithTextLabel(semantics, Localize.cigar_details_name, "12' 3/4“", true).assertExists()

            replaceText(semantics, Localize.cigar_details_name, "")
            childWithTextLabel(semantics, Localize.cigar_details_name, null).assertExists()
        }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Test
    fun test7_EditPriceStyledTextLabel() {
        val semantics = "Test card"
        with(composeTestRule) {
            setContent {
                setAppContext(LocalContext.current)
                Log.initLog { }
                val number = remember { mutableStateOf<Double?>(null) }
                DefaultTheme {
                    Scaffold(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CompositionLocalProvider(LocalContentColor provides materialColor(MaterialColors.color_onPrimaryContainer)) {
                            OutlinedCard(
                                colors = CardDefaults.cardColors(
                                    containerColor = materialColor(MaterialColors.color_primaryContainer),
                                ),
                                modifier = Modifier.fillMaxWidth().height(150.dp).padding(24.dp).semantics {
                                    contentDescription = semantics
                                }
                            ) {
                                TextStyled(
                                    text = number.value.toString(),
                                    Localize.cigar_details_name,
                                    style = TextStyles.Headline,
                                    editable = true,
                                    modifier = Modifier.padding(16.dp),
                                    inputMode = InputMode.Price,
                                    onValueChange = { number.value = it.toDoubleOrNull() }
                                )
                            }
                        }
                    }
                }
            }
            childWithTextLabel(semantics, Localize.cigar_details_name, null)
            onNode(
                hasContentDescription(semantics).and(
                    hasAnyDescendant(
                        hasStateDescription(
                            TextStyledState(
                                editable = true,
                                vertical = false,
                                labelVisible = true
                            ).toString()
                        )
                    )
                )
            ).assertExists()

            performTextInput(semantics, Localize.cigar_details_name, "1")
            onNodeWithContentDescription(semantics)
            childWithTextLabel(semantics, Localize.cigar_details_name, "0.01", true).assertExists()

            performTextInput(semantics, Localize.cigar_details_name, "2")
            onNodeWithContentDescription(semantics)
            childWithTextLabel(semantics, Localize.cigar_details_name, "0.12", true).assertExists()

            performTextInput(semantics, Localize.cigar_details_name, "2")
            onNodeWithContentDescription(semantics)
            childWithTextLabel(semantics, Localize.cigar_details_name, "1.22", true).assertExists()

            performTextInput(semantics, Localize.cigar_details_name, "3")
            onNodeWithContentDescription(semantics)
            childWithTextLabel(semantics, Localize.cigar_details_name, "12.23", true).assertExists()

            replaceText(semantics, Localize.cigar_details_name, "")
            childWithTextLabel(semantics, Localize.cigar_details_name, null).assertExists()
        }
    }
}