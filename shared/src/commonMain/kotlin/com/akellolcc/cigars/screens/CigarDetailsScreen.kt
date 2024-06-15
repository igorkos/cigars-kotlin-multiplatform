/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/14/24, 8:03 PM
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

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.navigator.Navigator
import com.akellolcc.cigars.databases.models.Cigar
import com.akellolcc.cigars.databases.models.CigarShapes
import com.akellolcc.cigars.databases.models.CigarStrength
import com.akellolcc.cigars.databases.models.Humidor
import com.akellolcc.cigars.databases.models.HumidorCigar
import com.akellolcc.cigars.databases.models.emptyCigar
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.mvvm.base.BaseDetailsScreenViewModel
import com.akellolcc.cigars.mvvm.base.createViewModel
import com.akellolcc.cigars.mvvm.cigars.CigarsDetailsScreenViewModel
import com.akellolcc.cigars.screens.base.BaseItemDetailsScreen
import com.akellolcc.cigars.screens.components.InfoImageDialog
import com.akellolcc.cigars.screens.components.TextStyled
import com.akellolcc.cigars.screens.components.TwoButtonDialog
import com.akellolcc.cigars.screens.components.ValueCard
import com.akellolcc.cigars.screens.components.ValuePicker
import com.akellolcc.cigars.screens.components.ValuePickerItem
import com.akellolcc.cigars.screens.components.ValuesCard
import com.akellolcc.cigars.screens.components.transformations.InputMode
import com.akellolcc.cigars.screens.navigation.CigarHistoryRoute
import com.akellolcc.cigars.screens.navigation.CigarImagesViewRoute
import com.akellolcc.cigars.screens.navigation.HumidorCigarsRoute
import com.akellolcc.cigars.screens.navigation.NavRoute
import com.akellolcc.cigars.theme.Images
import com.akellolcc.cigars.theme.Localize
import com.akellolcc.cigars.theme.MaterialColors
import com.akellolcc.cigars.theme.TextStyles
import com.akellolcc.cigars.theme.loadIcon
import com.akellolcc.cigars.theme.materialColor

open class CigarDetailsScreen(route: NavRoute) : BaseItemDetailsScreen<CigarsDetailsScreenViewModel>(route) {

    @Composable
    override fun Content() {
        viewModel = rememberScreenModel {
            createViewModel(
                CigarsDetailsScreenViewModel::class, when (route.data) {
                    null -> emptyCigar
                    is Cigar -> route.data as Cigar
                    is Humidor -> emptyCigar
                    else -> {}
                }
            ).apply {
                if (route.data is Humidor) {
                    humidor = route.data as Humidor
                }
            }

        }
        super.Content()
    }

    @Composable
    override fun details() {
        cigarOrigin()
        //Cigar Shape Length and Gauge
        cigarSize()
        //Cigar tobacco
        cigarTobacco()
        //Cigar ratings
        cigarRatings()
        //Cigar humidors
        cigarHumidors()
        //Cigar Notes and Link
        cigarNotes()
    }

    override fun handleAction(
        event: Any,
        navigator: Navigator
    ) {
        when (event) {
            is CigarsDetailsScreenViewModel.CigarsDetailsAction.AddToHumidor -> viewModel.humidorCigarsCount = event.humidor

            is CigarsDetailsScreenViewModel.CigarsDetailsAction.OpenHumidor -> navigator.push(
                HumidorCigarsScreen(HumidorCigarsRoute.applyData(event.humidor))
            )

            is BaseDetailsScreenViewModel.DetailsAction.OpenHistory -> navigator.push(
                CigarHistoryScreen(
                    CigarHistoryRoute.applyData(event.entity)
                )
            )

            is CigarsDetailsScreenViewModel.CigarsDetailsAction.MoveCigar -> viewModel.moveCigarDialog =
                true

            is BaseDetailsScreenViewModel.DetailsAction.ShowImages -> {
                val data = route.data as Cigar
                navigator.push(ImagesViewScreen(CigarImagesViewRoute.applyData(Pair(data, event.selected))))
            }

            is CigarsDetailsScreenViewModel.CigarsDetailsAction.AddCigarToHumidor -> TODO()
            else -> super.handleAction(event, navigator)
        }
    }

