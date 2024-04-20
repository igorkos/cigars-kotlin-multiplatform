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

import android.content.ContentResolver
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.ByteArrayOutputStream
import java.io.InputStream


object BitmapUtils {
    fun getBitmapFromUri(uri: Uri, contentResolver: ContentResolver): android.graphics.Bitmap? {
        var inputStream: InputStream? = null
        try {
            inputStream = contentResolver.openInputStream(uri)
            val s = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()
            return s
        } catch (e: Exception) {
            e.printStackTrace()
            println("getBitmapFromUri Exception: ${e.message}")
            println("getBitmapFromUri Exception: ${e.localizedMessage}")
            return null
        }
    }

    fun getDataFromUri(uri: Uri, contentResolver: ContentResolver): ByteArray? {
        // this dynamically extends to take the bytes you read
        try {
            val byteBuffer = ByteArrayOutputStream()
            contentResolver.openInputStream(uri)?.let { inputStream ->
                val bufferSize = 1024
                val buffer = ByteArray(bufferSize)

                // we need to know how may bytes were read to write them to the byteBuffer
                var len = 0
                while ((inputStream.read(buffer).also { len = it }) != -1) {
                    byteBuffer.write(buffer, 0, len)
                }
                inputStream.close()
                return byteBuffer.toByteArray()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            println("getBitmapFromUri Exception: ${e.message}")
            println("getBitmapFromUri Exception: ${e.localizedMessage}")
        }
        return null
    }
}