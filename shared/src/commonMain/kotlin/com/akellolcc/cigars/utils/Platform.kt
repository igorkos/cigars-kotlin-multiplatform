package com.akellolcc.cigars.utils

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun setAppContext(context: Any);