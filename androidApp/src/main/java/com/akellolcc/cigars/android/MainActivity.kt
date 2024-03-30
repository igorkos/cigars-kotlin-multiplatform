package com.akellolcc.cigars.android

import android.database.CursorWindow
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import com.akellolcc.cigars.MainView
import com.akellolcc.cigars.databases.Database
import com.akellolcc.cigars.databases.DatabaseDriverFactory
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.utils.setAppContext


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setAppContext(this.applicationContext)
        Log.initLog()
        Database.createInstance(DatabaseDriverFactory(this.applicationContext))
        super.onCreate(savedInstanceState)

        try {
            val field = CursorWindow::class.java.getDeclaredField("sCursorWindowSize")
            field.setAccessible(true)
            field.set(null, 100 * 1024 * 1024) //the 100MB is the new size
        } catch (e: Exception) {
            e.printStackTrace()
        }
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
