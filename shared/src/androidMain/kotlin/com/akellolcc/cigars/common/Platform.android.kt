package com.akellolcc.cigars.common

class AndroidPlatform : Platform {
    override val name: String = "Android ${android.os.Build.VERSION.SDK_INT}"
}


actual fun getPlatformName(): String = "Android"

actual fun getPlatform(): Platform = AndroidPlatform()