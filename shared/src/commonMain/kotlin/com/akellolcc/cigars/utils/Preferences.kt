/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/1/24, 1:16 AM
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
 ******************************************************************************************************************************************/

package com.akellolcc.cigars.utils

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set

class Preferences {
    enum class Keys(val key: String, val default: Any?) {
        FirstStart("firstStart", true)
    }

    companion object {
        private var instance: Preferences? = null

        private fun createInstance(): Preferences {
            if (instance == null)
                instance = Preferences()
            return instance!!
        }

        fun getInstance(): Preferences {
            if (instance == null) createInstance()
            return instance!!
        }
    }

    private val storage: Settings = Settings()

    var isFirstStart: Boolean
        get() {
            return storage.getBoolean(Keys.FirstStart.key, Keys.FirstStart.default as Boolean)
        }
        set(value) {
            storage[Keys.FirstStart.key] = value
        }
}

val Pref = Preferences.getInstance()