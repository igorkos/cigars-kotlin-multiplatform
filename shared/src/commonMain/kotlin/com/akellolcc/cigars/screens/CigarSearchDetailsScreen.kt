/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/13/24, 6:13 PM
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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import cafe.adriel.voyager.navigator.Navigator
import com.akellolcc.cigars.databases.models.Humidor
import com.akellolcc.cigars.mvvm.cigars.CigarsDetailsScreenViewModel
import com.akellolcc.cigars.screens.components.DialogButton
import com.akellolcc.cigars.screens.components.TextStyled
import com.akellolcc.cigars.screens.components.ValuePicker
import com.akellolcc.cigars.screens.components.ValuePickerItem
import com.akellolcc.cigars.screens.components.transformations.InputMode
import com.akellolcc.cigars.screens.navigation.NavRoute
import com.akellolcc.cigars.theme.Localize
import com.akellolcc.cigars.theme.MaterialColors
import com.akellolcc.cigars.theme.TextStyles
import com.akellolcc.cigars.theme.materialColor

class CigarSearchDetailsScreen(override val route: NavRoute) : CigarDetailsScreen(route) {

    override fun handleAction(
        event: Any,
        navigator: Navigator
    ) {
        when (event) {
            is CigarsDetailsScreenViewModel.CigarsDetailsAction.AddCigarToHumidor -> {
                viewModel.addCigar(event.humidor, event.count, event.price)
            }

            else -> super.handleAction(event, navigator)
        }
    }

    @Composable
    override fun dialogs() {
        AddCigarsDialog { viewModel.moveCigarDialog = false }
    }

    @Composable
    private fun AddCigarsDialog(onDismissRequest: () -> Unit) {
        if (viewModel.moveCigarDialog) {
            val count = remember { mutableStateOf(0L) }
            val price = remember { mutableStateOf(0f) }
            val to = remember { mutableStateOf<Humidor?>(null) }
            val toList = remember { mutableStateOf<List<ValuePickerItem>>(listOf()) }

            fun isValid(): Boolean {
                return count.value > 0 && price.value > 0 && to.value != null
            }

            LaunchedEffect(Unit) {
                toList.value = viewModel.moveToHumidors(null)
            }

            Dialog(onDismissRequest = { onDismissRequest() }) {
                Card(
                    modifier = Modifier.wrapContentSize(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = materialColor(MaterialColors.color_surfaceVariant),
                        contentColor = materialColor(MaterialColors.color_primary),
                    ),
                ) {
                    Column(
                        modifier = Modifier.wrapContentSize().padding(24.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        TextStyled(
                            Localize.cigar_search_details_add_dialog,
                            Localize.nav_header_title_desc,
                            TextStyles.Headline,
                            labelStyle = TextStyles.None,
                        )
                        Column(
                            modifier = Modifier.wrapContentSize().padding(top = 24.dp),
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            ValuePicker(
                                modifier = Modifier.fillMaxWidth(),
                                backgroundColor = MaterialColors.color_outline,
                                value = ValuePickerItem(
                                    null,
                                    Localize.cigar_details_move_select,
                                    null
                                ),
                                label = Localize.cigar_details_move_to,
                                items = toList.value,
                                onClick = {
                                    to.value = it.value as Humidor?
                                }
                            )
                        }
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            TextStyled(
                                if (count.value > 0) "${count.value}" else "",
                                Localize.cigar_search_details_add_count_dialog,
                                TextStyles.Headline,
                                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                                editable = true,
                                inputMode = InputMode.Number,
                                center = true,
                                maxLines = 1,
                                onValueChange = {
                                    count.value = if (it.isNotBlank()) it.toLong() else 0
                                }
                            )
                            TextStyled(
                                if (price.value > 0) "${price.value}" else "",
                                Localize.cigar_search_details_add_price_dialog,
                                TextStyles.Headline,
                                modifier = Modifier.fillMaxWidth(),
                                editable = true,
                                inputMode = InputMode.Price,
                                maxLines = 1,
                                center = true,
                                prefix = "$",
                                onValueChange = {
                                    price.value = if (it.isNotBlank()) it.toFloat() else 0f
                                }
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
                            horizontalArrangement = Arrangement.End,
                        ) {
                            DialogButton(
                                title = Localize.button_cancel,
                                onClick = { onDismissRequest() })
                            DialogButton(
                                enabled = isValid(),
                                title = Localize.button_add,
                                onClick = {
                                    viewModel.sendEvent(
                                        CigarsDetailsScreenViewModel.CigarsDetailsAction.AddCigarToHumidor(
                                            to.value!!,
                                            count.value,
                                            price.value.toDouble()
                                        )
                                    )
                                })
                        }
                    }
                }
            }
        }
    }
}