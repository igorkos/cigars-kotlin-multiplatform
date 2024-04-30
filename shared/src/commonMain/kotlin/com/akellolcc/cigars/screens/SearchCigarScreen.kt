/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/29/24, 1:31 PM
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

package com.akellolcc.cigars.screens

import TextStyled
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.navigator.Navigator
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.mvvm.SearchCigarScreenViewModel
import com.akellolcc.cigars.mvvm.createViewModel
import com.akellolcc.cigars.screens.navigation.ITabItem
import com.akellolcc.cigars.screens.navigation.NavRoute
import com.akellolcc.cigars.screens.search.CigarSearchParameters
import com.akellolcc.cigars.screens.search.SearchComponent
import com.akellolcc.cigars.screens.search.SearchParameterAction
import com.akellolcc.cigars.theme.Images
import com.akellolcc.cigars.theme.Localize
import com.akellolcc.cigars.theme.MaterialColors
import com.akellolcc.cigars.theme.TextStyles
import com.akellolcc.cigars.theme.loadIcon
import com.akellolcc.cigars.theme.materialColor
import kotlinx.coroutines.flow.flow
import kotlin.jvm.Transient

class SearchScreen(
    override val route: NavRoute
) : ITabItem<SearchCigarScreenViewModel> {
    @Transient
    override lateinit var viewModel: SearchCigarScreenViewModel

    @Composable
    override fun Content() {
        viewModel =
            rememberScreenModel { createViewModel(SearchCigarScreenViewModel::class, route.data) }
        Navigator(SearchCigarScreen(route, viewModel))
    }
}

class SearchCigarScreen(
    route: NavRoute,
    @Transient
    override var viewModel: SearchCigarScreenViewModel
) :
    BaseTabListScreen<SearchCigarScreenViewModel.Actions, Cigar, SearchCigarScreenViewModel>(route) {

    @Composable
    override fun RightActionMenu(onDismiss: () -> Unit) {
        viewModel.sortingFields?.map {
            val selected = viewModel.sorting == it
            DropdownMenuItem(
                leadingIcon = {
                    loadIcon(if (selected) Images.icon_menu_checkmark else Images.icon_menu_sort, Size(24.0F, 24.0F))
                },
                text = {
                    TextStyled(
                        it.label,
                        TextStyles.Subhead
                    )
                },
                onClick = {
                    viewModel.sorting = it
                    onDismiss()
                }
            )
        }
    }

    @Composable
    override fun ContentHeader(modifier: Modifier) {
        val fields = remember { CigarSearchParameters() }
        LaunchedEffect(fields.selected) {
            viewModel.searchFieldsChanged(fields.selected)
        }
        SearchComponent(
            modifier = modifier,
            loading = viewModel.loading,
            fields = fields,
        ) { action, b ->
            flow {
                when (action) {
                    SearchParameterAction.Completed -> {
                        viewModel.reload()
                    }

                    else -> {}
                }
                emit(true)
            }
        }
    }

    @Composable
    override fun ListFooter(modifier: Modifier) {
        if (viewModel.loading) {
            Box(
                modifier = modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.width(32.dp),
                )
            }
        }
    }


    @Composable
    override fun EntityListRow(entity: Cigar, modifier: Modifier) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = materialColor(MaterialColors.color_primaryContainer)
            ),
            modifier = modifier.fillMaxWidth()
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
                    label = Localize.cigar_details_length,
                    labelSuffix = "",
                    text = entity.length,
                    style = TextStyles.Subhead,
                )
                TextStyled(
                    label = Localize.cigar_details_gauge,
                    labelSuffix = "",
                    text = "${entity.gauge}",
                    style = TextStyles.Subhead,
                )
            }
        }
    }

    override fun handleAction(event: SearchCigarScreenViewModel.Actions, navigator: Navigator?) {
        TODO("Not yet implemented")
    }
}
