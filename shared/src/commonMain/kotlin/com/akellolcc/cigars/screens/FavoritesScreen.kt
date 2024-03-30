package com.akellolcc.cigars.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.akellolcc.cigars.common.theme.DefaultTheme
import com.akellolcc.cigars.navigation.ITabItem
import com.akellolcc.cigars.navigation.NavRoute
import com.akellolcc.cigars.theme.Images
import com.akellolcc.cigars.theme.Localize
import com.akellolcc.cigars.theme.imagePainter

class FavoritesScreen(override val route: NavRoute) : ITabItem {
    override val options: TabOptions
        @Composable
        get() {
            val title = Localize.title_favorites
            val icon = imagePainter(Images.tab_icon_favorites)

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
        DefaultTheme {
            Box(modifier = with(Modifier){
                fillMaxSize()
            }) {
                Text(text = "Camera screen", modifier = with(Modifier) {
                    align(Alignment.Center)
                })
            }
        }
    }
}
