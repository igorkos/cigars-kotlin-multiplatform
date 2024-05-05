/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/1/24, 1:16 AM
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

package com.akellolcc.cigars.camera

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.preat.peekaboo.image.picker.FilterOptions
import com.preat.peekaboo.image.picker.ResizeOptions

class TakePictureCrop : ActivityResultContracts.TakePicture() {
    override fun createIntent(context: Context, input: Uri): Intent {
        val intent = super.createIntent(context, input)
        intent.putExtra("crop", "true")
        intent.putExtra("outputX", 200)
        intent.putExtra("outputY", 200)
        intent.putExtra("aspectX", 1)
        intent.putExtra("aspectY", 1)
        intent.putExtra("scale", true)
        return intent
    }
}

@Composable
actual fun rememberCameraManager(
    resizeOptions: ResizeOptions,
    filterOptions: FilterOptions,
    onResult: (SharedImage?) -> Unit
): CameraManager {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val contentResolver: ContentResolver = context.contentResolver
    var tempPhotoUri by remember { mutableStateOf(value = Uri.EMPTY) }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                PeekabooImageResizer.resizeImageAsync(
                    contentResolver = contentResolver,
                    coroutineScope = coroutineScope,
                    uri = tempPhotoUri,
                    width = resizeOptions.width,
                    height = resizeOptions.height,
                    resizeThresholdBytes = resizeOptions.resizeThresholdBytes,
                    compressionQuality = resizeOptions.compressionQuality,
                    filterOptions = filterOptions,
                ) { resizedImage ->
                    if (resizedImage != null) {
                        onResult.invoke(SharedImage(resizedImage, tempPhotoUri.path!!))
                    }
                }

            }
        }

    return remember {
        CameraManager {
            tempPhotoUri = ComposeFileProvider.getImageUri(context)
            cameraLauncher.launch(tempPhotoUri)
        }
    }
}

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class CameraManager actual constructor(
    private val onLaunch: () -> Unit
) {
    actual fun launch() {
        onLaunch()
    }
}