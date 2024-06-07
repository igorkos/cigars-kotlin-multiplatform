/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/7/24, 12:12 AM
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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults.centerAlignedTopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.akellolcc.cigars.databases.models.Cigar
import com.akellolcc.cigars.databases.models.CigarShapes
import com.akellolcc.cigars.databases.models.CigarStrength
import com.akellolcc.cigars.databases.models.Humidor
import com.akellolcc.cigars.databases.models.HumidorCigar
import com.akellolcc.cigars.databases.models.emptyCigar
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.mvvm.base.createViewModel
import com.akellolcc.cigars.mvvm.cigars.CigarsDetailsScreenViewModel
import com.akellolcc.cigars.screens.components.BackButton
import com.akellolcc.cigars.screens.components.DefaultButton
import com.akellolcc.cigars.screens.components.DialogButton
import com.akellolcc.cigars.screens.components.InfoImageDialog
import com.akellolcc.cigars.screens.components.PagedCarousel
import com.akellolcc.cigars.screens.components.TextStyled
import com.akellolcc.cigars.screens.components.ValueCard
import com.akellolcc.cigars.screens.components.ValuePicker
import com.akellolcc.cigars.screens.components.ValuePickerItem
import com.akellolcc.cigars.screens.components.ValuesCard
import com.akellolcc.cigars.screens.components.transformations.InputMode
import com.akellolcc.cigars.screens.navigation.CigarHistoryRoute
import com.akellolcc.cigars.screens.navigation.CigarImagesViewRoute
import com.akellolcc.cigars.screens.navigation.HumidorCigarsRoute
import com.akellolcc.cigars.screens.navigation.ITabItem
import com.akellolcc.cigars.screens.navigation.NavRoute
import com.akellolcc.cigars.theme.DefaultTheme
import com.akellolcc.cigars.theme.Images
import com.akellolcc.cigars.theme.Localize
import com.akellolcc.cigars.theme.MaterialColors
import com.akellolcc.cigars.theme.TextStyles
import com.akellolcc.cigars.theme.loadIcon
import com.akellolcc.cigars.theme.materialColor
import kotlin.jvm.Transient

open class CigarDetailsScreen(override val route: NavRoute) : ITabItem<CigarsDetailsScreenViewModel> {
    @kotlinx.serialization.Transient
    @Transient
    override lateinit var viewModel: CigarsDetailsScreenViewModel

    @Composable
    override fun Content() {
        viewModel = rememberScreenModel { createViewModel(CigarsDetailsScreenViewModel::class, route.data ?: emptyCigar) }
        val navigator = LocalNavigator.currentOrThrow

        LaunchedEffect(Unit) {
            viewModel.observeEvents(tag()) {
                handleAction(it, navigator)
            }
        }

        LaunchedEffect(viewModel.editing) {
            viewModel.observeCigar()
        }

        DefaultTheme {
            Scaffold(
                modifier = Modifier.fillMaxSize().semantics { contentDescription = route.semantics },
                topBar = { topTabBar() },
                bottomBar = { bottomTabBar() }
            ) {
                CompositionLocalProvider(LocalContentColor provides materialColor(MaterialColors.color_onPrimaryContainer)) {
                    dialogs()
                    Column(
                        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
                    ) {
                        //Top Images view
                        imagesView()
                        //Cigar details
                        Column(modifier = with(Modifier) {
                            fillMaxSize()
                                .padding(
                                    PaddingValues(
                                        16.dp,
                                        if (!viewModel.editing) 16.dp else it.calculateTopPadding(),
                                        16.dp,
                                        it.calculateBottomPadding() + 16.dp
                                    )
                                )
                        }) {
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
                    }
                }
            }
        }
    }

    override fun handleAction(
        event: Any,
        navigator: Navigator
    ) {
        when (event) {
            is CigarsDetailsScreenViewModel.CigarsDetailsAction.AddToHumidor -> viewModel.humidorCigarsCount = event.humidor

            is CigarsDetailsScreenViewModel.CigarsDetailsAction.OpenHumidor -> navigator.push(
                HumidorCigarsScreen(
                    HumidorCigarsRoute.apply {
                        this.data = event.humidor
                    })
            )

            is CigarsDetailsScreenViewModel.CigarsDetailsAction.OpenHistory -> navigator.push(
                CigarHistoryScreen(
                    CigarHistoryRoute.apply {
                        this.data = event.cigar
                    })
            )

            is CigarsDetailsScreenViewModel.CigarsDetailsAction.MoveCigar -> viewModel.moveCigarDialog =
                true

            is CigarsDetailsScreenViewModel.CigarsDetailsAction.ShowImages -> {
                val data = route.data as Cigar
                navigator.push(ImagesViewScreen(CigarImagesViewRoute.apply {
                    this.data = Pair(data, event.selected)
                }))
            }

            is CigarsDetailsScreenViewModel.CigarsDetailsAction.AddCigarToHumidor -> TODO()
            else -> super.handleAction(event, navigator)
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    open fun topTabBar() {
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
                    IconButton(
                        modifier = Modifier.semantics { contentDescription = Localize.cigar_details_top_bar_history_desc },
                        onClick = { viewModel.historyOpen() }) {
                        loadIcon(Images.icon_menu_history, Size(24.0F, 24.0F))
                    }
                    IconButton(
                        modifier = Modifier.semantics { contentDescription = Localize.cigar_details_top_bar_edit_desc },
                        onClick = { viewModel.editing = !viewModel.editing }) {
                        loadIcon(Images.icon_menu_edit, Size(24.0F, 24.0F))
                    }
                }
            },
            title = {})
    }