    @Composable
    override fun topTabBarActions() {
        IconButton(
            modifier = Modifier.semantics { contentDescription = Localize.cigar_details_top_bar_history_desc },
            onClick = { viewModel.historyOpen() }) {
            loadIcon(Images.icon_menu_history, Size(24.0F, 24.0F))
        }
        super.topTabBarActions()
    }

    @Composable
    override fun dialogs() {
        HumidorCigarsCountDialog {
            viewModel.humidorCigarsCount = null
        }
        CigarsRatingDialog {
            viewModel.cigarRating = false
        }
        MoveCigarsDialog {
            viewModel.moveCigarDialog = false
        }
        if (viewModel.infoDialog != CigarsDetailsScreenViewModel.InfoActions.None) {
            InfoImageDialog(
                when (viewModel.infoDialog) {
                    CigarsDetailsScreenViewModel.InfoActions.CigarSize -> Images.cigar_sizes_info
                    CigarsDetailsScreenViewModel.InfoActions.CigarTobacco -> Images.cigar_tobacco_info
                    CigarsDetailsScreenViewModel.InfoActions.CigarRatings -> Images.cigar_ratings_info
                    else -> TODO()
                },
                modifier = Modifier.semantics {
                    contentDescription = when (viewModel.infoDialog) {
                        CigarsDetailsScreenViewModel.InfoActions.CigarSize -> Localize.cigar_details_size_info_dialog_desc
                        CigarsDetailsScreenViewModel.InfoActions.CigarTobacco -> Localize.cigar_details_tobacco_info_dialog_desc
                        CigarsDetailsScreenViewModel.InfoActions.CigarRatings -> Localize.cigar_details_ratings_info_dialog_desc
                        else -> ""
                    }
                }
            ) {
                viewModel.infoDialog = CigarsDetailsScreenViewModel.InfoActions.None
            }
        }
    }

    @Composable
    private fun cigarOrigin() {
        //Cigar Name,  Company and Country
        ValuesCard(
            modifier = Modifier.semantics { contentDescription = Localize.cigar_details_origin_block_desc },
            vertical = true,
            border = false
        ) {
            TextStyled(
                viewModel.name,
                label = Localize.cigar_details_name,
                style = TextStyles.Headline,
                labelStyle = if (viewModel.editing) TextStyles.Description else TextStyles.None,
                editable = viewModel.editing,
                maxLines = 2,
                modifier = Modifier.padding(bottom = 4.dp).fillMaxWidth(),
                onValueChange = {
                    viewModel.name = it
                }
            )
            TextStyled(
                viewModel.brand,
                Localize.cigar_details_company,
                TextStyles.Subhead,
                labelStyle = if (viewModel.editing) TextStyles.Description else TextStyles.None,
                editable = viewModel.editing,
                modifier = Modifier.padding(bottom = 4.dp).fillMaxWidth(),
                onValueChange = {
                    viewModel.brand = it
                }
            )
            TextStyled(
                viewModel.country,
                Localize.cigar_details_country,
                TextStyles.Subhead,
                labelStyle = if (viewModel.editing) TextStyles.Description else TextStyles.None,
                editable = viewModel.editing,
                modifier = Modifier.padding(bottom = 4.dp).fillMaxWidth(),
                onValueChange = {
                    viewModel.country = it
                })
            if (viewModel.editing && viewModel.humidor != null) {
                TextStyled(
                    viewModel.count.toString(),
                    Localize.cigar_search_details_add_count_dialog,
                    TextStyles.Subhead,
                    labelStyle = TextStyles.Description,
                    inputMode = InputMode.Number,
                    editable = viewModel.editing,
                    modifier = Modifier.padding(bottom = 4.dp).fillMaxWidth(),
                    onValueChange = {
                        viewModel.count = it.toLong()
                    })
                TextStyled(
                    viewModel.price.toString(),
                    Localize.cigar_details_count_dialog_price,
                    TextStyles.Subhead,
                    labelStyle = TextStyles.Description,
                    editable = viewModel.editing,
                    inputMode = InputMode.Price,
                    modifier = Modifier.padding(bottom = 4.dp).fillMaxWidth(),
                    onValueChange = {
                        viewModel.price = it.toDouble()
                    })

            }
        }
    }

