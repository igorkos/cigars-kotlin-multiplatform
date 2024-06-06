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


actual fun getCallStack(): CallStackEntry? {
    /* val stack = Throwable().getStackTrace()
     for (i in stack.indices) {
         Napier.d("stack: $i -> ${stack[i]}")
     }
     val entries = stack.filter {
         it.contains("com.akellolcc.cigars")
     }
     val call = entries.firstOrNull {
         !it.contains("com.akellolcc.cigars.logging")
     }
     Napier.d("Stack call: $call")
     if (call != null) {
         val items = call.split("kfun:")
         var function = items[1].split("androidx")[0]
         val re = Regex("[^A-Za-z0-9 ]")
         function = re.replace(function, "")
         return CallStackEntry(null, null, function)
     }
 */
    return null//if(call != null)  else null
}