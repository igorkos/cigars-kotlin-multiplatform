/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 3/28/24, 10:57 AM
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

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.util.Objects

class ComposeFileProvider : FileProvider() {
    companion object {
        fun getImageUri(context: Context): Uri {
            // 1
            val tempFile = File.createTempFile(
                "picture_${System.currentTimeMillis()}", ".png", context.cacheDir
            ).apply {
                createNewFile()
            }
            // 2
            val authority = context.applicationContext.packageName + ".provider"
            // 3
            println("getImageUri: ${tempFile.absolutePath}")
            return getUriForFile(
                Objects.requireNonNull(context),
                authority,
                tempFile,
            )
        }
    }
}