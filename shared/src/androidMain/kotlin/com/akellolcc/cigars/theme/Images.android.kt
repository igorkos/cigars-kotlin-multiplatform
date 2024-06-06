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