    @Composable
    private fun cigarSize() {
        if (!viewModel.editing && viewModel.shape.isBlank() && viewModel.length.isBlank() && viewModel.gauge == 0L) return
        ValuesCard(
            modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
                .semantics { contentDescription = Localize.cigar_details_size_block_desc },
            label = Localize.cigar_details_cigars,
            vertical = viewModel.editing,
            actionIcon = Images.icon_menu_info,
            onAction = {
                viewModel.infoDialog = CigarsDetailsScreenViewModel.InfoActions.CigarSize
            }
        ) {
            ValueCard(
                Localize.cigar_details_shape,
                if (viewModel.editing) {
                    ValuePickerItem(
                        CigarShapes.fromString(viewModel.shape),
                        viewModel.shape,
                        null
                    )
                } else {
                    viewModel.shape
                },
                viewModel.editing,
                items = CigarShapes.enumValues().map {
                    ValuePickerItem(it.first, it.second, Images.tab_icon_cigars)
                },
                modifier = Modifier.padding(bottom = 10.dp),
                onClick = { value ->
                    value?.let {
                        viewModel.shape = (it.value as CigarShapes).name
                    }
                })

            ValueCard(
                Localize.cigar_details_length,
                viewModel.length,
                viewModel.editing,
                modifier = Modifier.padding(bottom = 10.dp),
                inputMode = InputMode.Inches,
                onValueChange = {
                    viewModel.length = it
                })

            ValueCard(
                Localize.cigar_details_gauge,
                viewModel.gauge.toString(),
                viewModel.editing,
                inputMode = InputMode.Number,
                onValueChange = {
                    viewModel.gauge =
                        if (it.isNotBlank()) it.toLong() else 0
                })

        }
    }

    @Composable
    private fun cigarTobacco() {
        if (!viewModel.editing && viewModel.wrapper.isBlank() && viewModel.binder.isBlank() && viewModel.filler.isBlank() && viewModel.strength == CigarStrength.Unknown) return
        ValuesCard(
            modifier = Modifier.semantics { contentDescription = Localize.cigar_details_tobacco_block_desc },
            label = Localize.cigar_details_tobacco,
            vertical = true,
            actionIcon = Images.icon_menu_info,
            onAction = {
                viewModel.infoDialog = CigarsDetailsScreenViewModel.InfoActions.CigarTobacco
            }
        ) {
            if (viewModel.editing || viewModel.wrapper.isNotBlank()) {
                TextStyled(
                    viewModel.wrapper,
                    Localize.cigar_details_wrapper,
                    TextStyles.Subhead,
                    labelStyle = TextStyles.Subhead,
                    editable = viewModel.editing,
                    modifier = Modifier.padding(bottom = 10.dp).fillMaxWidth(),
                    onValueChange = {
                        viewModel.wrapper = it
                    }
                )
            }
            if (viewModel.editing || viewModel.binder.isNotBlank()) {
                TextStyled(
                    viewModel.binder,
                    Localize.cigar_details_binder,
                    TextStyles.Subhead,
                    labelStyle = TextStyles.Subhead,
                    editable = viewModel.editing,
                    modifier = Modifier.padding(bottom = 10.dp).fillMaxWidth(),
                    onValueChange = {
                        viewModel.binder = it
                    }
                )
            }
            if (viewModel.editing || viewModel.filler.isNotBlank()) {
                TextStyled(
                    viewModel.filler,
                    Localize.cigar_details_filler,
                    TextStyles.Subhead,
                    labelStyle = TextStyles.Subhead,
                    editable = viewModel.editing,
                    modifier = Modifier.padding(bottom = 10.dp).fillMaxWidth(),
                    onValueChange = {
                        viewModel.filler = it
                    }
                )
            }
            if (viewModel.editing || viewModel.strength != CigarStrength.Unknown) {
                ValueCard(
                    Localize.cigar_details_strength,
                    if (viewModel.editing) {
                        ValuePickerItem(
                            viewModel.strength,
                            CigarStrength.localized(viewModel.strength),
                            null
                        )
                    } else {
                        CigarStrength.localized(viewModel.strength)
                    },
                    viewModel.editing,
                    vertical = false,
                    items = CigarStrength.enumValues()
                        .map { ValuePickerItem(it.first, it.second, null) },
                    onClick = {
                        viewModel.strength = (it?.value as CigarStrength?)!!
                    })

            }
        }
    }

