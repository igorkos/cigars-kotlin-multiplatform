/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/31/24, 12:03 PM
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import app.cash.paging.compose.collectAsLazyPagingItems
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.akellolcc.cigars.databases.models.BaseEntity
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.mvvm.MainScreenViewModel
import com.akellolcc.cigars.mvvm.base.BaseListViewModel
import com.akellolcc.cigars.mvvm.base.createViewModel
import com.akellolcc.cigars.screens.components.TextStyled
import com.akellolcc.cigars.screens.navigation.ITabItem
import com.akellolcc.cigars.screens.navigation.NavRoute
import com.akellolcc.cigars.theme.DefaultTheme
import com.akellolcc.cigars.theme.Images
import com.akellolcc.cigars.theme.Localize
import com.akellolcc.cigars.theme.MaterialColors
import com.akellolcc.cigars.theme.TextStyles
import com.akellolcc.cigars.theme.loadIcon
import com.akellolcc.cigars.theme.materialColor

abstract class BaseTabListScreen<E : BaseEntity, VM : BaseListViewModel<E>>(override val route: NavRoute) :
    ITabItem<VM> {
    @kotlinx.serialization.Transient
    @kotlin.jvm.Transient
    override lateinit var viewModel: VM

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


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val sharedViewModel = createViewModel(MainScreenViewModel::class)

        LaunchedEffect(navigator, viewModel) {
            if (sharedViewModel.isTabsVisible != route.isTabsVisible)
                sharedViewModel.isTabsVisible = route.isTabsVisible
            viewModel.observeEvents(tag()) {
                handleAction(it, navigator)
            }
        }

        val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

        DefaultTheme {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = { topTabBar(scrollBehavior) },
                bottomBar = {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(64.dp)
                            .background(Color.Transparent)
                    )
                },
                content = {
                    val padding by remember { mutableStateOf(it.calculateTopPadding() + 16.dp to it.calculateBottomPadding() + 16.dp) }
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
                        entitiesList()
                        ContentFooter(Modifier)
                    }
                }
            )
        }
    }

    @Composable
    open fun entitiesList() {
        val listState = rememberLazyListState()
        val pagingItems = viewModel.items?.collectAsLazyPagingItems()

        LaunchedEffect(Unit) {
            viewModel.paging()
        }

        when (pagingItems?.loadState?.refresh) {
            is LoadState.Error -> {

            }

            is LoadState.NotLoading -> {
                if (pagingItems.itemCount == 0) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = materialColor(MaterialColors.color_transparent),
                            contentColor = materialColor(MaterialColors.color_onSurfaceVariant)
                        ),
                    ) {
                        TextStyled(
                            modifier = Modifier.fillMaxWidth(),
                            center = true,
                            text = Localize.list_is_empty,
                            style = TextStyles.Subhead
                        )
                    }
                } else {
                    // Log.debug("${this::class.simpleName} items count: ${pagingItems.itemCount}")
                    LazyColumn(
                        modifier = Modifier.testTag("${route.route}-List"),
                        state = listState,
                        verticalArrangement = Arrangement.Top,
                    )
                    {
                        item {
                            ListHeader(Modifier.fillMaxWidth())
                        }
                        items(count = pagingItems.itemCount, key = { index -> pagingItems[index]?.key ?: "NoN" }) { index ->
                            pagingItems[index]?.let {
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
                        }
                        item {
                            ListFooter(Modifier.fillMaxWidth())
                        }
                    }
                }
            }

            else -> {
                Log.debug("${this::class.simpleName} load data state: ${pagingItems?.loadState?.refresh}")
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.width(32.dp),
                    )
                }
            }
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    open fun topTabBar(scrollBehavior: TopAppBarScrollBehavior?) {
        var expanded by remember { mutableStateOf(false) }

        CenterAlignedTopAppBar(
            navigationIcon = {
                IconButton(onClick = {
                    val sharedViewModel = createViewModel(MainScreenViewModel::class)
                    sharedViewModel.isDrawerVisible = true
                }) { loadIcon(Images.icon_menu_dots, Size(24.0F, 24.0F)) }
            },
            actions = {
                if (route.isSearchEnabled) {
                    IconButton(
                        modifier = Modifier.testTag("${route.route}-Filter"),
                        onClick = { viewModel.updateSearch(!viewModel.search) }) {
                        loadIcon(
                            Images.tab_icon_search,
                            Size(24.0F, 24.0F)
                        )
                    }
                }
                IconButton(
                    modifier = Modifier.testTag("${route.route}-Sort"),
                    onClick = { viewModel.sortingOrder(!viewModel.accenting) }) {
                    loadIcon(
                        if (viewModel.accenting) Images.icon_menu_sort_alpha_asc else Images.icon_menu_sort_alpha_desc,
                        Size(24.0F, 24.0F)
                    )
                }
                IconButton(modifier = Modifier.testTag("${route.route}-SortField"),
                    onClick = { expanded = !expanded }) {
                    loadIcon(Images.icon_menu, Size(24.0F, 24.0F))
                    DropdownMenu(
                        modifier = Modifier.testTag("${route.route}-Menu"),
                        expanded = expanded,
                        onDismissRequest = { expanded = false }) {
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