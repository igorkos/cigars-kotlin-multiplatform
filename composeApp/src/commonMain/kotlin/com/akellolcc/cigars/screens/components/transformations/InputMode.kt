/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/20/24, 12:13 PM
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

enum class InputType {
    Text, Number, Inches, Price, Temperature, Humidity
}

data class InputMode(private val value: InputType) {

    fun toKeyboardType(): KeyboardType {
        return when (value) {
            InputType.Text -> KeyboardType.Text
            InputType.Number -> KeyboardType.Number
            InputType.Inches -> KeyboardType.Number
            InputType.Price -> KeyboardType.Decimal
            InputType.Temperature -> KeyboardType.Number
            InputType.Humidity -> KeyboardType.Decimal
        }
    }

    val visualTransformation: VisualTransformation
        get() {
            return when (value) {
                InputType.Inches -> InchesVisualTransformation()
                InputType.Price -> PriceVisualTransformation()
                InputType.Number -> NumberVisualTransformation()
                else -> VisualTransformation.None
            }
        }

    fun validate(text: String): Boolean {
        return visualTransformation.nullOrTransform()?.validate(text, "InputMode.validate") ?: true
    }

    fun compare(transformed: String?, original: String?): Boolean {
        if (transformed.isNullOrEmpty() && original.isNullOrEmpty()) return true
        if (transformed.isNullOrEmpty() || original.isNullOrEmpty()) return false
        val fieldText = fromTransformed(transformed, "InputMode.compare")
        return fieldText == original
    }

    fun fromTransformed(text: String?, tag: String? = null): String {
        if (text.isNullOrEmpty()) return ""
        return visualTransformation.nullOrTransform()?.fromTransformed(text, tag ?: "InputMode.fromTransformed") ?: text
    }

    fun toTransformed(text: String?, tag: String? = null): String {
        if (text.isNullOrEmpty()) return ""
        return visualTransformation.nullOrTransform()?.toTransformed(text, tag ?: "InputMode.toTransformed") ?: text
    }

    companion object {
        @Stable
        val Text: InputMode = InputMode(InputType.Text)

        @Stable
        val Number: InputMode = InputMode(InputType.Number)

        @Stable
        val Inches: InputMode = InputMode(InputType.Inches)

        @Stable
        val Price: InputMode = InputMode(InputType.Price)

        @Stable
        val Temperature: InputMode = InputMode(InputType.Temperature)

        @Stable
        val Humidity: InputMode = InputMode(InputType.Humidity)

    }
}