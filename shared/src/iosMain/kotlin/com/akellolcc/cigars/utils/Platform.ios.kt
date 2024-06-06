/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/28/24, 1:52 PM
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
 ******************************************************************************************************************************************/

package com.akellolcc.cigars.utils

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseApp
import dev.gitlive.firebase.FirebaseOptions
import dev.gitlive.firebase.initialize
import platform.UIKit.UIDevice

class IOSPlatform : Platform {
    override val name: String =
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()
actual fun setAppContext(context: Any) {
}

val firebaseOptions: FirebaseOptions
    get() = FirebaseOptions(
        applicationId = "1:888991141757:ios:0645c41b9abfdb7f724614",
        apiKey = "AIzaSyCnlvU3Mwd71UV3blX0iYSKPJBIsz1baZc",
        storageBucket = "cigars-e3746.appspot.com",
        projectId = "cigars-e3746",
        gcmSenderId = "888991141757",
    )

actual fun initFirebase(): FirebaseApp? {
    return Firebase.initialize()
}