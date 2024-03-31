package com.akellolcc.cigars.screens

import TextStyled
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.akellolcc.cigars.camera.PermissionCallback
import com.akellolcc.cigars.camera.PermissionStatus
import com.akellolcc.cigars.camera.PermissionType
import com.akellolcc.cigars.camera.SharedImage
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
import com.akellolcc.cigars.mvvm.ImagesViewScreenViewModel
import com.akellolcc.cigars.navigation.ITabItem
import com.akellolcc.cigars.navigation.NavRoute
import com.akellolcc.cigars.theme.Images
import com.akellolcc.cigars.theme.Localize
import com.akellolcc.cigars.theme.MaterialColors
import com.akellolcc.cigars.theme.TextStyles
import com.akellolcc.cigars.theme.imagePainter
import com.akellolcc.cigars.theme.loadIcon
import com.akellolcc.cigars.theme.materialColor
import com.akellolcc.cigars.ui.screenWidth
import com.preat.peekaboo.image.picker.ResizeOptions
import com.preat.peekaboo.image.picker.SelectionMode
import com.preat.peekaboo.image.picker.rememberImagePickerLauncher
import com.preat.peekaboo.ui.camera.PeekabooCamera
import com.preat.peekaboo.ui.camera.rememberPeekabooCameraState
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import dev.icerock.moko.mvvm.flow.compose.observeAsActions
import kotlinx.coroutines.launch

class ImagesViewScreen(override val route: NavRoute) : ITabItem {

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
        val cigar: Cigar = (route.data as Pair<Cigar, Int>).first
        var select by remember { mutableStateOf((route.data as Pair<Cigar, Int>).second) }
        var lastSelect by remember { mutableStateOf(select) }
        val coroutineScope = rememberCoroutineScope()
        var launchCamera by remember { mutableStateOf(value = false) }
        var launchGallery by remember { mutableStateOf(value = false) }
        var loading by remember { mutableStateOf(value = true) }

        val resizeOptions = ResizeOptions(
            width = screenWidth().value.toInt(), // Custom width
            height = (screenWidth().value * 0.8).toInt(), // Custom height
            resizeThresholdBytes = 2 * 1024 * 1024L, // Custom threshold for 2MB,
        )
        val viewModel = getViewModel(
            key = "ImagesViewScreen",
            factory = viewModelFactory { ImagesViewScreenViewModel(cigar) })

        val images by viewModel.images.collectAsState()

        val multipleImagePicker = rememberImagePickerLauncher(
            // Optional: Set a maximum selection limit, e.g., SelectionMode.Multiple(maxSelection = 5).
            // Default: No limit, depends on system's maximum capacity.
            selectionMode = SelectionMode.Multiple(),
            scope = coroutineScope,
            resizeOptions = resizeOptions,
            onResult = { byteArrays ->
                lastSelect = images.size
                byteArrays.forEach {
                    // Process the selected images' ByteArrays.
                    Log.debug("Image size ${it.size}")
                    cigar.addImage(SharedImage( it, null))
                }
                viewModel.fetchImages()
            }
        )

        viewModel.actions.observeAsActions {
            when (it) {
                is ImagesViewScreenViewModel.Action.Loding -> {
                    select = lastSelect
                    loading = it.isLoading
                }
                is ImagesViewScreenViewModel.Action.ShowError -> TODO()
            }
        }

        LaunchedEffect(Unit) {
            viewModel.fetchImages()
        }

        val permissionsManager = createPermissionsManager(object : PermissionCallback {
            override fun onPermissionStatus(
                permissionType: PermissionType,
                status: PermissionStatus
            ) {
                Log.debug("Permission $permissionType -> $status")
                when (status) {
                    PermissionStatus.SHOW_RATIONAL -> {

                    }

                    PermissionStatus.DENIED -> {
                        if (permissionType == PermissionType.GALLERY) {
                            launchCamera = false
                        } else {
                            launchGallery = false
                        }
                    }

                    PermissionStatus.GRANTED -> {
                        if (permissionType == PermissionType.GALLERY) {
                            launchCamera = true
                        } else {
                            launchGallery = true
                        }
                    }
                }
            }


        })

        val cameraManager = rememberCameraManager {
            coroutineScope.launch {
                lastSelect = images.size
                it?.let {
                    Log.debug("Image size ${it.toByteArray()?.size}")
                    cigar.addImage(it)
                    viewModel.fetchImages()
                }
            }
        }

        if (launchCamera) {
            if (permissionsManager.isPermissionGranted(PermissionType.CAMERA)) {
                cameraManager.launch()
            } else {
                permissionsManager.askPermission(PermissionType.CAMERA)
            }
            launchCamera = false
        }

        if (launchGallery) {
            if (permissionsManager.isPermissionGranted(PermissionType.GALLERY)) {
                multipleImagePicker.launch()
            } else {
                permissionsManager.askPermission(PermissionType.GALLERY)
            }
            launchGallery = false
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
                                navigator.pop()
                            }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = null
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = { launchCamera = true }) {
                                loadIcon(Images.icon_menu_camera, Size(24.0F, 24.0F))
                            }
                            IconButton(onClick = { launchGallery = true }) {
                                loadIcon(Images.icon_menu_image, Size(24.0F, 24.0F))
                            }
                        },
                        title = {})
                },
            ) {
                    CompositionLocalProvider(
                        LocalContentColor provides materialColor(MaterialColors.color_onPrimaryContainer)
                    )
                    {
                        if (loading) {
                            Box(modifier = Modifier.fillMaxSize().background(materialColor(MaterialColors.color_transparent)), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(
                                    modifier = Modifier.width(64.dp),
                                )
                            }
                        } else {
                            Column(
                                horizontalAlignment = Alignment.Start,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                PagedCarousel(
                                    images,
                                    loading = loading,
                                    scale = ContentScale.Fit,
                                    select = select,
                                )
                            }
                        }
                    }
            }
        }
    }

}