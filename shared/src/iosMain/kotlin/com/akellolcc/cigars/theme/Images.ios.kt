package com.akellolcc.cigars.theme

import com.akellolcc.cigars.camera.COMPRESSION_QUALITY
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.get
import kotlinx.cinterop.reinterpret
import platform.UIKit.UIImageJPEGRepresentation

@OptIn(ExperimentalForeignApi::class)
actual fun imageData(name: String): ByteArray? {
    return loadImageByName(name)?.let { resource ->
        resource.toUIImage()?.let { image ->
            val imageData = UIImageJPEGRepresentation(image, COMPRESSION_QUALITY)
                ?: throw IllegalArgumentException("image data is null")
            val bytes = imageData.bytes ?: throw IllegalArgumentException("image bytes is null")
            val length = imageData.length

            val data: CPointer<ByteVar> = bytes.reinterpret()
            ByteArray(length.toInt()) { index -> data[index] }
        }
    }
}