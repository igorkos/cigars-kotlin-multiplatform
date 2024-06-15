/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/11/24, 5:45 PM
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

package com.akellolcc.cigars.screens.components.search.data

import com.akellolcc.cigars.screens.components.transformations.InputMode
import dev.icerock.moko.resources.ImageResource

data class FilterParameter<T>(
    val key: String,
    var value: T,
    var label: String,
    var icon: ImageResource? = null,
    var selectable: Boolean = true
) :
    Comparable<FilterParameter<T>> {

    constructor(key: String, value: T) : this(key, value, "", null, true)

    constructor(key: String, value: T, label: String) : this(key, value, label, null, true)

    val keyboardType: InputMode
        get() = when (value) {
            is Long -> InputMode.Number
            is Double -> InputMode.Inches
            is Boolean -> InputMode.Number
            else -> InputMode.Text
        }

    @Suppress("UNCHECKED_CAST")
    fun set(v: String) {
        this.value = try {
            when (value) {
                is Long -> v.toLong() as T
                is Double -> v.toDouble() as T
                is Boolean -> v.toBoolean() as T
                else -> v as T
            }
        } catch (e: Exception) {
            when (value) {
                is Long -> 0.toLong() as T
                is Double -> 0.toDouble() as T
                is Boolean -> false as T
                else -> throw e
            }
        }
    }

    override fun compareTo(other: FilterParameter<T>): Int {
        if (other == this) return 0
        if (key != other.key) return key.compareTo(other.key)
        when (value) {
            is String -> {
                return (value as String).compareTo(other.value as String)
            }

            is Long -> {
                return (value as Long).compareTo(other.value as Long)
            }

            is Double -> {
                return (value as Double).compareTo(other.value as Double)
            }

            is Boolean -> {
                return (value as Boolean).compareTo(other.value as Boolean)
            }

            else -> {
                return -1
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as FilterParameter<*>

        if (key != other.key) return false
        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        var result = key.hashCode()
        result = 31 * result + (value.hashCode())
        return result
    }

    override fun toString(): String {
        return "FilterParameter(key='$key', value=$value)"
    }

}

fun compareFilterParameters(list1: List<FilterParameter<*>>, list2: List<FilterParameter<*>>): Boolean {
    if (list1.size != list2.size) return false
    val sorted1 = list1.sortedBy { it.key }
    val sorted2 = list2.sortedBy { it.key }
    return sorted1.zip(sorted2).all { (p1, p2) -> p1 == p2 }
}

