/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/24/24, 6:45 PM
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

package com.akellolcc.cigars.screens.components.transformations

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import com.akellolcc.cigars.logging.Log


class InchesVisualTransformation : ExtendedVisualTransformation() {
    private val delimiters = arrayOf("'", "/", "“")

    private enum class State {
        Start,
        Inches,
        InchesFirst,
        PartOne,
        PartTwo,
        Error
    }

    override fun toTransformed(text: String, tag: String?): String {
        if (text.isEmpty()) return text
        var state = State.Start
        var transformed = ""
        text.forEach {
            when {
                it.isDigit() -> {
                    transformed += it
                    if (state == State.Start) state = State.Inches
                }

                else -> {
                    when (state) {
                        State.Inches -> {
                            transformed += delimiters[0]
                            state = State.PartOne
                        }

                        State.PartOne -> {
                            transformed += delimiters[1]
                            state = State.PartTwo
                        }

                        else -> {}
                    }
                }
            }
        }
        if (state == State.Inches && transformed.last().isDigit()) {
            transformed += delimiters[0]
        }
        if (state == State.PartTwo && transformed.last().isDigit()) transformed += delimiters[2]
        //   Log.debug("$tag: Transformed: from '$text' to '$transformed'")
        return transformed
    }

    override fun fromTransformed(text: String, tag: String?): String {
        if (text.isEmpty()) return text
        var state = State.Start
        var transformed = ""
        text.forEach {
            when {
                it.isDigit() -> {
                    transformed += it
                    if (state == State.Start) state = State.Inches
                }

                else -> {
                    when (state) {
                        State.Inches -> {
                            state = State.InchesFirst
                        }

                        State.InchesFirst -> {
                            transformed += '.'
                            state = State.PartOne
                        }

                        State.PartOne -> {
                            transformed += '.'
                            state = State.PartTwo
                        }

                        else -> {}
                    }
                }
            }
        }
        Log.debug("$tag: from '$text' to '$transformed'")
        return transformed
    }

    override fun validate(text: String, tag: String?): Boolean {
        if (text.isEmpty()) return true
        if (text.length > 8) return false
        Log.debug("$tag: '$text'")
        return currentState(text, text.length) != State.Error
    }

    private fun currentState(text: String, offset: Int): State {
        if (text.isEmpty()) return State.Start
        var state = State.Start
        text.forEach {
            when {
                it.isDigit() -> {
                    state = when (state) {
                        State.Start -> State.Inches
                        else -> state
                    }
                }

                else -> {
                    state = when (state) {
                        State.Inches -> State.PartOne
                        State.PartOne -> State.PartTwo
                        else -> State.Error
                    }
                }
            }
            if (state == State.Error) return State.Error
        }
        return state
    }

    private fun offsetState(original: String, offset: Int): Int {
        var state = State.Start
        var position = 0
        original.forEachIndexed { index, it ->
            if (index == offset) return position
            when {
                it.isDigit() -> {
                    position++
                    if (state == State.Start) state = State.Inches
                }

                else -> {
                    when (state) {
                        State.Inches -> {
                            position += delimiters[0].length
                            state = State.PartOne
                        }

                        State.PartOne -> {
                            position += delimiters[1].length
                            state = State.PartTwo
                        }

                        else -> {}
                    }
                }
            }
        }
        if (state == State.PartTwo) position += delimiters[2].length
        return position
    }


    override fun filter(text: AnnotatedString): TransformedText {
        val transformedText = AnnotatedString(toTransformed(text.text, "Filter"))
        Log.debug("Filter: '$text' -> '$transformedText'")
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset == 0) return 0
                Log.debug("originalToTransformed: '$text' -> '$transformedText' -> offset: $offset -> ${transformedText.length}")
                return offsetState(text.text, offset)
            }

            override fun transformedToOriginal(offset: Int): Int {
                var state = State.Start
                var position = 0
                text.forEachIndexed { index, it ->
                    if (index == offset) return position
                    when {
                        it.isDigit() -> {
                            position++
                            if (state == State.Start) state = State.Inches
                        }

                        else -> {
                            when (state) {
                                State.Inches -> {
                                    position++
                                    state = State.InchesFirst
                                }

                                State.InchesFirst -> {
                                    state = State.PartOne
                                }

                                State.PartOne -> {
                                    position++
                                    state = State.PartTwo
                                }

                                else -> {}
                            }
                        }
                    }
                }
                Log.debug("transformedToOriginal: '$text' offset: $offset -> $position")
                return position
            }
        }
        return TransformedText(transformedText, offsetMapping)
    }

}