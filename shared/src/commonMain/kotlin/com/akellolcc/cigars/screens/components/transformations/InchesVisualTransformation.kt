/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/14/24, 3:40 PM
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
    override fun toTransformed(text: String): String {
        Log.debug("toTransformed: $text")
        if (text.isEmpty()) return text
        val parts = text.split(" ", "' ", "'", "/", "“", ".", ",", "-")
        var transformed = ""
        parts.forEachIndexed { index, part ->
            if (part.isNotBlank()) {
                transformed += part + delimiters[index]
            }
        }
        return transformed
    }

    override fun fromTransformed(text: String): String {
        Log.debug("fromTransformed: $text")
        if (text.isEmpty()) return text
        val parts = text.split(" ", "' ", "'", "/", "“", ".", ",", "-")
        return parts.joinToString(" ")
    }

    override fun validate(text: String): Boolean {
        return text.length <= 8
    }

    override fun filter(text: AnnotatedString): TransformedText {
        val transformedText = AnnotatedString(toTransformed(text.text))
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val parts = text.text.substring(0, offset).split(" ")
                val off = when (parts.size) {
                    1 -> offset
                    else -> offset + 1
                }
                Log.debug("originalToTransformed: '$text' $offset -> $off")
                return off
            }

            override fun transformedToOriginal(offset: Int): Int {
                val parts = text.text.split(" ")
                val off = when (parts.size) {
                    1 -> offset
                    else -> offset - 1
                }
                Log.debug("transformedToOriginal: '$text' $offset -> $off")
                return off
            }

        }
        return TransformedText(transformedText, offsetMapping)
    }

}