package com.akellolcc.cigars.screens

import TextStyled
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.centerAlignedTopAppBarColors
import androidx.compose.material3.TopAppBarDefaults.exitUntilCollapsedScrollBehavior
import androidx.compose.material3.TopAppBarDefaults.largeTopAppBarColors
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.akellolcc.cigars.common.theme.DefaultTheme
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.mvvm.CigarsScreenViewModel
import com.akellolcc.cigars.navigation.NavRoute
import com.akellolcc.cigars.navigation.TabItem
import com.akellolcc.cigars.theme.Images
import com.akellolcc.cigars.theme.Localize
import com.akellolcc.cigars.theme.MaterialColors
import com.akellolcc.cigars.theme.TextStyles
import com.akellolcc.cigars.theme.imagePainter
import com.akellolcc.cigars.theme.loadIcon
import com.akellolcc.cigars.theme.materialColor
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import dev.icerock.moko.mvvm.flow.compose.observeAsActions

class CigarsScreen(override val route: NavRoute) : TabItem, Tab {

    private lateinit var viewModel: CigarsScreenViewModel

    override val options: TabOptions
        @Composable
        get() {
            val title = Localize.title_cigars
            val icon = imagePainter(Images.tab_icon_cigars)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        viewModel = getViewModel(key = route, factory = viewModelFactory { CigarsScreenViewModel() })
        val cigars by viewModel.cigars.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.fetchCigars()
        }

        viewModel.actions.observeAsActions {
            when (it) {
                is CigarsScreenViewModel.Action.RouteToCigar -> {
                    Log.debug("Selected cigar ${it.cigar.id}")
                    //navigator.push(postScreen)
                }

                is CigarsScreenViewModel.Action.ShowError -> TODO()
            }
        }

        val scrollBehavior = exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

        Log.debug("Cigars : $cigars")
        DefaultTheme {
            Scaffold(
                modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    CenterAlignedTopAppBar(
                         navigationIcon = { loadIcon(Images.icon_menu_dots, Size(24.0F, 24.0F), materialColor(MaterialColors.color_onSurface)) },
                         actions = {loadIcon(Images.icon_menu, Size(24.0F, 24.0F), materialColor(MaterialColors.color_onSurface))},
                         title = { TextStyled(text = Localize.title_cigars, style = TextStyles.ScreenTitle) },
                         scrollBehavior = scrollBehavior)
                },
                content = {
                    if (cigars.isNotEmpty()) {
                        LazyColumn(
                            verticalArrangement = Arrangement.Top,
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                            modifier = Modifier
                            .fillMaxSize()
                           // .padding(top = 64.dp, bottom = 80.dp)
                        )
                        {
                            item { Box( modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .background(Color.Transparent)
                            ) }
                            items(cigars) {
                                ListRow(it)
                                Spacer( modifier = Modifier
                                    .fillMaxWidth()
                                    .height(4.dp)
                                    .background(Color.Transparent)
                                )
                            }
                            item { Box( modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .background(Color.Transparent)
                            ) }
                        }
                    }
                }
            )
        }
    }
    @Composable
    fun ListRow(cigar: Cigar) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = materialColor(MaterialColors.color_onPrimary),
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .clickable(onClick = {
                    viewModel.cigarSelected(cigar)
                })
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, start = 12.dp, end = 12.dp, bottom = 0.dp)

            ) {
                TextStyled(
                    maxLines = 2,
                    minLines = 2,
                    text = cigar.name,
                    style = TextStyles.Headline,
                )
            }
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                   .padding(16.dp)
            ) {
                TextStyled(
                    text = cigar.cigar,
                    style = TextStyles.Subhead
                )
                TextStyled(
                    text = cigar.length,
                    style = TextStyles.Subhead
                )
                TextStyled(
                    text = "10",
                    style = TextStyles.Subhead
                )
            }
        }
    }

}
