package com.akellolcc.cigars.common.logging

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

public data class CallStackEntry(val file: String?, val line: Int?, val function: String?)


class Log {
    companion object {
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
                if(call.file != null) {
                    "sf: " + call.file + " (" + call.line + ")"
                } else {
                    "sf: " + call.function
                }
            } else {
                null
            }
        }
    }
}

fun initLog() {
    Napier.base(DebugAntilog())
}
expect fun getCallStack(): CallStackEntry?