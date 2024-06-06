/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/10/24, 10:04 PM
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
 */

package com.akellolcc.cigars.camera

import androidx.annotation.FloatRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.preat.peekaboo.image.picker.FilterOptions
import com.preat.peekaboo.image.picker.ResizeOptions
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.refTo
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGRectMake
import platform.CoreGraphics.CGSize
import platform.CoreGraphics.CGSizeMake
import platform.CoreImage.CIContext
import platform.CoreImage.CIFilter
import platform.CoreImage.CIImage
import platform.CoreImage.createCGImage
import platform.CoreImage.filterWithName
import platform.Foundation.setValue
import platform.UIKit.UIApplication
import platform.UIKit.UIGraphicsBeginImageContextWithOptions
import platform.UIKit.UIGraphicsEndImageContext
import platform.UIKit.UIGraphicsGetImageFromCurrentImageContext
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerCameraCaptureMode
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerEditedImage
import platform.UIKit.UIImagePickerControllerOriginalImage
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.darwin.NSObject
import platform.posix.memcpy

@Composable
actual fun rememberCameraManager(
    resizeOptions: ResizeOptions,
    filterOptions: FilterOptions,
    onResult: (SharedImage?) -> Unit
): CameraManager {
    val imagePicker = UIImagePickerController()
    val cameraDelegate = remember {
        object : NSObject(), UIImagePickerControllerDelegateProtocol,
            UINavigationControllerDelegateProtocol {
            override fun imagePickerController(
                picker: UIImagePickerController, didFinishPickingMediaWithInfo: Map<Any?, *>
            ) {
                val image =
                    didFinishPickingMediaWithInfo.getValue(UIImagePickerControllerEditedImage) as? UIImage
                        ?: didFinishPickingMediaWithInfo.getValue(
                            UIImagePickerControllerOriginalImage
                        ) as? UIImage
                val resizedImage =
                    image?.fitInto(
                        resizeOptions.width,
                        resizeOptions.height,
                        resizeOptions.resizeThresholdBytes,
                        resizeOptions.compressionQuality,
                        filterOptions,
                    )
                onResult.invoke(SharedImage(resizedImage, null))
                picker.dismissViewControllerAnimated(true, null)
            }
        }
    }
    return remember {
        CameraManager {
            imagePicker.setSourceType(UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera)
            imagePicker.setAllowsEditing(true)
            imagePicker.setCameraCaptureMode(UIImagePickerControllerCameraCaptureMode.UIImagePickerControllerCameraCaptureModePhoto)
            imagePicker.setDelegate(cameraDelegate)
            UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
                imagePicker, true, null
            )
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun UIImage.toByteArray(compressionQuality: Double): ByteArray {
    val validCompressionQuality = compressionQuality.coerceIn(0.0, 1.0)
    val jpegData = UIImageJPEGRepresentation(this, validCompressionQuality)!!
    return ByteArray(jpegData.length.toInt()).apply {
        memcpy(this.refTo(0), jpegData.bytes, jpegData.length)
    }
}

@OptIn(ExperimentalForeignApi::class, ExperimentalForeignApi::class)
private fun UIImage.fitInto(
    maxWidth: Int,
    maxHeight: Int,
    resizeThresholdBytes: Long,
    @FloatRange(from = 0.0, to = 1.0)
    compressionQuality: Double,
    filterOptions: FilterOptions,
): UIImage {
    val imageData = this.toByteArray(compressionQuality)
    if (imageData.size > resizeThresholdBytes) {
        val originalWidth = this.size.useContents { width }
        val originalHeight = this.size.useContents { height }
        val originalRatio = originalWidth / originalHeight

        val targetRatio = maxWidth.toDouble() / maxHeight.toDouble()
        val scale =
            if (originalRatio > targetRatio) {
                maxWidth.toDouble() / originalWidth
            } else {
                maxHeight.toDouble() / originalHeight
            }

        val newWidth = originalWidth * scale
        val newHeight = originalHeight * scale

        val targetSize = CGSizeMake(newWidth, newHeight)
        val resizedImage = this.resize(targetSize)

        return applyFilterToUIImage(resizedImage, filterOptions)
    } else {
        return applyFilterToUIImage(this, filterOptions)
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun UIImage.resize(targetSize: CValue<CGSize>): UIImage {
    UIGraphicsBeginImageContextWithOptions(targetSize, false, 0.0)
    this.drawInRect(
        CGRectMake(
            0.0,
            0.0,
            targetSize.useContents { width },
            targetSize.useContents { height },
        ),
    )
    val newImage = UIGraphicsGetImageFromCurrentImageContext()
    UIGraphicsEndImageContext()

    return newImage!!
}


@OptIn(ExperimentalForeignApi::class)
private fun applyFilterToUIImage(
    image: UIImage,
    filterOptions: FilterOptions,
): UIImage {
    val ciImage = CIImage.imageWithCGImage(image.CGImage)

    val filteredCIImage =
        when (filterOptions) {
            FilterOptions.GrayScale -> {
                CIFilter.filterWithName("CIPhotoEffectNoir")?.apply {
                    setValue(ciImage, forKey = "inputImage")
                }?.outputImage
            }

            FilterOptions.Sepia -> {
                CIFilter.filterWithName("CISepiaTone")?.apply {
                    setValue(ciImage, forKey = "inputImage")
                    setValue(0.8, forKey = "inputIntensity")
                }?.outputImage
            }

            FilterOptions.Invert -> {
                CIFilter.filterWithName("CIColorInvert")?.apply {
                    setValue(ciImage, forKey = "inputImage")
                }?.outputImage
            }

            FilterOptions.Default -> ciImage
        }

    val context = CIContext.contextWithOptions(null)
    return filteredCIImage?.let {
        val filteredCGImage = context.createCGImage(it, fromRect = it.extent())
        UIImage.imageWithCGImage(filteredCGImage)
    } ?: image
}

actual class CameraManager actual constructor(
    private val onLaunch: () -> Unit
) {
    actual fun launch() {
        onLaunch()
    }
}