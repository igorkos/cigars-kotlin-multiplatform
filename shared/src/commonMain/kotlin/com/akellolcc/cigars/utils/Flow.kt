/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/25/24, 10:00 PM
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

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector

@ExperimentalCoroutinesApi
suspend fun <T> Flow<T>.collectFirst(n: Int, collector: FlowCollector<List<T>>) {
    val list = mutableListOf<T>()
    var count = 0
    this.collect { value ->
        count++
        list.add(value)
        if (count == n) collector.emit(list)
    }
}