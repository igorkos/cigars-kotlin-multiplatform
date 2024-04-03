package com.akellolcc.cigars.screens

import TextStyled
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults.centerAlignedTopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.akellolcc.cigars.common.theme.DefaultTheme
import com.akellolcc.cigars.components.PagedCarousel
import com.akellolcc.cigars.components.ValueCard
import com.akellolcc.cigars.components.ValuesCard
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.Humidor
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.mvvm.CigarsDetailsScreenViewModel
import com.akellolcc.cigars.navigation.ITabItem
import com.akellolcc.cigars.navigation.ImagesViewRoute
import com.akellolcc.cigars.navigation.NavRoute
import com.akellolcc.cigars.theme.Images
import com.akellolcc.cigars.theme.Localize
import com.akellolcc.cigars.theme.MaterialColors
import com.akellolcc.cigars.theme.TextStyles
import com.akellolcc.cigars.theme.imagePainter
import com.akellolcc.cigars.theme.loadIcon
import com.akellolcc.cigars.theme.materialColor
import kotlin.jvm.Transient

class CigarDetailsScreen(override val route: NavRoute) : ITabItem {
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

    @Transient
    private val viewModel = CigarsDetailsScreenViewModel(route.data as Cigar)

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        var isEdit by remember { mutableStateOf(false) }
        var notesHeight by remember { mutableStateOf(0) }
        var update by remember { mutableStateOf(false) }

        val images by viewModel.imagesAsState()
        val humidors by viewModel.humidorsAsState()

        Log.debug("Images: ${images.size} : ${viewModel.loading}  ")
        viewModel.observeEvents {
            when (it) {
                is CigarsDetailsScreenViewModel.CigarsDetailsAction.ShowError -> TODO()
                is CigarsDetailsScreenViewModel.CigarsDetailsAction.AddToHumidor -> TODO()
                is CigarsDetailsScreenViewModel.CigarsDetailsAction.OpenHumidor -> TODO()
                is CigarsDetailsScreenViewModel.CigarsDetailsAction.RateCigar -> TODO()
            }
        }

