/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/20/24, 12:08 PM
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

class PriceVisualTransformation : ExtendedVisualTransformation() {
    override fun toTransformed(text: String, tag: String?): String {
        val transformed = when (text.length) {
            0 -> "0.00"
            1 -> "0.0${text}"
            2 -> "0.${text}"
            else -> {
                val cents = text.substring(text.length - 2)
                val dollars = text.substring(0, text.length - 2)
                "$dollars.$cents"
            }
        }
        Log.debug("toTransformed: $text -> $transformed")
        return transformed
    }

    override fun fromTransformed(text: String, tag: String?): String {
        Log.debug("fromTransformed: $text")
        if (text.isEmpty()) return text
        val original = text.filter { it.isDigit() }
        return original.trimStart('0')
    }

    override fun validate(text: String, tag: String?): Boolean {
        if (text.isEmpty()) return true
        try {
            text.toDouble()
        } catch (_: Exception) {
            return false
        }
        return true
    }

    override fun filter(text: AnnotatedString): TransformedText {
        val transformedText = AnnotatedString(toTransformed(text.text))
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                Log.debug("originalToTransformed: $text $offset")
                return if (offset < 3) 4 else offset + 1
            }

            override fun transformedToOriginal(offset: Int): Int {
                Log.debug("transformedToOriginal: $text $offset")
                return text.length
            }

        }
        return TransformedText(transformedText, offsetMapping)
    }
}