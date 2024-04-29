/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/27/24, 2:21 PM
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

package com.akellolcc.cigars.screens

import TextStyled
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import com.akellolcc.cigars.databases.extensions.History
import com.akellolcc.cigars.databases.extensions.HistoryType
import com.akellolcc.cigars.mvvm.HistoryScreenViewModel
import com.akellolcc.cigars.screens.navigation.NavRoute
import com.akellolcc.cigars.theme.Localize
import com.akellolcc.cigars.theme.MaterialColors
import com.akellolcc.cigars.theme.TextStyles
import com.akellolcc.cigars.theme.loadIcon
import com.akellolcc.cigars.theme.materialColor
import com.akellolcc.cigars.utils.formatDate

abstract class HistoryScreen<VM : HistoryScreenViewModel>(override val route: NavRoute) :
    BaseTabListScreen<HistoryScreenViewModel.CigarsAction, History, VM>(route) {

    override fun handleAction(
        event: HistoryScreenViewModel.CigarsAction,
        navigator: Navigator?
    ) {
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun topTabBar(scrollBehavior: TopAppBarScrollBehavior?, navigator: Navigator?) {
        val topColors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = materialColor(MaterialColors.color_transparent),
            navigationIconContentColor = materialColor(MaterialColors.color_onPrimaryContainer),
            actionIconContentColor = materialColor(MaterialColors.color_onPrimaryContainer),
        )
        TopAppBar(
            title = {
                TextStyled(
                    viewModel.name,
                    TextStyles.ScreenTitle,
                )
            },
            colors = topColors,
            navigationIcon = {
                IconButton(onClick = {
                    navigator?.pop()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            })
    }

    @Composable
    override fun EntityListRow(entity: History, modifier: Modifier) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = materialColor(MaterialColors.color_primaryContainer),
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                loadIcon(HistoryType.icon(entity.type), Size(32.dp.value, 32.dp.value))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp)

                ) {
                    Column {
                        TextStyled(
                            text = entity.date.formatDate(),
                            style = TextStyles.Subhead,
                            keepHeight = true
                        )
                        TextStyled(
                            text = entity.cigar?.name ?: entity.humidorFrom.name,
                            style = TextStyles.Subhead,
                            keepHeight = true
                        )
                        Row(
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            TextStyled(
                                text = if (entity.cigar == null) Localize.history_humidor_added(entity.type) else Localize.history_transaction_desc(
                                    entity.type,
                                    entity.count
                                ),
                                style = TextStyles.Subhead
                            )
                            if (entity.price != null) {
                                TextStyled(
                                    text = Localize.history_transaction_price(entity.price!!),
                                    style = TextStyles.Subhead
                                )
                            }
                        }
                        if (entity.type == HistoryType.Move) {
                            Column {
                                TextStyled(
                                    label = "From",
                                    text = entity.humidorFrom.name,
                                    style = TextStyles.Subhead
                                )
                                TextStyled(
                                    label = "To",
                                    text = entity.humidorTo.name,
                                    style = TextStyles.Subhead
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}
