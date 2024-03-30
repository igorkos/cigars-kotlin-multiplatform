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
            contentResolver.openInputStream(uri)?.let{ inputStream ->
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