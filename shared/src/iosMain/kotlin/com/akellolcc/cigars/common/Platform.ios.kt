package com.akellolcc.cigars.common

import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatformName(): String = "iOS"

actual fun getPlatform(): Platform = IOSPlatform()