    @Composable
    open fun bottomTabBar() {
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
                        title = Localize.button_save,
                        onClick = { viewModel.onSaveEdit() })
                }
            )
        }
    }

    @Composable
    open fun dialogs() {
        HumidorCigarsCountDialog {
            viewModel.onCancelEdit()
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
                        CigarsDetailsScreenViewModel.InfoActions.CigarRatings -> Localize.cigar_details_size_info_dialog_desc
                        else -> ""
                    }
                }
            ) {
                viewModel.infoDialog = CigarsDetailsScreenViewModel.InfoActions.None
            }
        }
    }

    @Composable
    private fun imagesView() {
        if (!viewModel.editing) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.padding(bottom = 10.dp)
                    .aspectRatio(ratio = 1.8f).semantics { contentDescription = Localize.cigar_details_images_block_desc }
            ) {
                PagedCarousel(
                    viewModel.images,
                    loading = viewModel.loading,
                ) {
                    viewModel.showImages(it)
                }
            }
        }
    }

    @Composable
    private fun cigarOrigin() {
        //Cigar Name,  Company and Country
        Column(modifier = Modifier.semantics { contentDescription = Localize.cigar_details_origin_block_desc }) {
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
        }
    }

    @Composable
    private fun cigarSize() {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
        ) {
            ValuesCard(
                modifier = Modifier.semantics { contentDescription = Localize.cigar_details_size_block_desc },
                label = Localize.cigar_details_cigars,
                vertical = viewModel.editing,
                actionIcon = Images.icon_menu_info,
                onAction = {
                    viewModel.infoDialog = CigarsDetailsScreenViewModel.InfoActions.CigarSize
                }
            ) {
                if (!viewModel.editing) {
                    if (viewModel.shape.isNotBlank()) {
                        ValueCard(Localize.cigar_details_shape, viewModel.shape)
                    }
                    if (viewModel.length.isNotBlank()) {
                        ValueCard(
                            Localize.cigar_details_length,
                            viewModel.length
                        )
                    }
                    if (viewModel.gauge != 0L) {
                        ValueCard(
                            Localize.cigar_details_gauge,
                            viewModel.gauge.toString()
                        )
                    }
                } else {
                    ValuePicker(
                        modifier = Modifier.padding(bottom = 10.dp),
                        value = ValuePickerItem(
                            CigarShapes.fromString(viewModel.shape),
                            viewModel.shape,
                            null
                        ),
                        label = Localize.cigar_details_shape,
                        items = CigarShapes.enumValues()
                            .map { ValuePickerItem(it.first, it.second, Images.tab_icon_cigars) },
                        onClick = {
                            viewModel.shape = it.label
                        }
                    )
                    TextStyled(
                        viewModel.length,
                        Localize.cigar_details_length,
                        TextStyles.Subhead,
                        labelStyle = TextStyles.Subhead,
                        inputMode = InputMode.Inches,
                        editable = viewModel.editing,
                        modifier = Modifier.padding(bottom = 10.dp).fillMaxWidth(),
                        onValueChange = {
                            viewModel.length = it
                        }
                    )
                    TextStyled(
                        if (viewModel.gauge == 0L) "" else viewModel.gauge.toString(),
                        Localize.cigar_details_gauge,
                        TextStyles.Subhead,
                        labelStyle = TextStyles.Subhead,
                        editable = viewModel.editing,
                        inputMode = InputMode.Number,
                        modifier = Modifier.fillMaxWidth(),
                        onValueChange = {
                            viewModel.gauge =
                                if (it.isNotBlank()) it.toLong() else 0
                        }
                    )
                }
            }
        }
    }

    @Composable
    private fun cigarTobacco() {
        if (!viewModel.editing && viewModel.wrapper.isBlank() && viewModel.binder.isBlank() && viewModel.filler.isBlank() && viewModel.strength == CigarStrength.Unknown) return
        Column {
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
                    if (!viewModel.editing) {
                        TextStyled(
                            CigarStrength.localized(viewModel.strength),
                            Localize.cigar_details_strength,
                            TextStyles.Subhead,
                            labelStyle = TextStyles.Subhead
                        )
                    } else {
                        ValuePicker(
                            value = ValuePickerItem(
                                viewModel.strength,
                                CigarStrength.localized(viewModel.strength),
                                null
                            ),
                            label = Localize.cigar_details_strength,
                            items = CigarStrength.enumValues()
                                .map { ValuePickerItem(it.first, it.second, null) },
                            onClick = {
                                viewModel.strength = it.value!!
                            }
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun cigarRatings() {
        if (!viewModel.editing && viewModel.rating == null && viewModel.myRating == null) return
        Column(horizontalAlignment = Alignment.Start) {
            ValuesCard(
                modifier = Modifier.semantics { contentDescription = Localize.cigar_details_ratings_block_desc },
                label = Localize.cigar_details_ratings,
                vertical = viewModel.editing,
                actionIcon = Images.icon_menu_info,
                onAction = {
                    viewModel.infoDialog = CigarsDetailsScreenViewModel.InfoActions.CigarRatings
                }
            ) {
                if (!viewModel.editing) {
                    ValueCard(
                        Localize.cigar_details_rating,
                        "${viewModel.rating}",
                        modifier = Modifier.semantics { contentDescription = Localize.cigar_details_ratings_block_external_desc })
                    ValueCard(
                        Localize.cigar_details_myrating,
                        "${viewModel.myRating}",
                        modifier = Modifier.semantics { contentDescription = Localize.cigar_details_ratings_block_my_desc }
                    ) {
                        viewModel.cigarRating = true
                    }
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
                } else {
                    TextStyled(
                        if (viewModel.rating == null) "" else viewModel.rating.toString(),
                        Localize.cigar_details_rating,
                        TextStyles.Subhead,
                        labelStyle = TextStyles.Subhead,
                        editable = viewModel.editing,
                        modifier = Modifier.padding(bottom = 10.dp).fillMaxWidth(),
                        onValueChange = {
                            viewModel.rating =
                                if (it.isNotBlank()) it.toLong() else 0
                        },
                        inputMode = InputMode.Number
                    )
                    TextStyled(
                        if (viewModel.myRating == null) "" else viewModel.myRating.toString(),
                        Localize.cigar_details_myrating,
                        TextStyles.Subhead,
                        labelStyle = TextStyles.Subhead,
                        editable = viewModel.editing,
                        modifier = Modifier.fillMaxWidth(),
                        onValueChange = {
                            viewModel.myRating =
                                if (it.isNotBlank()) it.toLong() else 0
                        },
                        inputMode = InputMode.Number
                    )
                }
            }
        }
    }

    @Composable
    private fun cigarHumidors() {
        if (!viewModel.editing && viewModel.humidors.isNotEmpty()) {
            Log.debug("Cigar Humidors:  ${viewModel.humidors.map { it.humidor.name }} ")
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(((viewModel.humidors.count() + 2) * 48).dp)
            ) {
                ValuesCard(
                    modifier = Modifier.semantics { contentDescription = Localize.cigar_details_humidors_block_desc },
                    label = Localize.cigar_details_humidors,
                    vertical = true,
                    actionIcon = if (viewModel.humidorsCount() > 1) Images.icon_tab else null,
                    onAction = {
                        viewModel.moveCigar()
                    }
                ) {
                    LazyColumn(
                        verticalArrangement = Arrangement.Top,
                        modifier = Modifier.fillMaxSize().testTag(tag(HUMIDORS_BLOCK_LIST))
                    )
                    {
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
            Dialog(onDismissRequest = { onDismissRequest() }) {
                Card(
                    modifier = Modifier.wrapContentSize().semantics { contentDescription = Localize.cigar_details_size_info_dialog_desc },
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
                            Localize.cigar_details_count_dialog,
                            Localize.nav_header_title_desc,
                            TextStyles.Headline,
                            labelStyle = TextStyles.None,
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth().padding(top = 24.dp, bottom = 24.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            IconButton(
                                modifier = Modifier.testTag(tag(DIALOG_CIGAR_COUNT_MINUS)),
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
                                modifier = Modifier.padding(horizontal = 10.dp).wrapContentSize()
                                    .weight(1f).testTag(tag(DIALOG_CIGAR_COUNT_INPUT)),
                                editable = true,
                                maxLines = 1,
                                inputMode = InputMode.Number,
                                center = true,
                                onValueChange = {
                                    count.value = it
                                }
                            )
                            IconButton(
                                modifier = Modifier.testTag(tag(DIALOG_CIGAR_COUNT_PLUS)),
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
                                    modifier = Modifier.padding(bottom = 10.dp).testTag(tag(DIALOG_CIGAR_COUNT_PRICE)),
                                    onValueChange = {
                                        price.value = it
                                    },
                                    inputMode = InputMode.Price
                                )
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                        ) {
                            DialogButton(
                                title = Localize.button_cancel,
                                onClick = { viewModel.humidorCigarsCount = null })
                            DialogButton(
                                enabled = viewModel.verifyFields(),
                                title = Localize.button_save,
                                onClick = {
                                    viewModel.updateCigarCount(
                                        viewModel.humidorCigarsCount!!,
                                        count.value.toLong(),
                                        if (price.value.isNotBlank()) price.value.toDouble() else null
                                    )
                                })
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun CigarsRatingDialog(onDismissRequest: () -> Unit) {
        val rating = remember { mutableStateOf("0") }

        LaunchedEffect(viewModel.myRating) {
            rating.value = (viewModel.myRating ?: 0).toString()
        }

        if (viewModel.cigarRating) {
            Dialog(onDismissRequest = { onDismissRequest() }) {
                Card(
                    modifier = Modifier.wrapContentSize().testTag(tag(DIALOG_CIGAR_RATING)),
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
                            Localize.cigar_details_rating_dialog,
                            Localize.nav_header_title_desc,
                            TextStyles.Headline,
                            labelStyle = TextStyles.None,
                        )
                        Row(
                            modifier = Modifier.width(100.dp).padding(top = 24.dp, bottom = 24.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            TextStyled(
                                rating.value,
                                Localize.cigar_details_myrating,
                                TextStyles.Headline,
                                labelStyle = TextStyles.None,
                                modifier = Modifier.padding(horizontal = 10.dp),
                                editable = true,
                                maxLines = 1,
                                inputMode = InputMode.Number,
                                center = true,
                                onValueChange = {
                                    rating.value = if (it.length > 2) it.substring(0, 2) else it
                                }
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                        ) {
                            DialogButton(
                                title = Localize.button_cancel,
                                onClick = { onDismissRequest() })
                            DialogButton(
                                enabled = viewModel.verifyFields(),
                                title = Localize.button_save,
                                onClick = {
                                    viewModel.updateCigarRating(rating.value.toLong())
                                    onDismissRequest()
                                })
                        }
                    }
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
            val fromList = remember { mutableStateOf<List<ValuePickerItem<HumidorCigar>>>(listOf()) }
            val toList = remember { mutableStateOf<List<ValuePickerItem<Humidor>>>(listOf()) }

            LaunchedEffect(from.value, to.value) {
                fromList.value = viewModel.moveFromHumidors()
                toList.value = viewModel.moveToHumidors(from.value?.humidor)
                count.value = from.value?.count ?: 1L
            }

            Dialog(onDismissRequest = { onDismissRequest() }) {
                Card(
                    modifier = Modifier.wrapContentSize().testTag(tag(DIALOG_MOVE_CIGARS)),
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
                            Localize.cigar_details_move_dialog,
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
                                modifier = Modifier.padding(bottom = 10.dp).fillMaxWidth().testTag(tag(DIALOG_MOVE_CIGARS_FROM)),
                                backgroundColor = MaterialColors.color_outline,
                                value = ValuePickerItem(
                                    null,
                                    Localize.cigar_details_move_select,
                                    null
                                ),
                                label = Localize.cigar_details_move_from,
                                items = fromList.value,
                                onClick = {
                                    from.value = it.value
                                }
                            )
                            ValuePicker(
                                modifier = Modifier.fillMaxWidth().testTag(tag(DIALOG_MOVE_CIGARS_TO)),
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
                            Row(
                                modifier = Modifier.width(100.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                TextStyled(
                                    if (count.value > 0) "${count.value}" else "",
                                    Localize.cigar_search_details_add_count_dialog,
                                    TextStyles.Headline,
                                    labelStyle = TextStyles.None,
                                    modifier = Modifier.padding(horizontal = 10.dp)
                                        .wrapContentSize().testTag(tag(DIALOG_MOVE_CIGARS_COUNT)),
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
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
                            horizontalArrangement = Arrangement.End,
                        ) {
                            DialogButton(
                                title = Localize.button_cancel,
                                onClick = { onDismissRequest() })
                            DialogButton(
                                enabled = isValid(from.value, to.value, count.value),
                                title = Localize.button_save,
                                onClick = {
                                    viewModel.moveCigar(from.value!!, to.value!!, count.value)
                                })
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val TOP_BAR_HISTORY = "history_button"
        const val TOP_BAR_EDIT = "edit_button"
        const val ORIGIN_BLOCK = "cigar_origin"
        const val SIZE_BLOCK = "cigar_size"
        const val TOBACCO_BLOCK = "cigar_tobacco"
        const val RATINGS_BLOCK = "cigar_ratings"
        const val RATINGS_FAVORITE = "cigar_favorite_"
        const val HUMIDORS_BLOCK = "cigar_humidors"
        const val HUMIDORS_BLOCK_LIST = "cigar_humidors_list"
        const val NOTES_BLOCK = "cigar_notes"
        const val DIALOG_CIGAR_COUNT = "humidor_cigar_count_dialog"
        const val DIALOG_CIGAR_COUNT_PLUS = "cigar_count_plus"
        const val DIALOG_CIGAR_COUNT_MINUS = "cigar_count_minus"
        const val DIALOG_CIGAR_COUNT_INPUT = "cigar_count"
        const val DIALOG_CIGAR_COUNT_PRICE = "cigar_count_price"
        const val DIALOG_CIGAR_RATING = "cigar_rating_dialog"
        const val DIALOG_MOVE_CIGARS = "move_cigars_dialog"
        const val DIALOG_MOVE_CIGARS_FROM = "move_from_humidor"
        const val DIALOG_MOVE_CIGARS_TO = "move_to_humidor"
        const val DIALOG_MOVE_CIGARS_COUNT = "move_count"
    }
}