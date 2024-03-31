package com.akellolcc.cigars.screens

import TextStyled
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults.centerAlignedTopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.akellolcc.cigars.camera.PermissionCallback
import com.akellolcc.cigars.camera.PermissionStatus
import com.akellolcc.cigars.camera.PermissionType
import com.akellolcc.cigars.camera.createPermissionsManager
import com.akellolcc.cigars.camera.rememberCameraManager
import com.akellolcc.cigars.camera.rememberGalleryManager
import com.akellolcc.cigars.common.theme.DefaultTheme
import com.akellolcc.cigars.components.PagedCarousel
import com.akellolcc.cigars.components.ValueCard
import com.akellolcc.cigars.components.ValuesCard
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.mvvm.CigarsDetailsScreenViewModel
import com.akellolcc.cigars.navigation.CigarsDetailsRoute
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
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import kotlinx.coroutines.launch

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

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
    @Composable
    override fun Content() {
        val cigar: Cigar = route.data as Cigar
        var isEdit by remember { mutableStateOf(false) }
        var notesHeight by remember { mutableStateOf(0) }

        val viewModel = getViewModel(
            key = "CigarDetailsScreen",
            factory = viewModelFactory { CigarsDetailsScreenViewModel(cigar) })

        val images by viewModel.images.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.fetchImages()
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
                       // modifier = Modifier.background(materialColor(MaterialColors.color_error)),
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
                            modifier = Modifier.padding(bottom = 10.dp)
                        ) {
                            PagedCarousel(
                                images,
                                modifier = Modifier.aspectRatio(ratio = 1.8f)
                            ){
                                navigator.push(ImagesViewScreen(ImagesViewRoute.apply {
                                    this.data = Pair(viewModel.cigar,it)
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
                                    viewModel.cigar.name,
                                    TextStyles.Headline,
                                    editable = isEdit,
                                    maxLines = 2,
                                    modifier = Modifier.padding(end = 4.dp),
                                    keepHeight = false
                                )
                                TextStyled(
                                    viewModel.cigar.brand,
                                    TextStyles.Subhead,
                                    editable = isEdit,
                                    modifier = Modifier.padding(end = 4.dp)
                                )
                                TextStyled(viewModel.cigar.country, TextStyles.Subhead, editable = isEdit)
                            }
                            Row(modifier = Modifier.padding(top = 10.dp)) {
                                Column(
                                    horizontalAlignment = Alignment.Start,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    ValuesCard(
                                        label = "Cigar"
                                    ) {
                                        ValueCard("Shape", viewModel.cigar.cigar)
                                        ValueCard("Length", viewModel.cigar.length)
                                        ValueCard("Gauge", viewModel.cigar.gauge.toString())
                                    }
                                }
                            }
                            Column {
                                ValuesCard(
                                    label = "Tobacco",
                                    vertical = true
                                ) {
                                    TextStyled(
                                        viewModel.cigar.wrapper,
                                        TextStyles.Subhead,
                                        labelStyle = TextStyles.Subhead,
                                        label = "Wrapper",
                                        editable = isEdit,
                                        modifier = Modifier.padding(bottom = 10.dp)
                                    )
                                    TextStyled(
                                        viewModel.cigar.binder,
                                        TextStyles.Subhead,
                                        labelStyle = TextStyles.Subhead,
                                        label = "Binder",
                                        editable = isEdit,
                                        modifier = Modifier.padding(bottom = 10.dp)
                                    )
                                    TextStyled(
                                        viewModel.cigar.filler,
                                        TextStyles.Subhead,
                                        labelStyle = TextStyles.Subhead,
                                        label = "Filler",
                                        editable = isEdit,
                                        modifier = Modifier.padding(bottom = 10.dp)
                                    )
                                    TextStyled(
                                        viewModel.cigar.strength.toString(),
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
                                    label = "Humidors",
                                    vertical = true
                                ) {
                                    
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
                                        viewModel.cigar.notes,
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
                                    viewModel.cigar.link,
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
}