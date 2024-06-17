/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/17/24, 1:42 PM
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
    private val delimiters = arrayOf("' ", "/", "“")

    private enum class State {
        Start,
        Inches,
        PartOne,
        PartTwo,
        Error
    }

    override fun toTransformed(text: String): String {
        if (text.isEmpty()) return text
        var state = State.Start
        var transformed = ""
        text.forEach {
            when {
                it.isDigit() -> {
                    transformed += it
                    if (state == State.Start) state = State.Inches
                }

                it.isWhitespace() -> {
                    if (state == State.Inches) {
                        transformed += delimiters[0]
                        state = State.PartOne
                    }
                }

                it == '.' || it == ',' -> {
                    if (state == State.PartOne) {
                        transformed += delimiters[1]
                        state = State.PartTwo
                    }
                }
            }
        }
        if (state == State.PartTwo) transformed += delimiters[2]
        Log.debug("toTransformed: from '$text' to '$transformed'")
        return transformed
    }

    override fun fromTransformed(text: String): String {
        if (text.isEmpty()) return text
        val from = text.replace("'", " ")
            .replace("/", ".")
            .replace("“", "")
            .replace("  ", " ")
        Log.debug("fromTransformed: from '$text' to '$from'")
        return from
    }

    override fun validate(text: String): Boolean {
        if (text.isEmpty()) return true
        if (text.length > 8) return false
        Log.debug("validate: '$text'")
        var state = State.Start
        text.forEach {
            when {
                it.isDigit() -> {
                    state = when (state) {
                        State.Start -> State.Inches
                        else -> state
                    }
                }

                it.isWhitespace() -> {
                    state = when (state) {
                        State.Inches -> State.PartOne
                        else -> State.Error
                    }
                }

                it == '.' || it == ',' -> {
                    state = when (state) {
                        State.PartOne -> State.PartTwo
                        else -> State.Error
                    }
                }

                else -> state = State.Error
            }
            if (state == State.Error) return false
        }
        return true
    }

    override fun filter(text: AnnotatedString): TransformedText {
        Log.debug("filter: '$text'")
        val transformedText = AnnotatedString(toTransformed(text.text))
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val parts = text.text.substring(0, offset).split(" ")
                val off = when (parts.size) {
                    1 -> offset
                    else -> offset + 1
                }
                Log.debug("originalToTransformed: '$text' offset: $offset -> $off")
                return off
            }

            override fun transformedToOriginal(offset: Int): Int {
                val parts = text.text.split(" ")
                val off = when (parts.size) {
                    1 -> offset
                    else -> offset - 1
                }
                Log.debug("transformedToOriginal: '$text' offset: $offset -> $off")
                return off
            }

        }
        return TransformedText(transformedText, offsetMapping)
    }

}