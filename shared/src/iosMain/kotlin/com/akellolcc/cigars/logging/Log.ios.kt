package com.akellolcc.cigars.logging

import kotlin.experimental.ExperimentalNativeApi

@OptIn(ExperimentalNativeApi::class)
actual fun getCallStack(): CallStackEntry? {
    val stack = Throwable().getStackTrace()
    val call = stack.firstNotNullOfOrNull { it.takeIf { record -> !(record.contains("kfun:kotlin.Throwable") || record.contains("kfun:logging"))}}
    if(call != null) {
        val items = call.split("kfun:")
        var function = items[1].split("androidx")[0]
        val re = Regex("[^A-Za-z0-9 ]")
        function = re.replace(function, "")
        return CallStackEntry(null, null, function)
    }

    return null//if(call != null)  else null
}