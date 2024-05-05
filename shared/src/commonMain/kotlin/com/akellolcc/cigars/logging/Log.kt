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

package com.akellolcc.cigars.logging

import com.akellolcc.cigars.utils.getPlatform
import io.github.aakira.napier.Antilog
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

data class CallStackEntry(val file: String?, val line: Int?, val function: String?)

enum class AnalyticsEvents(val event: String) {
    None("none"),
    Log("log"),
    ScreenEnter("select_content"),
}

enum class AnalyticsParams(val parm: String) {
    ContentType("content_type"),
    ContentId("content_id"),
    Platform("platform")
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
                it.params += mapOf(AnalyticsParams.Platform.parm to getPlatform().name)
            }
        }
    }
}

class Log {
    companion object {
        private var analyticsCallback: ((AnalyticsEvent) -> Unit)? = null
        fun initLog(antilog: Any? = null, analytics: (AnalyticsEvent) -> Unit) {
            analyticsCallback = analytics
            Napier.base(antilog as? Antilog ?: DebugAntilog())
            debug("App started", analytics = AnalyticsEvents.Log)
        }

        fun debug(
            message: String,
            analytics: AnalyticsEvents = AnalyticsEvents.None,
            params: Map<String, String>? = null
        ) {
            Napier.d(tag = getTag()) { message }
            analyticLog(analytics, "debug", message, params)
        }

        fun error(
            message: String,
            analytics: AnalyticsEvents = AnalyticsEvents.None,
            params: Map<String, String>? = null
        ) {
            Napier.e(tag = getTag()) { message }
            analyticLog(analytics, "error", message, params)
        }

        fun info(
            message: String,
            analytics: AnalyticsEvents = AnalyticsEvents.None,
            params: Map<String, String>? = null
        ) {
            Napier.i(tag = getTag()) { message }
            analyticLog(analytics, "info", message, params)
        }


        fun warn(
            message: String,
            analytics: AnalyticsEvents = AnalyticsEvents.None,
            params: Map<String, String>? = null
        ) {
            Napier.w(tag = getTag()) { message }
            analyticLog(analytics, "warn", message, params)
        }

        fun analytics(event: AnalyticsEvent) {
            analyticsCallback?.invoke(event)
        }

        private fun analyticLog(
            analytics: AnalyticsEvents,
            level: String,
            message: String,
            params: Map<String, String>? = null
        ) {
            when (analytics) {
                AnalyticsEvents.None -> null
                AnalyticsEvents.Log -> {
                    AnalyticsEvent.create(
                        analytics,
                        mapOf("level" to level, "message" to message) + (params ?: emptyMap())
                    )
                }

                AnalyticsEvents.ScreenEnter -> {
                    AnalyticsEvent.create(
                        analytics,
                        mapOf(AnalyticsParams.ContentType.parm to "screen") + (params ?: emptyMap())
                    )
                }
            }?.let {
                analytics(it)
            }
        }

        private fun getTag(): String {
            val call = getCallStack()
            return if (call != null) {
                if (call.file != null) {
                    "cg: " + call.file + " (" + call.line + ")"
                } else {
                    "cg: " + call.function
                }
            } else {
                "Cigars"
            }
        }
    }
}

expect fun getCallStack(): CallStackEntry?