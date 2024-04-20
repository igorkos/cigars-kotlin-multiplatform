/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/16/24, 6:35 PM
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

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

data class CallStackEntry(val file: String?, val line: Int?, val function: String?)


class Log {
    companion object {
        fun initLog() {
            Napier.base(DebugAntilog())
        }

        fun debug(message: String) {
            Napier.d(tag = getTag()) { message }
        }

        fun debug(message: () -> String) {
            Napier.d(tag = getTag()) { message() }
        }

        fun error(message: String) {
            Napier.e(tag = getTag()) { message }
        }

        fun error(message: () -> String) {
            Napier.d(tag = getTag()) { message() }
        }

        fun info(message: String) {
            Napier.i(tag = getTag()) { message }
        }

        fun info(message: () -> String) {
            Napier.d(tag = getTag()) { message() }
        }

        fun warn(message: String) {
            Napier.w(tag = getTag()) { message }
        }

        fun warn(message: () -> String) {
            Napier.d(tag = getTag()) { message() }
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