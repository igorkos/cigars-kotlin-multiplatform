package com.akellolcc.cigars

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform