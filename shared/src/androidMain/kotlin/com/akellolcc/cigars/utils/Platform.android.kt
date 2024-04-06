package com.akellolcc.cigars.utils

import android.content.Context

var appContext: Context? = null;

class AndroidPlatform : Platform {
    override val name: String = "Android ${android.os.Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()
actual fun setAppContext(context: Any) {
    appContext = context as Context;
}