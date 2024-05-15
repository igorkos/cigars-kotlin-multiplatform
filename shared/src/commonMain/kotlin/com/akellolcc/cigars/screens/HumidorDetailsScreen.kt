/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/14/24, 2:56 PM
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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.akellolcc.cigars.databases.models.Humidor
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.mvvm.base.createViewModel
import com.akellolcc.cigars.mvvm.humidor.HumidorDetailsScreenViewModel
import com.akellolcc.cigars.screens.components.DialogButton
import com.akellolcc.cigars.screens.components.PagedCarousel
import com.akellolcc.cigars.screens.components.TextStyled
import com.akellolcc.cigars.screens.components.ValueCard
import com.akellolcc.cigars.screens.components.ValuesCard
import com.akellolcc.cigars.screens.components.transformations.InputMode
import com.akellolcc.cigars.screens.navigation.HumidorImagesViewRoute
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

class HumidorDetailsScreen(override val route: NavRoute) : ITabItem<HumidorDetailsScreenViewModel> {

    @Transient
    override lateinit var viewModel: HumidorDetailsScreenViewModel

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        viewModel =
            rememberScreenModel {
                createViewModel(
                    HumidorDetailsScreenViewModel::class,
                    route.data
                )
            }
        var notesHeight by remember { mutableStateOf(0) }
        val navigator = LocalNavigator.currentOrThrow
        Log.debug("Images: ${viewModel.images.size} : ${viewModel.loading}  ")
        viewModel.observeEvents {
            when (it) {
                is HumidorDetailsScreenViewModel.Action.OnBackAction -> navigator.pop()
                is HumidorDetailsScreenViewModel.Action.ShowError -> TODO()
            }
        }

        LaunchedEffect(viewModel.editing) {
            viewModel.observeHumidor()
        }

        val topColors = centerAlignedTopAppBarColors(
            containerColor = materialColor(MaterialColors.color_transparent),
            navigationIconContentColor = materialColor(MaterialColors.color_onPrimaryContainer),
            actionIconContentColor = materialColor(MaterialColors.color_onPrimaryContainer),
        )

