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

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.get
import kotlinx.cinterop.reinterpret
import org.jetbrains.skia.Image
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class SharedImage actual constructor(
    private val imageData: ByteArray?,
    actual val tempPath: String?
) {
    private var image: UIImage? = null

    constructor(image: UIImage?, tempPath: String?) : this(imageData = null, tempPath) {
        this.image = image
    }

    @OptIn(ExperimentalForeignApi::class)
    actual fun toByteArray(): ByteArray? {
        if (imageData != null) return imageData

        return if (image != null) {
            val imageData = UIImageJPEGRepresentation(image!!, COMPRESSION_QUALITY)
                ?: throw IllegalArgumentException("image data is null")
            val bytes = imageData.bytes ?: throw IllegalArgumentException("image bytes is null")
            val length = imageData.length

            val data: CPointer<ByteVar> = bytes.reinterpret()
            ByteArray(length.toInt()) { index -> data[index] }
        } else {
            null
        }

    }

    actual fun toImageBitmap(): ImageBitmap? {
        val byteArray = toByteArray()
        return if (byteArray != null) {
            Image.makeFromEncoded(byteArray).toComposeImageBitmap()
        } else {
            null
        }
    }
}