/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/10/24, 10:05 PM
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

import kotlin.experimental.ExperimentalNativeApi

@OptIn(ExperimentalNativeApi::class)
actual fun getCallStack(): CallStackEntry? {
    /*val stack = Throwable().getStackTrace()
    for (i in stack.indices) {
        println("stack: $i")
    }
    val call = stack.firstNotNullOfOrNull {
        it.takeIf { record ->
            !(record.contains("kfun:kotlin.Throwable") || record.contains("kfun:logging"))
        }
    }
    if (call != null) {
        println("call: $call")
        val items = call.split("kfun:")
        var function = items[1].split("androidx")[0]
        val re = Regex("[^A-Za-z0-9 ]")
        function = re.replace(function, "")
        return CallStackEntry(null, null, function)
    }*/

    return null//if(call != null)  else null
}