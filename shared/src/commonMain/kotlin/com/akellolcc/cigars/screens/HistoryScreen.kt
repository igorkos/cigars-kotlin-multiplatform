package com.akellolcc.cigars.screens

import TextStyled
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import com.akellolcc.cigars.databases.extensions.History
import com.akellolcc.cigars.databases.extensions.HistoryType
import com.akellolcc.cigars.mvvm.HistoryScreenViewModel
import com.akellolcc.cigars.navigation.NavRoute
import com.akellolcc.cigars.theme.Localize
import com.akellolcc.cigars.theme.MaterialColors
import com.akellolcc.cigars.theme.TextStyles
import com.akellolcc.cigars.theme.loadIcon
import com.akellolcc.cigars.theme.materialColor
import com.akellolcc.cigars.ui.formatDate

abstract class HistoryScreen(override val route: NavRoute) :
    BaseTabListScreen<HistoryScreenViewModel.CigarsAction, History>(route) {

    override lateinit var viewModel: HistoryScreenViewModel
    override fun handleAction(
        event: HistoryScreenViewModel.CigarsAction,
        navigator: Navigator?
    ) {
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun topTabBar(scrollBehavior: TopAppBarScrollBehavior, navigator: Navigator?) {
        val topColors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = materialColor(MaterialColors.color_transparent),
            navigationIconContentColor = materialColor(MaterialColors.color_onPrimaryContainer),
            actionIconContentColor = materialColor(MaterialColors.color_onPrimaryContainer),
        )
        TopAppBar(
            title = {
                TextStyled(
                    viewModel.name,
                    TextStyles.ScreenTitle,
                )
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
            })
    }

    @Composable
    override fun EntityListRow(entity: History, modifier: Modifier) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = materialColor(MaterialColors.color_primaryContainer),
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                loadIcon(HistoryType.icon(entity.type), Size(32.dp.value, 32.dp.value))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp)

                ) {
                    Column {
                        TextStyled(
                            text = entity.date.formatDate(),
                            style = TextStyles.Subhead,
                            keepHeight = true
                        )
                        TextStyled(
                            text = viewModel.entityName(entity),
                            style = TextStyles.Subhead,
                            keepHeight = true
                        )
                        Row(
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            TextStyled(
                                text = if (entity.cigarId < 0) Localize.history_humidor_added(entity.type) else Localize.history_transaction_desc(
                                    entity.type,
                                    entity.count
                                ),
                                style = TextStyles.Subhead
                            )
                            if (entity.price != null) {
                                TextStyled(
                                    text = Localize.history_transaction_price(entity.price!!),
                                    style = TextStyles.Subhead
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}
