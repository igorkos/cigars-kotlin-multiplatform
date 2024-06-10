/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/10/24, 12:16 PM
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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.akellolcc.cigars.camera.PermissionType
import com.akellolcc.cigars.camera.SharedImage
import com.akellolcc.cigars.camera.createPermissionsManager
import com.akellolcc.cigars.camera.rememberCameraManager
import com.akellolcc.cigars.databases.models.Cigar
import com.akellolcc.cigars.mvvm.base.BaseImagesViewScreenViewModel
import com.akellolcc.cigars.mvvm.base.CigarImagesViewScreenViewModel
import com.akellolcc.cigars.mvvm.base.HumidorImagesViewScreenViewModel
import com.akellolcc.cigars.mvvm.base.createViewModel
import com.akellolcc.cigars.screens.components.BackButton
import com.akellolcc.cigars.screens.components.PagedCarousel
import com.akellolcc.cigars.screens.components.ProgressIndicator
import com.akellolcc.cigars.screens.navigation.ITabItem
import com.akellolcc.cigars.screens.navigation.NavRoute
import com.akellolcc.cigars.theme.DefaultTheme
import com.akellolcc.cigars.theme.Images
import com.akellolcc.cigars.theme.MaterialColors
import com.akellolcc.cigars.theme.loadIcon
import com.akellolcc.cigars.theme.materialColor
import com.akellolcc.cigars.utils.ui.dpToPx
import com.akellolcc.cigars.utils.ui.screenWidth
import com.preat.peekaboo.image.picker.ResizeOptions
import com.preat.peekaboo.image.picker.SelectionMode
import com.preat.peekaboo.image.picker.rememberImagePickerLauncher
import kotlinx.coroutines.launch
import kotlin.jvm.Transient
import kotlin.math.roundToInt

class ImagesViewScreen(override val route: NavRoute) : ITabItem<BaseImagesViewScreenViewModel> {
    @kotlinx.serialization.Transient
    @Transient
    override lateinit var viewModel: BaseImagesViewScreenViewModel

    @Composable
    fun imageActions() {
        val coroutineScope = rememberCoroutineScope()
        val resizeOptions = ResizeOptions(
            width = screenWidth().dpToPx(), // Custom width
            height = (screenWidth().dpToPx() * 0.8).roundToInt(), // Custom height
            resizeThresholdBytes = 2 * 1024 * 1024L, // Custom threshold for 2MB,
        )

        val permissionsManager = createPermissionsManager(viewModel)

        val multipleImagePicker = rememberImagePickerLauncher(
            selectionMode = SelectionMode.Multiple(),
            scope = coroutineScope,
            resizeOptions = resizeOptions,
            onResult = { byteArrays ->
                viewModel.addImages(byteArrays.map {
                    SharedImage(it, null)
                })
            }
        )

        val cameraManager = rememberCameraManager {
            coroutineScope.launch {
                it?.let {
                    viewModel.addImages(listOf(it))
                }
            }
        }

        if (!viewModel.isPermissionGranted) {
            if (permissionsManager.isPermissionGranted(PermissionType.CAMERA)) {
                viewModel.permissionCamera = true
            } else {
                permissionsManager.askPermission(PermissionType.CAMERA)
            }
            if (permissionsManager.isPermissionGranted(PermissionType.GALLERY)) {
                viewModel.permissionGallery = true
            } else {
                permissionsManager.askPermission(PermissionType.GALLERY)
            }
        }

        if (viewModel.launchCamera) {
            cameraManager.launch()
            viewModel.launchCamera = false
        }
        if (viewModel.launchGallery) {
            multipleImagePicker.launch()
            viewModel.launchGallery = false
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        viewModel =
            rememberScreenModel {
                val params = route.data as Pair<*, *>
                if (params.first is Cigar) {
                    createViewModel(CigarImagesViewScreenViewModel::class, route.data)
                } else {
                    createViewModel(HumidorImagesViewScreenViewModel::class, route.data)
                }
            }

        viewModel.observeEvents(route.route) {
            handleAction(it, navigator)
        }
        imageActions()

        LaunchedEffect(Unit) {
            viewModel.paging()
        }

        DefaultTheme {
            Scaffold(
                modifier = Modifier.fillMaxSize().semantics { contentDescription = route.semantics },
                topBar = {
                    CenterAlignedTopAppBar(
                        navigationIcon = {
                            BackButton {
                                viewModel.onBackPress()
                            }
                        },
                        actions = {
                            IconButton(
                                modifier = Modifier.testTag("camera_button"),
                                onClick = { viewModel.launchCamera = true }) {
                                loadIcon(Images.icon_menu_camera, Size(24.0F, 24.0F))
                            }
                            IconButton(
                                modifier = Modifier.testTag("gallery_button"),
                                onClick = { viewModel.launchGallery = true }) {
                                loadIcon(Images.icon_menu_image, Size(24.0F, 24.0F))
                            }
                        },
                        title = {})
                },
            ) {
                CompositionLocalProvider(
                    LocalContentColor provides materialColor(MaterialColors.color_onPrimaryContainer)
                ) {
                    if (viewModel.loading) {
                        Box(
                            modifier = Modifier.fillMaxSize()
                                .background(materialColor(MaterialColors.color_transparent)),
                            contentAlignment = Alignment.Center
                        ) {
                            ProgressIndicator(size = 64f)
                        }
                    } else {
                        Column(
                            horizontalAlignment = Alignment.Start,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            PagedCarousel(
                                viewModel.items,
                                loading = viewModel.loading,
                                scale = ContentScale.Fit,
                                select = viewModel.select,
                            )
                        }
                    }
                }
            }
        }
    }

}