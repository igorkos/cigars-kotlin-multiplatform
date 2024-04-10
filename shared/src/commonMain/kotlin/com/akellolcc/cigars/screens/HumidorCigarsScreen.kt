package com.akellolcc.cigars.screens

import TextStyled
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import com.akellolcc.cigars.components.CigarListRow
import com.akellolcc.cigars.databases.extensions.Humidor
import com.akellolcc.cigars.databases.extensions.HumidorCigar
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.mvvm.HumidorCigarsScreenViewModel
import com.akellolcc.cigars.mvvm.MainScreenViewModel
import com.akellolcc.cigars.navigation.CigarsDetailsRoute
import com.akellolcc.cigars.navigation.HumidorDetailsRoute
import com.akellolcc.cigars.navigation.HumidorHistoryRoute
import com.akellolcc.cigars.navigation.NavRoute
import com.akellolcc.cigars.theme.Images
import com.akellolcc.cigars.theme.Localize
import com.akellolcc.cigars.theme.MaterialColors
import com.akellolcc.cigars.theme.TextStyles
import com.akellolcc.cigars.theme.loadIcon
import com.akellolcc.cigars.theme.materialColor
import kotlin.jvm.Transient

class HumidorCigarsScreen(override val route: NavRoute) :
    BaseTabListScreen<HumidorCigarsScreenViewModel.CigarsAction, HumidorCigar>(route) {

    @Transient
    override val viewModel = HumidorCigarsScreenViewModel(route.data as Humidor)

    override fun handleAction(
        event: HumidorCigarsScreenViewModel.CigarsAction,
        navigator: Navigator?
    ) {
        val mainModel = route.sharedViewModel as MainScreenViewModel
        when (event) {
            is HumidorCigarsScreenViewModel.CigarsAction.RouteToCigar -> {
                Log.debug("Selected cigar ${event.cigar.rowid}")
                mainModel.isTabsVisible = false
                navigator?.push(CigarDetailsScreen(CigarsDetailsRoute.apply {
                    data = event.cigar
                    sharedViewModel = mainModel
                }))
            }

            is HumidorCigarsScreenViewModel.CigarsAction.ShowError -> TODO()
            is HumidorCigarsScreenViewModel.CigarsAction.AddCigar -> {
                mainModel.isTabsVisible = false
                navigator?.push(CigarDetailsScreen(CigarsDetailsRoute.apply {
                    this.data = null
                    sharedViewModel = mainModel
                }))
            }

            is HumidorCigarsScreenViewModel.CigarsAction.RouteToHumidorDetails -> {
                mainModel.isTabsVisible = false
                navigator?.push(
                    HumidorDetailsScreen(HumidorDetailsRoute
                        .apply {
                            this.data = event.humidor
                            sharedViewModel = mainModel
                        })
                )
            }

            is HumidorCigarsScreenViewModel.CigarsAction.OpenHistory -> {
                mainModel.isTabsVisible = false
                navigator?.push(
                    HumidorHistoryScreen(HumidorHistoryRoute
                        .apply {
                            this.data = viewModel.humidor
                            sharedViewModel = mainModel
                        })
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun topTabBar(scrollBehavior: TopAppBarScrollBehavior, navigator: Navigator?) {
        val topColors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = materialColor(MaterialColors.color_transparent),
            navigationIconContentColor = materialColor(MaterialColors.color_onPrimaryContainer),
            actionIconContentColor = materialColor(MaterialColors.color_onPrimaryContainer),
        )
        LargeTopAppBar(
            title = {
                Column {
                    TextStyled(text = viewModel.humidor.name, style = TextStyles.ScreenTitle)
                    TextStyled(
                        text = Localize.humidor_cigars(
                            viewModel.humidor.count ?: 0,
                            (viewModel.humidor.holds ?: 0) - (viewModel.humidor.count ?: 0)
                        ), style = TextStyles.Subhead
                    )
                }
            },
            colors = topColors,
            navigationIcon = {
                IconButton(onClick = {
                    navigator?.pop()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            },
            actions = {
                IconButton(onClick = { viewModel.openHistory() }) {
                    loadIcon(Images.icon_menu_history, Size(24.0F, 24.0F))
                }
                IconButton(onClick = { viewModel.humidorDetails() }) {
                    loadIcon(Images.icon_menu_info, Size(24.0F, 24.0F))
                }
                IconButton(onClick = { viewModel.addCigar() }) {
                    loadIcon(Images.icon_menu_plus, Size(24.0F, 24.0F))
                }
            })
    }

    @Composable
    override fun EntityListRow(entity: HumidorCigar, modifier: Modifier) {
        entity.cigar?.let {
            CigarListRow(it, modifier)
        }
    }

}
