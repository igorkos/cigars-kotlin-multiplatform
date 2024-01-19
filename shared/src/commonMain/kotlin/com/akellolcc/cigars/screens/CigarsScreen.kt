package com.akellolcc.cigars.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.akellolcc.cigars.common.theme.DefaultTheme
import com.akellolcc.cigars.databases.Database
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.navigation.NavRoute
import com.akellolcc.cigars.navigation.TabItem
import com.akellolcc.cigars.theme.Images
import com.akellolcc.cigars.theme.LocalBackgroundTheme
import com.akellolcc.cigars.theme.Localize
import com.akellolcc.cigars.theme.MaterialColors
import com.akellolcc.cigars.theme.imagePainter
import com.akellolcc.cigars.theme.materialColor

class CigarsScreen(override val route: NavRoute) : TabItem, Tab {
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
        val cigars by Database.getInstance().cigars()
        Log.debug("Cigars : $cigars")
        DefaultTheme {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                containerColor = materialColor(MaterialColors.color_transparent),
                topBar = {
                     TopAppBar(title = { Text(text = Localize.title_cigars) })
                },
                content = {
                    if (cigars.isNotEmpty()) {
                        LazyColumn(
                            verticalArrangement = Arrangement.Top,
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                            modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 48.dp, bottom = 80.dp)
                        )
                        {
                            items(cigars) {
                                ListRow(it)
                            }
                        }
                    }
                }
            )
        }
    }
    @Composable
    fun ListRow(cigar: Cigar) {
        Card(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = materialColor(MaterialColors.color_transparent),
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 0.dp)

            ) {
                Text(
                    maxLines = 2,
                    minLines = 2,
                    text = cigar.name,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Normal,
                )
            }
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    maxLines = 1,
                    text = cigar.cigar ?: "",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal,
                )
                Text(
                    maxLines = 1,
                    text = "10",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Normal,
                )
            }
        }
    }

}
