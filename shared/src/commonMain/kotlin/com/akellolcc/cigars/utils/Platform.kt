package com.akellolcc.cigars.utils

import dev.gitlive.firebase.FirebaseApp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun setAppContext(context: Any);

expect fun initFirebase(): FirebaseApp?