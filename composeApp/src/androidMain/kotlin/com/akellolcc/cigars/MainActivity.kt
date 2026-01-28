package com.akellolcc.cigars

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.os.bundleOf
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.utils.setAppContext
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import kotlin.collections.component1
import kotlin.collections.component2

class MainActivity : ComponentActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        setAppContext(this.applicationContext)
        firebaseAnalytics = Firebase.analytics
        Log.initLog { event ->
            val bundle = bundleOf()
            event.params.forEach { (key, value) ->
                bundle.putString(key, value)
            }
            firebaseAnalytics.logEvent(event.event.event, bundle)
        }
        super.onCreate(savedInstanceState)

        setContent {
            MainView()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    MainView()
}