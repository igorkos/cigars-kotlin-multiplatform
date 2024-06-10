/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/9/24, 1:35 PM
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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.akellolcc.cigars.theme.MaterialColors
import com.akellolcc.cigars.theme.TextStyles
import com.akellolcc.cigars.theme.loadIcon
import com.akellolcc.cigars.theme.materialColor
import dev.icerock.moko.resources.ImageResource

@Composable
internal fun contentLayout(modifier: Modifier, vertical: Boolean, content: @Composable () -> Unit) {
    if (vertical) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.Start
        ) {
            content()
        }
    } else {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            content()
        }
    }
}

@Composable
internal fun decorationLayout(label: String, actionIcon: ImageResource? = null, onAction: (() -> Unit)? = null) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth().padding(start = 24.dp, end = 16.dp)
            .background(materialColor(MaterialColors.color_transparent))
            .semantics(mergeDescendants = true) {
                heading()
            }
    ) {
        TextStyled(
            label,
            label,
            TextStyles.Subhead,
            labelStyle = TextStyles.None,
            modifier = Modifier.padding(start = 8.dp, end = 8.dp)
                .background(materialColor(MaterialColors.color_background))
        )
        if (actionIcon != null) {
            Box(contentAlignment = Alignment.Center) {
                Spacer(
                    modifier = Modifier
                        .width(28.dp)
                        .height(8.dp)
                        .background(materialColor(MaterialColors.color_background))
                )
                IconButton(
                    modifier = Modifier.size(24.dp)
                        .background(materialColor(MaterialColors.color_transparent)),
                    onClick = { onAction?.invoke() }) {
                    loadIcon(
                        actionIcon,
                        Size(24f, 24f),
                        modifier = Modifier.background(materialColor(MaterialColors.color_transparent))
                    )
                }
            }
        }
    }
}

@Composable
fun ValuesCard(
    modifier: Modifier = Modifier,
    label: String? = null,
    vertical: Boolean = false,
    border: Boolean = true,
    actionIcon: ImageResource? = null,
    onAction: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier.semantics {
        stateDescription = "$vertical"
    }) {
        if (border) {
            OutlinedCard(
                colors = CardDefaults.cardColors(
                    materialColor(MaterialColors.color_transparent),
                    materialColor(MaterialColors.color_onPrimaryContainer)
                ),
                border = BorderStroke(0.5.dp, materialColor(MaterialColors.color_onPrimaryContainer)),
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp, bottom = 4.dp),
            ) {
                contentLayout(Modifier.fillMaxWidth().padding(top = 20.dp, bottom = 16.dp, start = 16.dp, end = 16.dp), vertical, content)
            }
            if (label != null) {
                decorationLayout(label, actionIcon, onAction)
            }
        } else {
            contentLayout(Modifier, vertical, content)
        }
    }
}
