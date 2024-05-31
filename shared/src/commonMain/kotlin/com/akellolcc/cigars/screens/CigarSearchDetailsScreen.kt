/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/31/24, 11:16 AM
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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults.centerAlignedTopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import cafe.adriel.voyager.navigator.Navigator
import com.akellolcc.cigars.databases.models.Humidor
import com.akellolcc.cigars.mvvm.cigars.CigarsDetailsScreenViewModel
import com.akellolcc.cigars.screens.components.BackButton
import com.akellolcc.cigars.screens.components.DefaultButton
import com.akellolcc.cigars.screens.components.DialogButton
import com.akellolcc.cigars.screens.components.TextStyled
import com.akellolcc.cigars.screens.components.ValuePicker
import com.akellolcc.cigars.screens.components.ValuePickerItem
import com.akellolcc.cigars.screens.components.transformations.InputMode
import com.akellolcc.cigars.screens.navigation.NavRoute
import com.akellolcc.cigars.theme.Images
import com.akellolcc.cigars.theme.Localize
import com.akellolcc.cigars.theme.MaterialColors
import com.akellolcc.cigars.theme.TextStyles
import com.akellolcc.cigars.theme.loadIcon
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

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun topTabBar() {
        val topColors = centerAlignedTopAppBarColors(
            containerColor = materialColor(MaterialColors.color_transparent),
            navigationIconContentColor = materialColor(MaterialColors.color_onPrimaryContainer),
            actionIconContentColor = materialColor(MaterialColors.color_onPrimaryContainer),
        )

        CenterAlignedTopAppBar(
            colors = topColors,
            navigationIcon = {
                if (!viewModel.editing) {
                    BackButton {
                        viewModel.onBackPress()
                    }
                }
            },
            actions = {
                if (!viewModel.editing) {
                    IconButton(onClick = { viewModel.editing = true }) {
                        loadIcon(Images.icon_menu_plus, Size(24.0F, 24.0F))
                    }
                }
            },
            title = {})
    }

    @Composable
    override fun dialogs() {
        AddCigarsDialog { viewModel.moveCigarDialog = false }
    }

    @Composable
    override fun bottomTabBar() {
        if (viewModel.editing) {
            BottomAppBar(
                actions = {
                    Box(modifier = Modifier.weight(1f))
                    DefaultButton(
                        title = Localize.button_cancel,
                        modifier = Modifier.padding(end = 10.dp).weight(2f),
                        onClick = { viewModel.onCancelEdit() })
                    DefaultButton(
                        modifier = Modifier.weight(2f),
                        enabled = viewModel.verifyFields(),
                        title = Localize.button_add,
                        onClick = { viewModel.moveCigarDialog = true })
                }
            )
        }
    }

    @Composable
    private fun AddCigarsDialog(onDismissRequest: () -> Unit) {

        if (viewModel.moveCigarDialog) {
            val count = remember { mutableStateOf(0L) }
            val price = remember { mutableStateOf(0f) }
            val to = remember { mutableStateOf<Humidor?>(null) }
            val toList = remember { mutableStateOf<List<ValuePickerItem<Humidor>>>(listOf()) }

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
                            TextStyles.Headline,
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
                                    to.value = it.value
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
                                TextStyles.Headline,
                                label = Localize.cigar_search_details_add_count_dialog,
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
                                TextStyles.Headline,
                                label = Localize.cigar_search_details_add_price_dialog,
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