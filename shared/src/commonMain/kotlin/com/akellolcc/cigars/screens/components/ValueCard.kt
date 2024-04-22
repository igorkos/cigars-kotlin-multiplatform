/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/19/24, 11:45 PM
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
 */

package com.akellolcc.cigars.screens.components

import TextStyled
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.dp
import com.akellolcc.cigars.theme.MaterialColors
import com.akellolcc.cigars.theme.TextStyles
import com.akellolcc.cigars.theme.loadIcon
import com.akellolcc.cigars.theme.materialColor
import dev.icerock.moko.resources.ImageResource

@Composable
fun ValueCard(label: String?, value: String?, onClick: (() -> Unit)? = null) {
    OutlinedCard(
        colors = CardDefaults.cardColors(
            materialColor(MaterialColors.color_primaryContainer),
            materialColor(MaterialColors.color_onPrimaryContainer)
        ),
        shape = RoundedCornerShape(5.dp),
        border = BorderStroke(
            if (onClick != null) 1.dp else 0.5.dp,
            materialColor(MaterialColors.color_onPrimaryContainer)
        ),
        elevation = if (onClick != null) CardDefaults.outlinedCardElevation(8.dp) else CardDefaults.outlinedCardElevation()
    ) {
        Column(
            modifier = Modifier.padding(8.dp).clickable(onClick = {
                onClick?.invoke()
            }),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (label != null) {
                TextStyled(
                    label,
                    TextStyles.Subhead,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
            TextStyled(
                value,
                TextStyles.Subhead,
            )
        }
    }
}

@Composable
fun ValuesCard(
    label: String? = null,
    vertical: Boolean = false,
    actionIcon: ImageResource? = null,
    onAction: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Box {
        OutlinedCard(
            colors = CardDefaults.cardColors(
                materialColor(MaterialColors.color_transparent),
                materialColor(MaterialColors.color_onPrimaryContainer)
            ),
            border = BorderStroke(0.5.dp, materialColor(MaterialColors.color_onPrimaryContainer)),
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp, bottom = 4.dp),
        ) {
            if (vertical) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    content()
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    content()
                }
            }
        }
        if (label != null) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth().padding(start = 24.dp, end = 16.dp)
                    .background(materialColor(MaterialColors.color_transparent))
            ) {
                TextStyled(
                    label,
                    TextStyles.Subhead,
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
    }
}
