/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/15/24, 12:33 PM
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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import com.akellolcc.cigars.mvvm.base.createViewModel
import com.akellolcc.cigars.mvvm.humidor.HumidorDetailsScreenViewModel
import com.akellolcc.cigars.screens.base.BaseItemDetailsScreen
import com.akellolcc.cigars.screens.components.TextStyled
import com.akellolcc.cigars.screens.components.ValueCard
import com.akellolcc.cigars.screens.components.ValuesCard
import com.akellolcc.cigars.screens.components.transformations.InputMode
import com.akellolcc.cigars.screens.navigation.NavRoute
import com.akellolcc.cigars.theme.Localize
import com.akellolcc.cigars.theme.TextStyles

class HumidorDetailsScreen(route: NavRoute) : BaseItemDetailsScreen<HumidorDetailsScreenViewModel>(route) {
    @Composable
    override fun Content() {
        viewModel = rememberScreenModel { createViewModel(HumidorDetailsScreenViewModel::class, route.data) }
        super.Content()
    }

    @Composable
    override fun details() {
        humidorOrigin()
        humidorCapacity()
        humidorParameters()
        humidorNotes()
    }

    @Composable
    private fun humidorOrigin() {
        //Humidor Name Brand
        ValuesCard(
            modifier = Modifier.semantics { contentDescription = Localize.humidor_details_origin_block_desc },
            vertical = true,
            border = false
        ) {
            TextStyled(
                viewModel.name,
                Localize.cigar_details_name,
                TextStyles.Headline,
                labelStyle = if (viewModel.editing) TextStyles.Description else TextStyles.None,
                editable = viewModel.editing,
                maxLines = 2,
                modifier = Modifier.padding(bottom = 4.dp),
                onValueChange = {
                    viewModel.name = it
                }
            )
            TextStyled(
                viewModel.brand,
                label = Localize.cigar_details_company,
                TextStyles.Subhead,
                labelStyle = if (viewModel.editing) TextStyles.Description else TextStyles.None,
                editable = viewModel.editing,
                modifier = Modifier.padding(bottom = 4.dp),
                onValueChange = {
                    viewModel.brand = it
                }
            )
        }
    }

    @Composable
    private fun humidorCapacity() {
        ValuesCard(
            modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
                .semantics { contentDescription = Localize.humidor_details_size_block_desc },
            label = Localize.humidor_details_cigars,
            vertical = viewModel.editing
        ) {
            ValueCard(
                Localize.humidor_details_holds,
                viewModel.holds.toString(),
                viewModel.editing,
                inputMode = InputMode.Number,
                onValueChange = {
                    viewModel.holds =
                        if (it.isNotBlank()) it.toLong() else 0
                })
            ValueCard(
                Localize.humidor_details_count,
                viewModel.count.toString(),
                viewModel.editing,
                inputMode = InputMode.Number,
                onValueChange = {
                    viewModel.count =
                        if (it.isNotBlank()) it.toLong() else 0
                })
        }
    }

    @Composable
    private fun humidorParameters() {
        //Humidor Parameters
        ValuesCard(
            modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
                .semantics { contentDescription = Localize.humidor_details_params_block_desc },
            label = Localize.humidor_details_humidor,
            vertical = viewModel.editing
        ) {
            ValueCard(
                Localize.humidor_details_temperature,
                viewModel.temperature?.toString() ?: "72",
                viewModel.editing,
                inputMode = InputMode.Number,
                onValueChange = {
                    viewModel.temperature =
                        if (it.isNotBlank()) it.toLong() else null
                })
            ValueCard(
                Localize.humidor_details_humidity,
                viewModel.humidity?.toString() ?: "72",
                viewModel.editing,
                inputMode = InputMode.Number,
                onValueChange = {
                    viewModel.humidity =
                        if (it.isNotBlank()) it.toDouble() else null
                })
        }
    }

    @Composable
    private fun humidorNotes() {
        var notesHeight by remember { mutableStateOf(0) }
        if (viewModel.editing || viewModel.notes != null || viewModel.notes != null) {
            Column(
                modifier = Modifier
                    .padding(bottom = 10.dp, top = 10.dp)
                    .onSizeChanged {
                        notesHeight = it.height
                    }.semantics {
                        contentDescription = Localize.humidor_details_notes_block_desc
                    }
            ) {
                if (viewModel.link != null || viewModel.editing) {
                    TextStyled(
                        viewModel.notes,
                        Localize.cigar_details_notes,
                        TextStyles.Subhead,
                        labelStyle = TextStyles.None,
                        modifier = Modifier.fillMaxWidth(),
                        editable = viewModel.editing,
                        onValueChange = {
                            viewModel.notes = it
                        }
                    )
                }
                if (viewModel.link != null || viewModel.editing) {
                    TextStyled(
                        viewModel.link,
                        Localize.cigar_details_link,
                        TextStyles.Subhead,
                        labelStyle = TextStyles.Subhead,
                        editable = viewModel.editing,
                        maxLines = 1,
                        modifier = Modifier.fillMaxWidth(),
                        onValueChange = {
                            viewModel.link = it
                        }
                    )
                }
            }
        }
    }
}