        DefaultTheme {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    CenterAlignedTopAppBar(
                        colors = topColors,
                        navigationIcon = {
                            if (!viewModel.editing) {
                                IconButton(onClick = {
                                    viewModel.sendEvent(
                                        HumidorDetailsScreenViewModel.Action.OnBackAction(
                                            0
                                        )
                                    )
                                }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = null
                                    )
                                }
                            }
                        },
                        actions = {
                            if (!viewModel.editing) {
                                IconButton(onClick = { viewModel.editing = !viewModel.editing }) {
                                    loadIcon(Images.icon_menu_edit, Size(24.0F, 24.0F))
                                }
                            }
                        },
                        title = {})
                },
                bottomBar = {
                    if (viewModel.editing) {
                        BottomAppBar(
                            actions = {
                                Box(modifier = Modifier.weight(1f))
                                DialogButton(
                                    title = Localize.button_cancel,
                                    modifier = Modifier.padding(end = 10.dp).weight(2f),
                                    onClick = { viewModel.cancelEdit() })
                                DialogButton(
                                    modifier = Modifier.weight(2f),
                                    enabled = viewModel.verifyFields(),
                                    title = Localize.button_save,
                                    onClick = { viewModel.saveEdit() })
                            }
                        )
                    }
                }
            ) {
                CompositionLocalProvider(
                    LocalContentColor provides materialColor(MaterialColors.color_onPrimaryContainer)
                )
                {
                    Column(
                        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
                    ) {
                        //Top Images view
                        if (!viewModel.editing) {
                            Column(
                                horizontalAlignment = Alignment.Start,
                                modifier = Modifier.padding(bottom = 10.dp)
                                    .aspectRatio(ratio = 1.8f)
                            ) {
                                PagedCarousel(
                                    viewModel.images,
                                    loading = viewModel.loading,
                                ) {
                                    val data = route.data as Humidor
                                    navigator.push(ImagesViewScreen(HumidorImagesViewRoute.apply {
                                        this.data = Pair(data, it)
                                    }))
                                }
                            }
                        }
                        //Humidor details
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
                            //Humidor Name Brand
                            Column {
                                TextStyled(
                                    viewModel.name,
                                    TextStyles.Headline,
                                    label = if (viewModel.editing) Localize.cigar_details_name else null,
                                    editable = viewModel.editing,
                                    maxLines = 2,
                                    modifier = Modifier.padding(bottom = 4.dp),
                                    onValueChange = {
                                        viewModel.name = it
                                    }
                                )
                                TextStyled(
                                    viewModel.brand,
                                    TextStyles.Subhead,
                                    label = if (viewModel.editing) Localize.cigar_details_company else null,
                                    editable = viewModel.editing,
                                    modifier = Modifier.padding(bottom = 4.dp),
                                    onValueChange = {
                                        viewModel.brand = it
                                    }
                                )
                            }
                            //Humidor cigars
                            Column(
                                horizontalAlignment = Alignment.Start,
                                modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
                            ) {
                                if (!viewModel.editing) {
                                    ValuesCard(
                                        label = Localize.humidor_details_cigars,
                                        vertical = viewModel.editing
                                    ) {
                                        ValueCard(
                                            Localize.humidor_details_holds,
                                            viewModel.holds.toString()
                                        )
                                        ValueCard(
                                            Localize.humidor_details_count,
                                            viewModel.count.toString()
                                        )
                                    }
                                } else {
                                    TextStyled(
                                        if (viewModel.holds == 0L) "" else viewModel.holds.toString(),
                                        TextStyles.Subhead,
                                        labelStyle = TextStyles.Subhead,
                                        label = Localize.humidor_details_holds,
                                        editable = viewModel.editing,
                                        modifier = Modifier.padding(bottom = 10.dp),
                                        onValueChange = {
                                            viewModel.holds =
                                                if (it.isNotBlank()) it.toLong() else 0
                                        },
                                        inputMode = InputMode.Number
                                    )
                                }
                            }
                            Column(
                                horizontalAlignment = Alignment.Start,
                                modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
                            ) {
                                if (!viewModel.editing && (viewModel.temperature != null || viewModel.humidity != null)) {
                                    ValuesCard(
                                        label = Localize.humidor_details_humidor,
                                        vertical = viewModel.editing
                                    ) {
                                        viewModel.temperature?.let {
                                            ValueCard(
                                                Localize.humidor_details_temperature,
                                                it.toString()
                                            )
                                        }
                                        viewModel.humidity?.let {
                                            ValueCard(
                                                Localize.humidor_details_humidity,
                                                it.toString()
                                            )
                                        }
                                    }
                                } else if (viewModel.editing) {
                                    TextStyled(
                                        if (viewModel.temperature == null || viewModel.temperature == 0L) "" else viewModel.temperature.toString(),
                                        TextStyles.Subhead,
                                        labelStyle = TextStyles.Subhead,
                                        label = Localize.humidor_details_temperature,
                                        editable = viewModel.editing,
                                        modifier = Modifier.padding(bottom = 10.dp),
                                        onValueChange = {
                                            viewModel.temperature =
                                                if (it.isNotBlank()) it.toLong() else 0
                                        },
                                        inputMode = InputMode.Temperature
                                    )
                                    TextStyled(
                                        if (viewModel.humidity == null || viewModel.humidity == 0.0) "" else viewModel.humidity.toString(),
                                        TextStyles.Subhead,
                                        labelStyle = TextStyles.Subhead,
                                        label = Localize.humidor_details_humidity,
                                        editable = viewModel.editing,
                                        modifier = Modifier.padding(bottom = 10.dp),
                                        onValueChange = {
                                            viewModel.humidity =
                                                if (it.isNotBlank()) it.toDouble() else 0.0
                                        },
                                        inputMode = InputMode.Humidity
                                    )
                                }
                                //Cigar Notes and Link
                                Column(
                                    modifier = Modifier
                                        .padding(bottom = 10.dp, top = 10.dp)
                                        .onSizeChanged {
                                            notesHeight = it.height
                                        }
                                ) {
                                    TextStyled(
                                        viewModel.notes,
                                        TextStyles.Subhead,
                                        //label = "Notes",
                                        editable = viewModel.editing,
                                        onValueChange = {
                                            viewModel.notes = it
                                        }
                                    )
                                }
                                if (viewModel.link != null || viewModel.editing) {
                                    Column(
                                        modifier = Modifier.padding(bottom = 10.dp)
                                    ) {
                                        TextStyled(
                                            viewModel.link,
                                            TextStyles.Subhead,
                                            label = Localize.cigar_details_link,
                                            labelStyle = TextStyles.Subhead,
                                            editable = viewModel.editing,
                                            maxLines = 1,
                                            onValueChange = {
                                                viewModel.link = it
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }
    }
}