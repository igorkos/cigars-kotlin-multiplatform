/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/14/24, 3:00 PM
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

package com.akellolcc.cigars.screens.base

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults.centerAlignedTopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.akellolcc.cigars.mvvm.base.BaseDetailsScreenViewModel
import com.akellolcc.cigars.screens.components.BackButton
import com.akellolcc.cigars.screens.components.DefaultButton
import com.akellolcc.cigars.screens.components.PagedCarousel
import com.akellolcc.cigars.screens.navigation.ITabItem
import com.akellolcc.cigars.screens.navigation.NavRoute
import com.akellolcc.cigars.theme.DefaultTheme
import com.akellolcc.cigars.theme.Images
import com.akellolcc.cigars.theme.Localize
import com.akellolcc.cigars.theme.MaterialColors
import com.akellolcc.cigars.theme.loadIcon
import com.akellolcc.cigars.theme.materialColor
import kotlin.jvm.Transient

open class BaseItemDetailsScreen<T : BaseDetailsScreenViewModel<*>>(override val route: NavRoute) : ITabItem<T> {
    @kotlinx.serialization.Transient
    @Transient
    override lateinit var viewModel: T

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        LaunchedEffect(Unit) {
            viewModel.observeEvents(tag()) {
                handleAction(it, navigator)
            }
            viewModel.observeObject()
        }

        DefaultTheme {
            Scaffold(
                modifier = Modifier.fillMaxSize().semantics {
                    contentDescription = route.semantics
                    stateDescription = "${viewModel.loadingState}"
                },
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
                        //details
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
                            details()
                        }
                    }
                }
            }
        }
    }

    @Composable
    open fun topTabBarActions() {
        IconButton(
            modifier = Modifier.semantics { contentDescription = Localize.cigar_details_top_bar_edit_desc },
            onClick = { viewModel.editing = !viewModel.editing }) {
            loadIcon(Images.icon_menu_edit, Size(24.0F, 24.0F))
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    protected fun topTabBar() {
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
                    topTabBarActions()
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
    open fun details() {
    }

    @Composable
    open fun dialogs() {
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
                    loading = viewModel.loadingState,
                ) {
                    viewModel.showImages(it)
                }
            }
        }
    }
}