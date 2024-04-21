/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/21/24, 1:25 PM
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

import kotlin.math.abs
import kotlin.random.Random

val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
fun randomString() = (1..4)
    .map { Random.nextInt(0, charPool.size).let { charPool[it] } }
    .joinToString("")

val Double.fraction: String
    get() {
        val whole = toInt()
        val sign = if (whole < 0) -1 else 1
        val fraction = this - whole

        for (i in 1 until fractions.size) {
            if (abs(fraction) > (fractionValues[i] + fractionValues[i - 1]) / 2) {
                return if (fractionValues[i - 1] == 1.0) {
                    "${whole + sign}"
                } else {
                    "$whole ${fractions[i - 1]}"
                }
            }
        }
        return "$whole"
    }

val Float.fraction: String
    get() = toDouble().fraction

private val fractions = arrayOf(
    "",                           // 16/16
    "\u00B9\u2075/\u2081\u2086",  // 15/16
    "\u215E",                     // 7/8
    "\u00B9\u00B3/\u2081\u2086",  // 13/16
    "\u00BE",                     // 3/4
    "\u00B9\u00B9/\u2081\u2086",  // 11/16
    "\u215D",                     // 5/8
    "\u2079/\u2081\u2086",        // 9/16
    "\u00BD",                     // 1/2
    "\u2077/\u2081\u2086",        // 7/16
    "\u215C",                     // 3/8
    "\u2075/\u2081\u2086",        // 5/16
    "\u00BC",                     // 1/4
    "\u00B3/\u2081\u2086",        // 3/16
    "\u215B",                     // 1/8
    "\u00B9/\u2081\u2086",        // 1/16
    ""                            // 0/16
)

private val fractionValues = arrayOf(
    1.0,
    15.0 / 16, 7.0 / 8, 13.0 / 16, 3.0 / 4, 11.0 / 16,
    5.0 / 8, 9.0 / 16, 1.0 / 2, 7.0 / 16, 3.0 / 8,
    5.0 / 16, 1.0 / 4, 3.0 / 16, 1.0 / 8, 1.0 / 16,
    0.0
)
