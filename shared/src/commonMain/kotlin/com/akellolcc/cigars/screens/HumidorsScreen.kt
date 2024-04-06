package com.akellolcc.cigars.screens

import TextStyled
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.akellolcc.cigars.databases.extensions.Humidor
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.mvvm.HumidorsViewModel
import com.akellolcc.cigars.mvvm.MainScreenViewModel
import com.akellolcc.cigars.navigation.HumidorCigarsRoute
import com.akellolcc.cigars.navigation.ITabItem
import com.akellolcc.cigars.navigation.NavRoute
import com.akellolcc.cigars.theme.Images
import com.akellolcc.cigars.theme.Localize
import com.akellolcc.cigars.theme.MaterialColors
import com.akellolcc.cigars.theme.TextStyles
import com.akellolcc.cigars.theme.imagePainter
import com.akellolcc.cigars.theme.materialColor

class HumidorsScreen(override val route: NavRoute) : ITabItem {

    override val options: TabOptions
        @Composable
        get() {
            val title = Localize.title_humidors
            val icon = imagePainter(Images.tab_icon_humidors)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        navigator?.push(HumidorsListScreen(route))
        //Navigator(HumidorsListScreen(route.updateTabState))
    }
}

class HumidorsListScreen(route: NavRoute) :
    BaseTabListScree<HumidorsViewModel.Action, Humidor>(route) {

    override val viewModel = HumidorsViewModel()

    @Composable
    override fun EntityListRow(entity: Humidor) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = materialColor(MaterialColors.color_primaryContainer),
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = {
                    viewModel.humidorSelected(entity)
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
                    text = entity.name,
                    style = TextStyles.Headline,
                    keepHeight = true
                )
            }
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                TextStyled(
                    text = Localize.humidor_cigars(
                        entity.count ?: 0,
                        (entity.holds ?: 0) - (entity.count ?: 0)
                    ),
                    style = TextStyles.Subhead
                )
            }
        }
    }

    override fun handleAction(event: HumidorsViewModel.Action, navigator: Navigator?) {
        val mainModel = route.sharedViewModel as MainScreenViewModel
        when (event) {
            is HumidorsViewModel.Action.RouteToHumidor -> {
                Log.debug("Selected humidor ${event.humidor.rowid}")
                mainModel.isTabsVisible = false
                navigator?.push(HumidorCigarsScreen(HumidorCigarsRoute.apply {
                    this.data = event.humidor
                    this.sharedViewModel = mainModel
                }))
            }

            is HumidorsViewModel.Action.ShowError -> TODO()
        }
    }

}



