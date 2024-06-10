/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/7/24, 2:12 PM
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

package com.akellolcc.cigars.screens.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.akellolcc.cigars.theme.Localize
import com.akellolcc.cigars.theme.MaterialColors
import com.akellolcc.cigars.theme.TextStyles
import com.akellolcc.cigars.theme.materialColor

@Composable
fun TwoButtonDialog(
    title: String,
    onDismissRequest: () -> Unit,
    onVerify: (() -> Boolean)? = null,
    onComplete: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier.wrapContentSize().semantics {
                contentDescription = title
            },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = materialColor(MaterialColors.color_surfaceVariant),
                contentColor = materialColor(MaterialColors.color_primary),
            ),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(24.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                TextStyled(
                    title,
                    Localize.nav_header_title_desc,
                    TextStyles.Headline,
                    labelStyle = TextStyles.None,
                    modifier = Modifier.semantics { heading() }
                )
            }
            Box(
                modifier = Modifier.wrapContentSize().padding(horizontal = 24.dp),
            ) {
                content()
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(24.dp),
                horizontalArrangement = Arrangement.End,
            ) {
                DialogButton(
                    title = Localize.button_cancel,
                    onClick = { onDismissRequest() })
                DialogButton(
                    enabled = onVerify?.invoke() ?: true,
                    title = Localize.button_save,
                    onClick = {
                        onComplete?.invoke()
                        onDismissRequest()
                    })
            }
        }
    }
}