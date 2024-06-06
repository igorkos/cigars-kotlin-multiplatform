/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/21/24, 1:08 PM
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

package com.akellolcc.cigars.utils

import kotlinx.datetime.Instant

expect fun String.parseDate(pattern: String, defValue: Long = 0L): Long
expect fun Instant.formatDate(pattern: String, defValue: String = ""): String
const val defaultDateFormat = "MMMM d, yyyy"
fun Long.formatDate(pattern: String? = null, defValue: String = ""): String {
    return Instant.fromEpochMilliseconds(this).formatDate(pattern ?: defaultDateFormat, defValue)
}