/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/20/24, 7:09 PM
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

package com.akellolcc.cigars.screens

import TextStyled
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import com.akellolcc.cigars.common.theme.DefaultTheme
import com.akellolcc.cigars.databases.extensions.BaseEntity
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.mvvm.BaseListViewModel
import com.akellolcc.cigars.screens.navigation.ITabItem
import com.akellolcc.cigars.screens.navigation.NavRoute
import com.akellolcc.cigars.theme.Images
import com.akellolcc.cigars.theme.MaterialColors
import com.akellolcc.cigars.theme.TextStyles
import com.akellolcc.cigars.theme.loadIcon
import com.akellolcc.cigars.theme.materialColor
import com.akellolcc.cigars.utils.ui.reachedBottom

abstract class BaseTabListScreen<A, E : BaseEntity>(override val route: NavRoute) : ITabItem {
    abstract val viewModel: BaseListViewModel<E, A>

    abstract fun handleAction(event: A, navigator: Navigator?)

    @Composable
    open fun RightActionMenu(onDismiss: () -> Unit) {
    }

    @Composable
    abstract fun EntityListRow(entity: E, modifier: Modifier)

    @Composable
    open fun ListHeader(modifier: Modifier) {
    }

    @Composable
    open fun ListFooter(modifier: Modifier) {
    }

    @Composable
    open fun ContentFooter(modifier: Modifier) {
    }

    @Composable
    open fun ContentHeader(modifier: Modifier) {
    }

    open fun loadMore() {
        viewModel.loadMore()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        // val entities by viewModel.asState()

        LaunchedEffect(navigator) {
            viewModel.loadMore()
            if (route.sharedViewModel?.isTabsVisible != route.isTabsVisible)
                route.sharedViewModel?.isTabsVisible = route.isTabsVisible
        }

        viewModel.observeEvents {
            handleAction(it, navigator)
        }

        val scrollBehavior =
            TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

        val listState = rememberLazyListState()
        DefaultTheme {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = { topTabBar(scrollBehavior, navigator) },
                bottomBar = {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(64.dp)
                            .background(Color.Transparent)
                    )
                },
                content = {
                    val padding by remember { mutableStateOf(it.calculateTopPadding() + 16.dp to it.calculateBottomPadding() + 16.dp) }
                    Log.debug("padding $padding")
                    if (viewModel.loading && route.isLoadingCover) {
                        Box(
                            modifier = Modifier.fillMaxSize().background(
                                materialColor(
                                    MaterialColors.color_transparent
                                )
                            ), contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.width(64.dp),
                            )
                        }
                    } else {
                        Column(
                            modifier = Modifier.padding(
                                top = padding.first,
                                bottom = padding.second,
                                start = 16.dp,
                                end = 16.dp
                            ),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            ContentHeader(Modifier)
                            if (viewModel.entities.isNotEmpty()) {
                                entitiesList(viewModel.entities, listState) {
                                    loadMore()
                                }
                            }
                            ContentFooter(Modifier)
                        }
                    }
                }
            )
        }
    }

    @Composable
    private fun entitiesList(
        entities: List<E>,
        listState: LazyListState,
        loadMore: (() -> Unit)? = null
    ) {
        val reachedBottom: Boolean by remember { derivedStateOf { listState.reachedBottom() } }

        // load more if scrolled to bottom
        LaunchedEffect(reachedBottom) {
            if (reachedBottom) loadMore?.invoke()
        }
        LazyColumn(
            state = listState,
            verticalArrangement = Arrangement.Top,
        )
        {
            item {
                ListHeader(Modifier.fillMaxWidth())
            }
            items(entities, key = { item -> item.key }) {
                val clickableModifier = remember(it.key) {
                    Modifier.clickable { viewModel.entitySelected(it) }
                }
                EntityListRow(it, clickableModifier)
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .background(Color.Transparent)
                )
            }
            item {
                ListFooter(Modifier.fillMaxWidth())
            }
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    open fun topTabBar(scrollBehavior: TopAppBarScrollBehavior, navigator: Navigator?) {
        var expanded by remember { mutableStateOf(false) }

        CenterAlignedTopAppBar(
            navigationIcon = {
                IconButton(onClick = {
                    route.sharedViewModel?.isDrawerVisible = true
                }) { loadIcon(Images.icon_menu_dots, Size(24.0F, 24.0F)) }
            },
            actions = {
                IconButton(onClick = { viewModel.sortingOrder(!viewModel.accenting) }) {
                    loadIcon(
                        if (viewModel.accenting) Images.icon_menu_sort_alpha_asc else Images.icon_menu_sort_alpha_desc,
                        Size(24.0F, 24.0F)
                    )
                }
                IconButton(onClick = { expanded = !expanded }) {
                    loadIcon(Images.icon_menu, Size(24.0F, 24.0F))
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        RightActionMenu {
                            expanded = false
                        }
                    }
                }
            },
            title = { TextStyled(text = route.title, style = TextStyles.ScreenTitle) },
            scrollBehavior = scrollBehavior
        )
    }

}