    @Composable
    private fun cigarRatings() {
        if (!viewModel.editing && viewModel.rating == null && viewModel.myRating == null) return
        ValuesCard(
            modifier = Modifier.semantics { contentDescription = Localize.cigar_details_ratings_block_desc },
            label = Localize.cigar_details_ratings,
            vertical = viewModel.editing,
            actionIcon = Images.icon_menu_info,
            onAction = {
                viewModel.infoDialog = CigarsDetailsScreenViewModel.InfoActions.CigarRatings
            }
        ) {
            ValueCard(
                Localize.cigar_details_rating,
                "${viewModel.rating}",
                viewModel.editing,
                modifier = Modifier.padding(bottom = 10.dp),
                inputMode = InputMode.Number,
                onValueChange = {
                    viewModel.rating =
                        if (it.isNotBlank()) it.toLong() else 0
                })

            ValueCard(
                Localize.cigar_details_myrating,
                "${viewModel.myRating}",
                viewModel.editing,
                modifier = Modifier.padding(bottom = 10.dp),
                inputMode = InputMode.Number,
                onClick = {
                    viewModel.cigarRating = true
                },
                onValueChange = {
                    viewModel.myRating =
                        if (it.isNotBlank()) it.toLong() else 0
                })

            if (!viewModel.editing) {
                IconButton(
                    modifier = Modifier.semantics {
                        contentDescription =
                            if (viewModel.favorites) Localize.cigar_details_ratings_block_favorite_remove_desc else Localize.cigar_details_ratings_block_favorite_add_desc
                    },
                    onClick = {
                        viewModel.favorite()
                    }) {
                    val icon = if (viewModel.favorites) Images.icon_star_filled else Images.icon_star_empty
                    loadIcon(icon, Size(64.0F, 64.0F))
                }
            }
        }
    }

