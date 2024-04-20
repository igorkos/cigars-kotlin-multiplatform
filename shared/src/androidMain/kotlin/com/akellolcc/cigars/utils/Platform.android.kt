package com.akellolcc.cigars.utils

import android.content.Context
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseApp
import dev.gitlive.firebase.FirebaseOptions
import dev.gitlive.firebase.initialize

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
    return Firebase.initialize(context = appContext, options = firebaseOptions)
}