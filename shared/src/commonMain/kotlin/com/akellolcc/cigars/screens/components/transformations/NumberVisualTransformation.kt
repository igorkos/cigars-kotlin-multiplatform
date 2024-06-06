/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/14/24, 2:56 PM
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

class NumberVisualTransformation : ExtendedVisualTransformation() {
    override fun toTransformed(text: String): String {
        for (i in text.indices) {
            try {
                val pref = text.substring(0, text.length - i)
                val num = pref.toDoubleOrNull()
                if (num != null && num < Long.MAX_VALUE) {
                    return pref
                }
            } catch (_: Exception) {
            }
        }
        return ""
    }

    override fun fromTransformed(text: String): String {
        return text
    }

    override fun validate(text: String): Boolean {
        if (text.isEmpty()) return true
        try {
            text.toLong()
        } catch (_: Exception) {
            return false
        }
        return true
    }

    override fun filter(text: AnnotatedString): TransformedText {
        val transformedText = AnnotatedString(toTransformed(text.text))
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return offset
            }

            override fun transformedToOriginal(offset: Int): Int {
                return offset
            }

        }
        return TransformedText(transformedText, offsetMapping)
    }
}