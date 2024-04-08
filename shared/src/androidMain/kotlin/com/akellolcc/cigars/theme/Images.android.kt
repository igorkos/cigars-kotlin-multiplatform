package com.akellolcc.cigars.theme

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import com.akellolcc.cigars.utils.appContext
import java.io.ByteArrayOutputStream


actual fun imageData(name: String): ByteArray? {
    return loadImageByName(name)?.let { resource ->
        resource.getDrawable(context = appContext!!)?.let {
            val bitmap = (it as BitmapDrawable).bitmap
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.toByteArray()
        }
    }
}