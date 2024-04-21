/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/21/24, 12:40 AM
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.akellolcc.cigars.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.os.bundleOf
import com.akellolcc.cigars.MainView
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.utils.setAppContext
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics


class MainActivity : ComponentActivity() {
    lateinit var firebaseAnalytics: FirebaseAnalytics
    override fun onCreate(savedInstanceState: Bundle?) {
        setAppContext(this.applicationContext)
        firebaseAnalytics = Firebase.analytics
        Log.initLog { event ->
            val bundle = bundleOf()
            event.params.forEach { (key, value) ->
                bundle.putString(key, value)
            }
            firebaseAnalytics.logEvent(event.event.name, bundle)
        }

        super.onCreate(savedInstanceState)

        /*  try {
              val field = CursorWindow::class.java.getDeclaredField("sCursorWindowSize")
              field.setAccessible(true)
              field.set(null, 100 * 1024 * 1024) //the 100MB is the new size
          } catch (e: Exception) {
              e.printStackTrace()
          }*/
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                MainView()
            }
        }
    }
}
