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
        "1:888991141757:ios:0645c41b9abfdb7f724614",
        "AIzaSyCnlvU3Mwd71UV3blX0iYSKPJBIsz1baZc",
        gcmSenderId = "888991141757",
        projectId= "cigars-e3746",
)

actual fun initFirebase(): FirebaseApp? {
    return null //Firebase.initialize(context = null, options = firebaseOptions)
}