    @Composable
    private fun cigarHumidors() {
        if (!viewModel.editing && viewModel.humidors.isNotEmpty()) {
            Log.debug("Cigar Humidors:  ${viewModel.humidors.map { it.humidor.name }} ")
            ValuesCard(
                modifier = Modifier.height(((viewModel.humidors.count() + 2) * 48).dp)
                    .semantics { contentDescription = Localize.cigar_details_humidors_block_desc },
                label = Localize.cigar_details_humidors,
                vertical = true,
                actionIcon = if (viewModel.humidorsCount() > 1) Images.icon_tab else null,
                onAction = {
                    viewModel.moveCigar()
                }
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier.fillMaxSize().semantics {
                        contentDescription = Localize.cigar_details_humidors_block_list_desc
                    }
                ) {
                    item {
                        TextStyled(
                            Localize.cigar_details_total(viewModel.count),
                            label = Localize.cigar_details_total_desc,
                            TextStyles.Subhead,
                            labelStyle = TextStyles.None,
                            modifier = Modifier.padding(bottom = 10.dp),
                        )
                    }
                    items(viewModel.humidors, key = { item -> item.key }, contentType = { item -> item.humidor.name }) {
                        HumidorListRow(it)
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                                .background(Color.Transparent)
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun HumidorListRow(item: HumidorCigar) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = materialColor(MaterialColors.color_transparent),
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = {
                    viewModel.openHumidor(item.humidor)
                })
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp, start = 12.dp, end = 12.dp, bottom = 6.dp)

            ) {
                TextStyled(
                    item.humidor.name,
                    Localize.humidor_details_humidor,
                    TextStyles.Subhead,
                    labelStyle = TextStyles.None,
                )
                ValueCard(null, "${item.count}") {
                    viewModel.addToHumidor(item)
                }
            }
        }
    }

    @Composable
    private fun cigarNotes() {
        var notesHeight by remember { mutableStateOf(0) }
        if (viewModel.editing || viewModel.notes != null) {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(bottom = 10.dp, top = 10.dp)
                    .onSizeChanged {
                        notesHeight = it.height
                    }.semantics {
                        contentDescription = Localize.cigar_details_notes_block_desc
                    }
            ) {
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
                if (viewModel.editing || viewModel.link != null) {
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

    @Composable
    private fun HumidorCigarsCountDialog(onDismissRequest: () -> Unit) {
        val count = remember { mutableStateOf("0") }
        val price = remember { mutableStateOf("") }
        val showPrice = remember { mutableStateOf(false) }

        LaunchedEffect(viewModel.humidorCigarsCount) {
            count.value = (viewModel.humidorCigarsCount?.count ?: 0).toString()
        }

        if (viewModel.humidorCigarsCount != null) {
            TwoButtonDialog(
                Localize.cigar_details_count_dialog,
                onVerify = { viewModel.verifyFields() },
                onDismissRequest = { onDismissRequest() },
                onComplete = {
                    viewModel.updateCigarCount(
                        viewModel.humidorCigarsCount!!,
                        count.value.toLong(),
                        if (price.value.isNotBlank()) price.value.toDouble() else null
                    )
                }) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        IconButton(
                            modifier = Modifier.wrapContentSize().semantics {
                                contentDescription = Localize.cigar_details_humidors_count_dialog_minus_desc
                            },
                            onClick = {
                                val current = (count.value.ifBlank { "0" }).toInt()
                                count.value = if (current > 0) current.dec().toString() else "0"
                                if (viewModel.humidorCigarsCount!!.count >= count.value.toLong()) {
                                    showPrice.value = false
                                }
                            }) {
                            loadIcon(Images.icon_menu_minus, Size(24.0F, 24.0F))
                        }
                        TextStyled(
                            count.value,
                            Localize.cigar_search_details_add_count_dialog,
                            TextStyles.Headline,
                            labelStyle = TextStyles.None,
                            modifier = Modifier.width(100.dp).padding(horizontal = 10.dp),
                            editable = true,
                            maxLines = 1,
                            inputMode = InputMode.Number,
                            center = true,
                            onValueChange = {
                                count.value = it
                            }
                        )
                        IconButton(
                            modifier = Modifier.wrapContentSize().semantics {
                                contentDescription = Localize.cigar_details_humidors_count_dialog_plus_desc
                            },
                            onClick = {
                                count.value = (count.value.ifBlank { "0" }).toInt().inc().toString()
                                if (viewModel.humidorCigarsCount!!.count < count.value.toLong()) {
                                    showPrice.value = true
                                }
                            }) {
                            loadIcon(Images.icon_menu_plus, Size(24.0F, 24.0F))
                        }
                    }
                    if (showPrice.value) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                        ) {
                            TextStyled(
                                price.value,
                                Localize.cigar_details_count_dialog_price,
                                TextStyles.Headline,
                                labelStyle = TextStyles.Subhead,
                                editable = true,
                                modifier = Modifier.padding(bottom = 10.dp),
                                onValueChange = {
                                    price.value = it
                                },
                                inputMode = InputMode.Price
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun CigarsRatingDialog(onDismissRequest: () -> Unit) {
        if (viewModel.cigarRating) {
            val rating = remember { mutableStateOf("0") }

            LaunchedEffect(viewModel.myRating) {
                rating.value = (viewModel.myRating ?: 0).toString()
            }

            TwoButtonDialog(
                Localize.cigar_details_rating_dialog,
                onDismissRequest = onDismissRequest,
                onVerify = { viewModel.verifyFields() },
                onComplete = {
                    viewModel.updateCigarRating(rating.value.toLong())
                }
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    TextStyled(
                        rating.value,
                        Localize.cigar_details_myrating,
                        TextStyles.Headline,
                        labelStyle = TextStyles.None,
                        modifier = Modifier.width(100.dp),
                        editable = true,
                        maxLines = 1,
                        inputMode = InputMode.Number,
                        center = true,
                        onValueChange = {
                            rating.value = if (it.length > 2) it.substring(0, 2) else it
                        }
                    )
                }
            }
        }
    }

    private fun isValid(from: HumidorCigar?, to: Humidor?, count: Long): Boolean {
        return from?.let { fromCH ->
            fromCH.humidor.let { fromH ->
                to?.let { to ->
                    from.count >= count && to.compareTo(fromH) != 0
                }
            }
        } ?: false
    }

    @Composable
    private fun MoveCigarsDialog(onDismissRequest: () -> Unit) {
        if (viewModel.moveCigarDialog) {
            val count = remember { mutableStateOf(1L) }
            val from = remember { mutableStateOf<HumidorCigar?>(null) }
            val to = remember { mutableStateOf<Humidor?>(null) }
            val fromList = remember { mutableStateOf<List<ValuePickerItem>>(listOf()) }
            val toList = remember { mutableStateOf<List<ValuePickerItem>>(listOf()) }

            LaunchedEffect(from.value, to.value) {
                fromList.value = viewModel.moveFromHumidors()
                toList.value = viewModel.moveToHumidors(from.value?.humidor)
                count.value = from.value?.count ?: 1L
            }

            TwoButtonDialog(
                Localize.cigar_details_move_dialog,
                onDismissRequest = onDismissRequest,
                onVerify = { isValid(from.value, to.value, count.value) },
                onComplete = {
                    viewModel.moveCigar(from.value!!, to.value!!, count.value)
                }
            ) {
                Column(
                    modifier = Modifier.wrapContentSize(),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    ValuePicker(
                        modifier = Modifier.padding(bottom = 10.dp).fillMaxWidth().semantics {
                            contentDescription = Localize.cigar_details_humidors_move_dialog_from_desc
                        },
                        backgroundColor = MaterialColors.color_outline,
                        value = ValuePickerItem(
                            null,
                            Localize.cigar_details_move_select,
                            null
                        ),
                        label = Localize.cigar_details_move_from,
                        items = fromList.value,
                        onClick = {
                            from.value = it.value as HumidorCigar?
                        }
                    )
                    ValuePicker(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp).semantics {
                            contentDescription = Localize.cigar_details_humidors_move_dialog_to_desc
                        },
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
                    Row(
                        modifier = Modifier.width(100.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        TextStyled(
                            if (count.value > 0) "${count.value}" else "",
                            Localize.cigar_details_humidors_move_dialog_count_desc,
                            TextStyles.Headline,
                            labelStyle = TextStyles.None,
                            modifier = Modifier.wrapContentSize(),
                            editable = true,
                            maxLines = 1,
                            inputMode = InputMode.Number,
                            center = true,
                            onValueChange = {
                                count.value = if (it.isNotBlank()) it.toLong() else 0
                            }
                        )
                    }
                }
            }
        }
    }

}