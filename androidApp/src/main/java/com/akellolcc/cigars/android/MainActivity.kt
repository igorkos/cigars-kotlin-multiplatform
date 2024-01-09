package com.akellolcc.cigars.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import com.akellolcc.cigars.common.MainView
import com.akellolcc.cigars.common.databases.Database
import com.akellolcc.cigars.common.databases.DatabaseDriverFactory
import com.akellolcc.cigars.common.logging.initLog

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        initLog()
        Database.createInstance(DatabaseDriverFactory(this.applicationContext))
        //installSplashScreen()
        super.onCreate(savedInstanceState)

        setContent {
            MainView()
        }
    }
}
