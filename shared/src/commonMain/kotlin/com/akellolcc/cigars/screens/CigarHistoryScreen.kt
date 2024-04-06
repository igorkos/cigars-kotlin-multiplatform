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
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.History
import com.akellolcc.cigars.databases.extensions.HistoryType
import com.akellolcc.cigars.mvvm.CigarHistoryScreenViewModel
import com.akellolcc.cigars.navigation.NavRoute
import com.akellolcc.cigars.theme.Images
import com.akellolcc.cigars.theme.Localize
import com.akellolcc.cigars.theme.MaterialColors
import com.akellolcc.cigars.theme.TextStyles
import com.akellolcc.cigars.theme.loadIcon
import com.akellolcc.cigars.theme.materialColor
import com.akellolcc.cigars.ui.formatDate
import kotlin.jvm.Transient

class CigarHistoryScreen(override val route: NavRoute) :
    BaseTabListScree<CigarHistoryScreenViewModel.CigarsAction, History>(route) {

    @Transient
    override val viewModel = CigarHistoryScreenViewModel(route.data as Cigar)

    override fun handleAction(
        event: CigarHistoryScreenViewModel.CigarsAction,
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
        MediumTopAppBar(
            title = { TextStyled(text = viewModel.cigar.name, style = TextStyles.ScreenTitle) },
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
            })
    }

    @Composable
    override fun EntityListRow(entity: History) {
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
                loadIcon(
                    if (entity.type == HistoryType.Addition) Images.icon_arrow_right else Images.icon_arrow_left,
                    Size(32.dp.value, 32.dp.value)
                )
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
                            text = viewModel.humidorName(entity.humidorId),
                            style = TextStyles.Subhead,
                            keepHeight = true
                        )
                        Row(
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            TextStyled(
                                text = Localize.history_transaction_desc(entity.type, entity.count),
                                style = TextStyles.Subhead
                            )
                            TextStyled(
                                text = Localize.history_transaction_price(entity.price),
                                style = TextStyles.Subhead
                            )
                        }
                    }
                }
            }
        }
    }

}
