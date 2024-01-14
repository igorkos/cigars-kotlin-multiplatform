package com.akellolcc.cigars.logging

actual fun getCallStack(): CallStackEntry? {
    val stack = Thread.currentThread().stackTrace
    val call = stack[5]
    return if(call != null) CallStackEntry(call.fileName, call.lineNumber, call.methodName) else null
}