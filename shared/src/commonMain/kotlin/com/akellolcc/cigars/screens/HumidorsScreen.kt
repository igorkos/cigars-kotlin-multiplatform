package com.akellolcc.cigars.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.akellolcc.cigars.common.theme.DefaultTheme
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.mvvm.HumidorsViewModel
import com.akellolcc.cigars.navigation.ITabItem
import com.akellolcc.cigars.navigation.NavRoute
import com.akellolcc.cigars.theme.Images
import com.akellolcc.cigars.theme.Localize
import com.akellolcc.cigars.theme.imagePainter
import com.akellolcc.cigars.ui.BackHandler
import kotlin.jvm.Transient

class HumidorsScreen(override val route: NavRoute,) : ITabItem {

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
        Navigator(HumidorsListScreen(route.updateTabState))
    }
}
class HumidorsListScreen( val updateTabState: ((Boolean) -> Unit)? = null) : Screen {

    @Transient
    private val viewModel = HumidorsViewModel()
    @Composable
    override fun Content() {

        BackHandler {}

        val humidors by viewModel.asState()

        Log.debug("Humidors: $humidors")

        DefaultTheme {
            Box(modifier = with(Modifier){
                fillMaxSize()
            }) {
                Text(text = "Humidors screen", modifier = with(Modifier) {
                    align(Alignment.Center)
                })
            }
        }
    }
}



