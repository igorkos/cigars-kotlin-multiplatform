package com.akellolcc.cigars.common

interface Platform {
    val name: String
}

expect fun getPlatformName(): String

expect fun getPlatform(): Platform
