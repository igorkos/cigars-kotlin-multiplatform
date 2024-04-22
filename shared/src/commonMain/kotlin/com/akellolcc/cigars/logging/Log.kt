/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/21/24, 10:30 PM
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

package com.akellolcc.cigars.logging

import com.akellolcc.cigars.utils.getPlatform
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

data class CallStackEntry(val file: String?, val line: Int?, val function: String?)

enum class AnalyticsEvents {
    Log,
}

data class AnalyticsEvent(
    val event: AnalyticsEvents,
    var params: Map<String, String> = mutableMapOf()
) {
    companion object {
        fun create(
            event: AnalyticsEvents,
            params: Map<String, String> = emptyMap()
        ): AnalyticsEvent {
            return AnalyticsEvent(event, params).also {
                it.params += mapOf("Platform" to getPlatform().name)
            }
        }
    }
}

class Log {
    companion object {
        private var analyticsCallback: ((AnalyticsEvent) -> Unit)? = null
        fun initLog(analytics: (AnalyticsEvent) -> Unit) {
            analyticsCallback = analytics
            Napier.base(DebugAntilog())
            debug("App started", analytics = true)
        }

        fun debug(message: String, analytics: Boolean = false) {
            Napier.d(tag = getTag()) { message }
            analyticLog(analytics, "debug", message)
        }

        fun error(message: String, analytics: Boolean = false) {
            Napier.e(tag = getTag()) { message }
            analyticLog(analytics, "error", message)
        }

        fun info(message: String, analytics: Boolean = false) {
            Napier.i(tag = getTag()) { message }
            analyticLog(analytics, "info", message)
        }


        fun warn(message: String, analytics: Boolean = false) {
            Napier.w(tag = getTag()) { message }
            analyticLog(analytics, "warn", message)
        }

        fun analytics(event: AnalyticsEvent) {
            analyticsCallback?.invoke(event)
        }

        private fun analyticLog(analytics: Boolean, level: String, message: String) {
            if (analytics) {
                analytics(
                    AnalyticsEvent.create(
                        AnalyticsEvents.Log,
                        mapOf("level" to level, "message" to message)
                    )
                )
            }
        }

        private fun getTag(): String? {
            val call = getCallStack()
            return if (call != null) {
                if (call.file != null) {
                    "cg: " + call.file + " (" + call.line + ")"
                } else {
                    "cg: " + call.function
                }
            } else {
                null
            }
        }
    }
}

expect fun getCallStack(): CallStackEntry?