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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.mvvm.CigarsScreenViewModel
import com.akellolcc.cigars.mvvm.MainScreenViewModel
import com.akellolcc.cigars.navigation.CigarsDetailsRoute
import com.akellolcc.cigars.navigation.ITabItem
import com.akellolcc.cigars.navigation.NavRoute
import com.akellolcc.cigars.theme.MaterialColors
import com.akellolcc.cigars.theme.TextStyles
import com.akellolcc.cigars.theme.materialColor
import kotlin.jvm.Transient

class CigarsScreen(
    override val route: NavRoute
) : ITabItem {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        navigator?.push(CigarsListScreen(route))
        //Navigator(CigarsListScreen(route))
    }
}

open class CigarsListScreen(override val route: NavRoute) :
    BaseTabListScree<CigarsScreenViewModel.CigarsAction, Cigar>(route) {

    @Transient
    override val viewModel = CigarsScreenViewModel()

    override fun handleAction(event: CigarsScreenViewModel.CigarsAction, navigator: Navigator?) {
        val mainModel = route.sharedViewModel as MainScreenViewModel
        when (event) {
            is CigarsScreenViewModel.CigarsAction.RouteToCigar -> {
                Log.debug("Selected cigar ${event.cigar.rowid}")
                mainModel.isTabsVisible = false
                //route.updateTabState?.invoke(false)
                navigator?.push(CigarDetailsScreen(CigarsDetailsRoute.apply {
                    data = event.cigar
                    sharedViewModel = mainModel
                }))
            }

            is CigarsScreenViewModel.CigarsAction.ShowError -> TODO()
        }
    }

    @Composable
    override fun EntityListRow(entity: Cigar) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = materialColor(MaterialColors.color_primaryContainer),
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = {
                    viewModel.cigarSelected(entity)
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
                    text = entity.cigar,
                    style = TextStyles.Subhead
                )
                TextStyled(
                    text = entity.length,
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
