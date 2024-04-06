package com.akellolcc.cigars.camera

import androidx.compose.ui.graphics.ImageBitmap

var COMPRESSION_QUALITY = 0.5

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class SharedImage(imageData: ByteArray?, tempPath: String?) {
    val tempPath: String?
    fun toByteArray(): ByteArray?
    fun toImageBitmap(): ImageBitmap?
}