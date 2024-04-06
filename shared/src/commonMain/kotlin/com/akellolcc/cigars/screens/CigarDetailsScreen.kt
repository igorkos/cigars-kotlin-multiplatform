package com.akellolcc.cigars.screens

import TextStyled
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.akellolcc.cigars.common.theme.DefaultTheme
import com.akellolcc.cigars.components.DefaultButton
import com.akellolcc.cigars.components.DialogButton
import com.akellolcc.cigars.components.PagedCarousel
import com.akellolcc.cigars.components.ValueCard
import com.akellolcc.cigars.components.ValuePicker
import com.akellolcc.cigars.components.ValuesCard
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.CigarShapes
import com.akellolcc.cigars.databases.extensions.CigarStrength
import com.akellolcc.cigars.databases.extensions.HumidorCigar
import com.akellolcc.cigars.databases.extensions.emptyCigar
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.mvvm.CigarsDetailsScreenViewModel
import com.akellolcc.cigars.mvvm.MainScreenViewModel
import com.akellolcc.cigars.navigation.CigarHistoryRoute
import com.akellolcc.cigars.navigation.HumidorCigarsRoute
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
    private val viewModel = CigarsDetailsScreenViewModel((route.data ?: emptyCigar) as Cigar)

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        var notesHeight by remember { mutableStateOf(0) }
        val navigator = LocalNavigator.currentOrThrow
        val mainModel = route.sharedViewModel as MainScreenViewModel
        Log.debug("Images: ${viewModel.images.size} : ${viewModel.loading}  ")
        viewModel.observeEvents {
            when (it) {
                is CigarsDetailsScreenViewModel.CigarsDetailsAction.ShowError -> TODO()
                is CigarsDetailsScreenViewModel.CigarsDetailsAction.AddToHumidor -> viewModel.humidorCigarsCount =
                    it.humidor

                is CigarsDetailsScreenViewModel.CigarsDetailsAction.OpenHumidor -> navigator.push(
                    HumidorCigarsScreen(
                        HumidorCigarsRoute.apply {
                            this.data = it.humidor
                            this.sharedViewModel = mainModel
                        })
                )

                is CigarsDetailsScreenViewModel.CigarsDetailsAction.RateCigar -> TODO()
                is CigarsDetailsScreenViewModel.CigarsDetailsAction.OnBackAction -> navigator.pop()
                is CigarsDetailsScreenViewModel.CigarsDetailsAction.OpenHistory -> navigator.push(
                    CigarHistoryScreen(
                        CigarHistoryRoute.apply {
                            this.data = it.cigar
                            this.sharedViewModel = mainModel
                        })
                )
            }
        }

        val topColors = centerAlignedTopAppBarColors(
            containerColor = materialColor(MaterialColors.color_transparent),
            navigationIconContentColor = materialColor(MaterialColors.color_onPrimaryContainer),
            actionIconContentColor = materialColor(MaterialColors.color_onPrimaryContainer),
        )

        DefaultTheme {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    CenterAlignedTopAppBar(
                        colors = topColors,
                        navigationIcon = {
                            if (!viewModel.editing) {
                                IconButton(onClick = {
                                    viewModel.sendEvent(
                                        CigarsDetailsScreenViewModel.CigarsDetailsAction.OnBackAction(
                                            0
                                        )
                                    )
                                }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = null
                                    )
                                }
                            }
                        },
                        actions = {
                            if (!viewModel.editing) {
                                IconButton(onClick = { viewModel.historyOpen() }) {
                                    loadIcon(Images.icon_menu_history, Size(24.0F, 24.0F))
                                }
                                IconButton(onClick = { viewModel.editing = !viewModel.editing }) {
                                    loadIcon(Images.icon_menu_edit, Size(24.0F, 24.0F))
                                }
                            }
                        },
                        title = {})
                },
                bottomBar = {
                    if (viewModel.editing) {
                        BottomAppBar(
                            actions = {
                                Box(modifier = Modifier.weight(1f))
                                DefaultButton(
                                    title = Localize.button_cancel,
                                    modifier = Modifier.padding(end = 10.dp).weight(2f),
                                    onClick = { viewModel.cancelEdit() })
                                DefaultButton(
                                    modifier = Modifier.weight(2f),
                                    enabled = viewModel.verifyFields(),
                                    title = Localize.button_save,
                                    onClick = { viewModel.saveEdit() })
                            }
                        )
                    }
                }
            ) {
                CompositionLocalProvider(
                    LocalContentColor provides materialColor(MaterialColors.color_onPrimaryContainer)
                )
                {
                    HumidorCigarsDialog {
                        viewModel.cancelEdit()
                    }
                    Column(
                        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
                    ) {
                        //Top Images view
                        if (!viewModel.editing) {
                            Column(
                                horizontalAlignment = Alignment.Start,
                                modifier = Modifier.padding(bottom = 10.dp)
                                    .aspectRatio(ratio = 1.8f)
                            ) {
                                PagedCarousel(
                                    viewModel.images,
                                    loading = viewModel.loading,
                                ) {
                                    val data = route.data as Cigar
                                    navigator.push(ImagesViewScreen(ImagesViewRoute.apply {
                                        this.data = Pair(data, it)
                                    }))
                                }
                            }
                        }
                        //Cigar details
                        Column(modifier = with(Modifier) {
                            fillMaxSize()
                                .padding(
                                    PaddingValues(
                                        16.dp,
                                        if (!viewModel.editing) 16.dp else it.calculateTopPadding(),
                                        16.dp,
                                        it.calculateBottomPadding() + 16.dp
                                    )
                                )
                        }) {
                            //Cigar Name,  Company and Country
                            Column {
                                TextStyled(
                                    viewModel.name,
                                    TextStyles.Headline,
                                    label = if (viewModel.editing) Localize.cigar_details_name else null,
                                    editable = viewModel.editing,
                                    maxLines = 2,
                                    modifier = Modifier.padding(bottom = 4.dp),
                                    keepHeight = false,
                                    onValueChange = {
                                        viewModel.name = it
                                    }
                                )
                                TextStyled(
                                    viewModel.brand,
                                    TextStyles.Subhead,
                                    label = if (viewModel.editing) Localize.cigar_details_company else null,
                                    editable = viewModel.editing,
                                    modifier = Modifier.padding(bottom = 4.dp),
                                    onValueChange = {
                                        viewModel.brand = it
                                    }
                                )
                                TextStyled(
                                    viewModel.country,
                                    TextStyles.Subhead,
                                    label = if (viewModel.editing) Localize.cigar_details_country else null,
                                    editable = viewModel.editing,
                                    onValueChange = {
                                        viewModel.country = it
                                    })
                            }
                            //Cigar Shape Length and Gauge
                            Column(
                                horizontalAlignment = Alignment.Start,
                                modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
                            ) {
                                ValuesCard(
                                    label = Localize.cigar_details_cigars,
                                    vertical = viewModel.editing
                                ) {
                                    if (!viewModel.editing) {
                                        ValueCard(Localize.cigar_details_shape, viewModel.shape)
                                        ValueCard(
                                            Localize.cigar_details_length,
                                            viewModel.length
                                        )
                                        ValueCard(
                                            Localize.cigar_details_gauge,
                                            viewModel.gauge.toString()
                                        )
                                    } else {
                                        ValuePicker(
                                            modifier = Modifier.padding(bottom = 10.dp),
                                            value = viewModel.shape,
                                            label = Localize.cigar_details_shape,
                                            items = CigarShapes.enumValues(),
                                            onClick = {
                                                viewModel.shape = it.second
                                            }
                                        )
                                        TextStyled(
                                            viewModel.length,
                                            TextStyles.Subhead,
                                            labelStyle = TextStyles.Subhead,
                                            label = Localize.cigar_details_length,
                                            editable = viewModel.editing,
                                            modifier = Modifier.padding(bottom = 10.dp),
                                            onValueChange = {
                                                viewModel.length = it
                                            }
                                        )
                                        TextStyled(
                                            if (viewModel.gauge == 0L) "" else viewModel.gauge.toString(),
                                            TextStyles.Subhead,
                                            labelStyle = TextStyles.Subhead,
                                            label = Localize.cigar_details_gauge,
                                            editable = viewModel.editing,
                                            modifier = Modifier.padding(bottom = 10.dp),
                                            onValueChange = {
                                                viewModel.gauge =
                                                    if (it.isNotBlank()) it.toLong() else 0
                                            },
                                            inputMode = KeyboardType.Number
                                        )
                                    }
                                }
                            }
                            //Cigar tobacco
                            Column {
                                ValuesCard(
                                    label = Localize.cigar_details_tobacco,
                                    vertical = true
                                ) {
                                    TextStyled(
                                        viewModel.wrapper,
                                        TextStyles.Subhead,
                                        labelStyle = TextStyles.Subhead,
                                        label = Localize.cigar_details_wrapper,
                                        editable = viewModel.editing,
                                        modifier = Modifier.padding(bottom = 10.dp),
                                        onValueChange = {
                                            viewModel.wrapper = it
                                        }
                                    )
                                    TextStyled(
                                        viewModel.binder,
                                        TextStyles.Subhead,
                                        labelStyle = TextStyles.Subhead,
                                        label = Localize.cigar_details_binder,
                                        editable = viewModel.editing,
                                        modifier = Modifier.padding(bottom = 10.dp),
                                        onValueChange = {
                                            viewModel.binder = it
                                        }
                                    )
                                    TextStyled(
                                        viewModel.filler,
                                        TextStyles.Subhead,
                                        labelStyle = TextStyles.Subhead,
                                        label = Localize.cigar_details_filler,
                                        editable = viewModel.editing,
                                        modifier = Modifier.padding(bottom = 10.dp),
                                        onValueChange = {
                                            viewModel.filler = it
                                        }
                                    )
                                    if (!viewModel.editing) {
                                        TextStyled(
                                            CigarStrength.localized(viewModel.strength),
                                            TextStyles.Subhead,
                                            labelStyle = TextStyles.Subhead,
                                            label = Localize.cigar_details_strength,
                                            modifier = Modifier.padding(bottom = 10.dp),
                                        )
                                    } else {
                                        ValuePicker(
                                            modifier = Modifier.padding(bottom = 10.dp),
                                            value = CigarStrength.localized(viewModel.strength),
                                            label = Localize.cigar_details_strength,
                                            items = CigarStrength.enumValues(),
                                            onClick = {
                                                viewModel.strength = it.first
                                            }
                                        )
                                    }
                                }
                            }
                            //Cigar ratings
                            Column(horizontalAlignment = Alignment.Start) {
                                ValuesCard(
                                    label = Localize.cigar_details_ratings,
                                    vertical = viewModel.editing
                                ) {
                                    if (!viewModel.editing) {
                                        ValueCard(
                                            Localize.cigar_details_rating,
                                            "${viewModel.rating}"
                                        )
                                        ValueCard(
                                            Localize.cigar_details_myrating,
                                            "${viewModel.myrating}"
                                        ) {
                                            viewModel.rate()
                                        }
                                        IconButton(onClick = {
                                            viewModel.favorite()
                                        }) {
                                            val icon =
                                                if (viewModel.favorites) Images.icon_star_filled else Images.icon_star_empty
                                            loadIcon(icon, Size(64.0F, 64.0F))
                                        }
                                    } else {
                                        TextStyled(
                                            if (viewModel.rating == 0L) "" else viewModel.rating.toString(),
                                            TextStyles.Subhead,
                                            labelStyle = TextStyles.Subhead,
                                            label = Localize.cigar_details_rating,
                                            editable = viewModel.editing,
                                            modifier = Modifier.padding(bottom = 10.dp),
                                            onValueChange = {
                                                viewModel.rating =
                                                    if (it.isNotBlank()) it.toLong() else 0
                                            },
                                            inputMode = KeyboardType.Number
                                        )
                                        TextStyled(
                                            if (viewModel.myrating == 0L) "" else viewModel.myrating.toString(),
                                            TextStyles.Subhead,
                                            labelStyle = TextStyles.Subhead,
                                            label = Localize.cigar_details_myrating,
                                            editable = viewModel.editing,
                                            modifier = Modifier.padding(bottom = 10.dp),
                                            onValueChange = {
                                                viewModel.myrating =
                                                    if (it.isNotBlank()) it.toLong() else 0
                                            },
                                            inputMode = KeyboardType.Number
                                        )
                                    }
                                }
                            }
                            //Cigar humidors
                            if (!viewModel.editing) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(((viewModel.humidors.count() + 2) * 48).dp)
                                ) {
                                    ValuesCard(
                                        label = Localize.cigar_details_humidors,
                                        vertical = true
                                    ) {
                                        LazyColumn(
                                            verticalArrangement = Arrangement.Top,
                                            modifier = Modifier
                                                .fillMaxSize()
                                        )
                                        {
                                            item {
                                                TextStyled(
                                                    Localize.cigar_details_total(viewModel.count),
                                                    TextStyles.Subhead,
                                                    modifier = Modifier.padding(bottom = 10.dp),
                                                )
                                            }
                                            items(viewModel.humidors, key = { item -> item.key }) {
                                                HumidorListRow(it, viewModel)
                                                Spacer(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(4.dp)
                                                        .background(Color.Transparent)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            //Cigar Notes and Link
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
                                    editable = viewModel.editing,
                                    onValueChange = {
                                        viewModel.notes = it
                                    }
                                )
                            }
                            Column(
                                modifier = Modifier.padding(bottom = 10.dp)
                            ) {
                                TextStyled(
                                    viewModel.link,
                                    TextStyles.Subhead,
                                    label = Localize.cigar_details_link,
                                    labelStyle = TextStyles.Subhead,
                                    editable = viewModel.editing,
                                    maxLines = 1,
                                    onValueChange = {
                                        viewModel.link = it
                                    }
                                )
                            }
                        }

                    }
                }
            }
        }
    }

    @Composable
    fun HumidorListRow(item: HumidorCigar, viewModel: CigarsDetailsScreenViewModel) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = materialColor(MaterialColors.color_transparent),
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = {
                    if (item.humidor != null) viewModel.openHumidor(item.humidor)
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
                    item.humidor?.name,
                    TextStyles.Subhead,
                )
                ValueCard(null, "${item.count}") {
                    if (item.humidor != null) viewModel.addToHumidor(item)
                }
            }
        }
    }

    @Composable
    fun HumidorCigarsDialog(onDismissRequest: () -> Unit) {
        val count = remember { mutableStateOf("0") }
        LaunchedEffect(viewModel.humidorCigarsCount) {
            count.value = (viewModel.humidorCigarsCount?.count ?: 0).toString()
        }
        if (viewModel.humidorCigarsCount != null) {
            Dialog(onDismissRequest = { onDismissRequest() }) {
                Card(
                    modifier = Modifier.wrapContentSize(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = materialColor(MaterialColors.color_surfaceVariant),
                        contentColor = materialColor(MaterialColors.color_primary),
                    ),
                ) {
                    Column(
                        modifier = Modifier.wrapContentSize().padding(24.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        TextStyled(
                            Localize.cigar_details_count_dialog,
                            TextStyles.Headline,
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth().padding(top = 24.dp, bottom = 24.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            IconButton(onClick = {
                                val current = (count.value.ifBlank { "0" }).toInt()
                                count.value = if (current > 0) current.dec().toString() else "0"
                            }) {
                                loadIcon(Images.icon_menu_minus, Size(24.0F, 24.0F))
                            }
                            TextStyled(
                                count.value,
                                TextStyles.Headline,
                                modifier = Modifier.padding(horizontal = 10.dp).wrapContentSize()
                                    .weight(1f),
                                editable = true,
                                maxLines = 1,
                                inputMode = KeyboardType.Number,
                                center = true,
                                onValueChange = {
                                    count.value = it
                                }
                            )
                            IconButton(onClick = {
                                count.value = (count.value.ifBlank { "0" }).toInt().inc().toString()
                            }) {
                                loadIcon(Images.icon_menu_plus, Size(24.0F, 24.0F))
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                        ) {
                            DialogButton(
                                title = Localize.button_cancel,
                                onClick = { viewModel.humidorCigarsCount = null })
                            DialogButton(
                                enabled = viewModel.verifyFields(),
                                title = Localize.button_save,
                                onClick = {
                                    viewModel.updateCigarCount(
                                        viewModel.humidorCigarsCount!!,
                                        count.value.toLong()
                                    )
                                })
                        }
                    }
                }
            }
        }
    }
}