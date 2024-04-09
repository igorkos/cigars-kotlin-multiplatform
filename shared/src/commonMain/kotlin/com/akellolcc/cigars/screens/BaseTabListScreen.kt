package com.akellolcc.cigars.screens

import TextStyled
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import com.akellolcc.cigars.common.theme.DefaultTheme
import com.akellolcc.cigars.databases.extensions.BaseEntity
import com.akellolcc.cigars.mvvm.BaseListViewModel
import com.akellolcc.cigars.navigation.ITabItem
import com.akellolcc.cigars.navigation.NavRoute
import com.akellolcc.cigars.theme.Images
import com.akellolcc.cigars.theme.MaterialColors
import com.akellolcc.cigars.theme.TextStyles
import com.akellolcc.cigars.theme.loadIcon
import com.akellolcc.cigars.theme.materialColor

abstract class BaseTabListScree<A, E : BaseEntity>(override val route: NavRoute) : ITabItem {

    abstract fun handleAction(event: A, navigator: Navigator?)

    @Composable
    abstract fun EntityListRow(entity: E)

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    open fun topTabBar(scrollBehavior: TopAppBarScrollBehavior, navigator: Navigator?) {
        CenterAlignedTopAppBar(
            navigationIcon = { loadIcon(Images.icon_menu_dots, Size(24.0F, 24.0F)) },
            actions = { loadIcon(Images.icon_menu, Size(24.0F, 24.0F)) },
            title = { TextStyled(text = route.title, style = TextStyles.ScreenTitle) },
            scrollBehavior = scrollBehavior
        )
    }

    abstract val viewModel: BaseListViewModel<E, A>

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val entities by viewModel.asState()

        LaunchedEffect(navigator) {
            route.sharedViewModel?.isTabsVisible = route.isTabsVisible
        }

        viewModel.observeEvents {
            handleAction(it, navigator)
        }

        val scrollBehavior =
            TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

        //Log.debug("Cigars : $cigars")
        DefaultTheme {
            Scaffold(
                modifier = Modifier.fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = { topTabBar(scrollBehavior, navigator) },
                content = {
                    if (viewModel.loading) {
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
                        if (entities.isNotEmpty()) {
                            LazyColumn(
                                verticalArrangement = Arrangement.Top,
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                modifier = Modifier
                                    .fillMaxSize()
                            )
                            {
                                val top = it.calculateTopPadding() + 16.dp
                                val bottom = it.calculateBottomPadding() + 16.dp
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(top)
                                            .background(Color.Transparent)
                                    )
                                }
                                items(entities, key = { item -> item.key }) {
                                    EntityListRow(it)
                                    Spacer(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(4.dp)
                                            .background(Color.Transparent)
                                    )
                                }
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(bottom)
                                            .background(Color.Transparent)
                                    )
                                }
                            }
                        }
                    }
                }
            )
        }
    }
}