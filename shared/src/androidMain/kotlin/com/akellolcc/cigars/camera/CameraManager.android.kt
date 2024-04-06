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
        intent.putExtra("crop", "true");
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
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

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
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
    )
    return remember {
        CameraManager(
            onLaunch = {
                tempPhotoUri = ComposeFileProvider.getImageUri(context)
                cameraLauncher.launch(tempPhotoUri)
            }
        )
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