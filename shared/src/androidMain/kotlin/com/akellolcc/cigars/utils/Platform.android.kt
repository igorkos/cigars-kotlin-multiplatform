/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/26/24, 9:06 PM
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

package com.akellolcc.cigars.utils

import android.content.Context
import dev.gitlive.firebase.FirebaseApp
import dev.gitlive.firebase.FirebaseOptions

var appContext: Context? = null;

class AndroidPlatform : Platform {
    override val name: String = "Android ${android.os.Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()
actual fun setAppContext(context: Any) {
    appContext = context as Context;
}

val firebaseOptions: FirebaseOptions
    get() = FirebaseOptions(
        "1:888991141757:android:0cd490d7b772d358724614",
        "AIzaSyDqCjCheBkbj1soAiMcA_i-SoiZCdP93cs",
        projectId = "cigars-e3746",
    )

actual fun initFirebase(): FirebaseApp? {
    return null
}