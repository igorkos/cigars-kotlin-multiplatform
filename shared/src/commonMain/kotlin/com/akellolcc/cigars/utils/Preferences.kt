package com.akellolcc.cigars.utils

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set

class Preferences {
    enum class Keys(val key: String, val default: Any?){
        FirstStart("firstStart", true)
    }

    companion object {
        private var instance : Preferences? = null

        private fun  createInstance(): Preferences {
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
        get() { return storage.getBoolean(Keys.FirstStart.key, Keys.FirstStart.default as Boolean)}
        set(value: Boolean) {
            storage[Keys.FirstStart.key] = value
        }
}

val Pref = Preferences.getInstance()