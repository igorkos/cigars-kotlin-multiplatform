/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/22/24, 12:19 AM
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

actual fun getCallStack(): CallStackEntry? {
    val stack = Thread.currentThread().stackTrace
    val index = stack.indexOfFirst {
        it.methodName == "debug\$default" || it.methodName == "error\$default" || it.methodName == "info\$default" || it.methodName == "warn\$default"
    }

    if (index == -1) return null
    val call = stack[index + 1]
    return if (call != null) CallStackEntry(
        call.fileName,
        call.lineNumber,
        call.methodName
    ) else null
}

