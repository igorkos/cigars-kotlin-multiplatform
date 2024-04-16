package com.akellolcc.cigars.screens

import TextStyled
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import cafe.adriel.voyager.navigator.Navigator
import com.akellolcc.cigars.components.CigarListRow
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.CigarSortingFields
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.mvvm.CigarsScreenViewModel
import com.akellolcc.cigars.mvvm.MainScreenViewModel
import com.akellolcc.cigars.navigation.CigarsDetailsRoute
import com.akellolcc.cigars.navigation.ITabItem
import com.akellolcc.cigars.navigation.NavRoute
import com.akellolcc.cigars.theme.Images
import com.akellolcc.cigars.theme.TextStyles
import com.akellolcc.cigars.theme.loadIcon
import kotlin.jvm.Transient

class CigarsScreen(
    override val route: NavRoute
) : ITabItem {
    private val screen = CigarsListScreen(route)

    @Composable
    override fun Content() {
        Navigator(screen)
    }
}

open class CigarsListScreen(override val route: NavRoute) :
    BaseTabListScreen<CigarsScreenViewModel.CigarsAction, Cigar>(route) {

    @Transient
    override val viewModel = CigarsScreenViewModel()

    @Composable
    override fun RightActionMenu(onDismiss: () -> Unit) {
        CigarSortingFields.enumValues().map {
            DropdownMenuItem(
                leadingIcon = {
                    loadIcon(Images.icon_menu_sort, Size(24.0F, 24.0F))
                },
                text = {
                    TextStyled(
                        it.second,
                        TextStyles.Subhead
                    )
                },
                onClick = {
                    viewModel.sorting(it.first.value)
                    onDismiss()
                }
            )
        }
    }

    override fun handleAction(event: CigarsScreenViewModel.CigarsAction, navigator: Navigator?) {
        val mainModel = route.sharedViewModel as MainScreenViewModel
        when (event) {
            is CigarsScreenViewModel.CigarsAction.RouteToCigar -> {
                Log.debug("Selected cigar ${event.cigar.rowid}")
                mainModel.isTabsVisible = false
                navigator?.push(CigarDetailsScreen(CigarsDetailsRoute.apply {
                    data = event.cigar
                    sharedViewModel = mainModel
                }))
            }

            is CigarsScreenViewModel.CigarsAction.ShowError -> TODO()
        }
    }

    @Composable
    override fun EntityListRow(entity: Cigar, modifier: Modifier) {
        CigarListRow(entity, modifier)
    }
}
