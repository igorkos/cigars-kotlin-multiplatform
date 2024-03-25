package com.akellolcc.cigars.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.akellolcc.cigars.MainView
import com.akellolcc.cigars.databases.Database
import com.akellolcc.cigars.databases.DatabaseDriverFactory
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.navigation.NavRoute
import com.akellolcc.cigars.screens.CigarsScreen
import com.akellolcc.cigars.utils.setAppContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setAppContext(this.applicationContext)
        Log.initLog()
        Database.createInstance(DatabaseDriverFactory(this.applicationContext))
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainView()
                }
            }
        }
    }
}
