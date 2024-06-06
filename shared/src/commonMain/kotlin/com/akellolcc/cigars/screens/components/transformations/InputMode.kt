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

import androidx.compose.runtime.Stable
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import kotlin.jvm.JvmInline

@JvmInline
value class InputMode(private val value: Int) {
    fun toKeyboardType(): KeyboardType {
        return when (value) {
            1 -> KeyboardType.Text
            3 -> KeyboardType.Number
            10 -> KeyboardType.Number
            11 -> KeyboardType.Decimal
            12 -> KeyboardType.Number
            13 -> KeyboardType.Decimal
            else -> KeyboardType.Text
        }
    }

    fun visualTransformation(): VisualTransformation {
        return when (value) {
            10 -> InchesVisualTransformation()
            11 -> PriceVisualTransformation()
            3 -> NumberVisualTransformation()
            else -> VisualTransformation.None
        }
    }

    fun validate(text: String): Boolean {
        val transformation = visualTransformation()
        if (transformation is ExtendedVisualTransformation) {
            return transformation.validate(text)
        }
        return true
    }

    fun fromTransformed(text: String?): String {
        if (text.isNullOrEmpty()) return ""
        val transformation = visualTransformation()
        if (transformation is ExtendedVisualTransformation) {
            return transformation.fromTransformed(text)
        }
        return text
    }

    fun toTransformed(text: String?): String {
        if (text.isNullOrEmpty()) return ""
        val transformation = visualTransformation()
        if (transformation is ExtendedVisualTransformation) {
            return transformation.toTransformed(text)
        }
        return text
    }

    companion object {
        @Stable
        val Text: InputMode = InputMode(1)

        @Stable
        val Number: InputMode = InputMode(3)

        @Stable
        val Inches: InputMode = InputMode(10)

        @Stable
        val Price: InputMode = InputMode(11)

        @Stable
        val Temperature: InputMode = InputMode(12)

        @Stable
        val Humidity: InputMode = InputMode(13)

    }
}