/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/24/24, 11:13 AM
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

import androidx.compose.ui.text.input.VisualTransformation
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
inline fun VisualTransformation?.nullOrTransform(): ExtendedVisualTransformation? {
    contract {
        returns(null) implies (this@nullOrTransform != null)
    }

    return if (this != null && this is ExtendedVisualTransformation) this else null
}

abstract class ExtendedVisualTransformation : VisualTransformation {
    abstract fun fromTransformed(text: String, tag: String? = null): String
    abstract fun validate(text: String, tag: String? = null): Boolean
    abstract fun toTransformed(text: String, tag: String? = null): String

}