        val topColors = centerAlignedTopAppBarColors(containerColor = materialColor(MaterialColors.color_transparent),
            navigationIconContentColor = materialColor(MaterialColors.color_onPrimaryContainer),
            actionIconContentColor = materialColor(MaterialColors.color_onPrimaryContainer),)
        val navigator = LocalNavigator.currentOrThrow
        DefaultTheme {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    CenterAlignedTopAppBar(
                        colors = topColors,
                        navigationIcon = {
                            IconButton(onClick = {
                                route.updateTabState?.invoke(true)
                                navigator.pop()
                            }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = null
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = { isEdit = !isEdit }) {
                                loadIcon(Images.icon_menu_edit, Size(24.0F, 24.0F))
                            }
                        },
                        title = {})
                },
            ) {
                CompositionLocalProvider(
                    LocalContentColor provides materialColor(MaterialColors.color_onPrimaryContainer)
                )
                {
                    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                        Column(
                            horizontalAlignment = Alignment.Start,
                            modifier = Modifier.padding(bottom = 10.dp).aspectRatio(ratio = 1.8f)
                        ) {
                            PagedCarousel(
                                images,
                                loading = viewModel.loading,
                            ){
                                val data = route.data as Cigar
                                navigator.push(ImagesViewScreen(ImagesViewRoute.apply {
                                    this.data = Pair(data,it)
                                }))
                            }
                        }
                        Column(modifier = with(Modifier) {
                            fillMaxSize()
                                .padding(
                                    PaddingValues(
                                        16.dp,
                                        0.dp,
                                        16.dp,
                                        it.calculateBottomPadding() + 16.dp
                                    )
                                )
                        }) {
                            Column {
                                TextStyled(
                                    viewModel.name,
                                    TextStyles.Headline,
                                    editable = isEdit,
                                    maxLines = 2,
                                    modifier = Modifier.padding(end = 4.dp),
                                    keepHeight = false
                                )
                                TextStyled(
                                    viewModel.brand,
                                    TextStyles.Subhead,
                                    editable = isEdit,
                                    modifier = Modifier.padding(end = 4.dp)
                                )
                                TextStyled(viewModel.country, TextStyles.Subhead, editable = isEdit)
                            }
                            Row(modifier = Modifier.padding(top = 10.dp)) {
                                Column(
                                    horizontalAlignment = Alignment.Start,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    ValuesCard(
                                        label = "Cigar"
                                    ) {
                                        ValueCard("Shape", viewModel.shape)
                                        ValueCard("Length", viewModel.length)
                                        ValueCard("Gauge", viewModel.gauge.toString())
                                    }
                                }
                            }
                            Column {
                                ValuesCard(
                                    label = "Tobacco",
                                    vertical = true
                                ) {
                                    TextStyled(
                                        viewModel.wrapper,
                                        TextStyles.Subhead,
                                        labelStyle = TextStyles.Subhead,
                                        label = "Wrapper",
                                        editable = isEdit,
                                        modifier = Modifier.padding(bottom = 10.dp)
                                    )
                                    TextStyled(
                                        viewModel.binder,
                                        TextStyles.Subhead,
                                        labelStyle = TextStyles.Subhead,
                                        label = "Binder",
                                        editable = isEdit,
                                        modifier = Modifier.padding(bottom = 10.dp)
                                    )
                                    TextStyled(
                                        viewModel.filler,
                                        TextStyles.Subhead,
                                        labelStyle = TextStyles.Subhead,
                                        label = "Filler",
                                        editable = isEdit,
                                        modifier = Modifier.padding(bottom = 10.dp)
                                    )
                                    TextStyled(
                                        viewModel.strength.toString(),
                                        TextStyles.Subhead,
                                        labelStyle = TextStyles.Subhead,
                                        label = "Strength",
                                        editable = isEdit,
                                        modifier = Modifier.padding(bottom = 10.dp)
                                    )
                                }
                            }
                            Column {
                                ValuesCard(
                                    label = "Ratings",
                                ) {
                                    ValueCard("Rating", "${viewModel.rating}")
                                    ValueCard("My Rating", "${viewModel.myrating}"){
                                        viewModel.rate()
                                    }
                                    IconButton(onClick = {
                                        viewModel.favorite()
                                        update = !update
                                    }) {
                                        val icon = if(viewModel.favorites == true) Images.icon_star_filled else Images.icon_star_empty
                                        loadIcon(icon, Size(64.0F, 64.0F))
                                    }
                                }
                            }
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth().height(((humidors.count() + 1) * 48).dp)
                            ) {
                                ValuesCard(
                                    label = "Humidors",
                                    vertical = true
                                ) {
                                    LazyColumn(
                                        verticalArrangement = Arrangement.Top,
                                        modifier = Modifier
                                            .fillMaxSize()
                                    )
                                    {
                                        items(humidors, key = { item -> item.rowid }) {
                                            HumidorListRow(it, viewModel)
                                            Spacer( modifier = Modifier
                                                .fillMaxWidth()
                                                .height(4.dp)
                                                .background(Color.Transparent)
                                            )
                                        }
                                    }
                                }
                            }
                            Column(
                                modifier = Modifier
                                    .padding(bottom = 10.dp, top = 10.dp)
                                    .onSizeChanged {
                                        notesHeight = it.height
                                    }
                            ) {
                                    TextStyled(
                                        viewModel.notes,
                                        TextStyles.Subhead,
                                        //label = "Notes",
                                        editable = isEdit,
                                       // maxLines = 5,
                                       // maxHeight = notesHeight
                                    )
                            }
                            Column(
                                modifier = Modifier.padding(bottom = 10.dp)
                            ) {
                                TextStyled(
                                    viewModel.link,
                                    TextStyles.Subhead,
                                    label = "Link",
                                    labelStyle = TextStyles.Subhead,
                                    editable = isEdit,
                                    maxLines = 1
                                )
                            }
                        }

                    }
                }
            }
        }
    }

    @Composable
    fun HumidorListRow(item: Humidor, viewModel: CigarsDetailsScreenViewModel) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = materialColor(MaterialColors.color_transparent),
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = {
                    viewModel.openHumidor(item)
                })
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp, start = 12.dp, end = 12.dp, bottom = 6.dp)

            ) {
                TextStyled(
                    item.name,
                    TextStyles.Subhead,
                )
                ValueCard(null, "${item.count}"){
                    viewModel.addToHumidor(item)
                }
            }
        }
    }
}