/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/5/24, 12:01 PM
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

package com.akellolcc.cigars.theme

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

enum class TextStyles {
    ScreenTitle,
    BarItemTitle,
    Headline,
    Subhead,
    Description,
    ErrorTitle,
    Error,
    None
}

@Composable
fun textStyle(style: TextStyles, color: Color = Color(LocalContentColor.current.value)): TextStyle {
    return when (style) {
        TextStyles.Headline -> MaterialTheme.typography.titleLarge.copy(color = color)
        TextStyles.Subhead -> MaterialTheme.typography.titleMedium.copy(color = color)
        TextStyles.Description -> MaterialTheme.typography.titleSmall.copy(color = color)
        TextStyles.ScreenTitle -> MaterialTheme.typography.titleLarge.copy(color = color)
        TextStyles.BarItemTitle -> MaterialTheme.typography.labelMedium.copy(color = color)
        TextStyles.Error -> MaterialTheme.typography.titleMedium.copy(color = materialColor(MaterialColors.color_error))
        TextStyles.ErrorTitle -> MaterialTheme.typography.titleLarge.copy(color = materialColor(MaterialColors.color_error))
        TextStyles.None -> MaterialTheme.typography.titleSmall.copy(color = color)
    }
}
